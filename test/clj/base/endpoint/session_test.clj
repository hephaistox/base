(ns base.endpoint.session-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [ring.middleware.keyword-params]
   [ring.middleware.params]
   [ring.util.response :as response]
   [ring.middleware.cookies]
   [ring.middleware.session]

   [base.endpoint.session :as sut]
   [ring.middleware.session.memory :as mem]
   ))

(defn handler-count
  "Check information there
  https://github.com/ring-clojure/ring/wiki/Sessions"
  [{:keys [session] :as params}]
  (let [count   (:count session 0)
        session (assoc session :count (inc count))]
    (-> (response/response (str "You accessed this page " count " times."))
        (assoc :session session       ;; Add new session version
               :echoed-request params ;; Echo params to test in response what params where in the query
               ))))

(deftest session-types
  (with-redefs [sut/session-store (atom {})]
    (testing "Session is an atom"
      (is (= clojure.lang.Atom
             (type sut/session-store))))
    (testing "Sessions are a map"
      (is (map? (sut/sessions)))
      (is (every? string?
                  (keys (sut/sessions)))))
    (testing "wrap-session is a function"
      (is (fn? sut/wrap-session)))))

(deftest raw-handler
  (testing "Handler answer has body status params headers and proper session"
    (is (= {:status 200
            :body "You accessed this page 0 times."
            :headers {}
            :echoed-request {}
            :session {:count 1}}
           (handler-count {})))))

(deftest kw-and-params
  (let [h (-> handler-count
              ring.middleware.params/wrap-params)]
    (testing "Params handler is working"
      (is (= {:params {"a" "b"} :form-params {} :query-params {}}
             (:echoed-request (h {:params {"a" "b"}})))))
    (testing "Query params handler is working"
      (is (= {:params {"a" "b"} :form-params {} :query-params {"a" "b"} :query-string "a=b"}
             (:echoed-request (h {:query-string "a=b"}))))))
  (let [h (-> handler-count
              ring.middleware.keyword-params/wrap-keyword-params
              ring.middleware.params/wrap-params)]
    (testing "Params and keyword params are working"
      (is (= {:params {:a "b"} :form-params {} :query-params {}}
             (:echoed-request (h {:params {"a" "b"}})))))))

(deftest cookies
  (let [h (-> handler-count
              ring.middleware.cookies/wrap-cookies)]
    (testing "Test cookie"
      (is (= {:headers {"cookie" "a=b"}
              :cookies {"a" {:value "b"}}}
             (:echoed-request
              (h {:headers {"cookie" "a=b"}})))))))

;; http://www.learningclojure.com/2013/01/how-sessions-work-in-ring.html
(deftest session-test
  (let [before-session-store (sut/sessions)
        store (atom {})
        mem-store (mem/memory-store store)]
    (with-redefs [sut/session-store store
                  sut/options {:store mem-store}]
      (let [app-count (-> handler-count
                          sut/wrap-session)]
        (testing "Before first handler call, session is empty"
          (is (zero? (count (sut/sessions)))))

        (testing "First handler count call, no cookie, no session, no session key"
          (let [first-attempt (app-count {})]
            (is (= {:status 200,
                    :body "You accessed this page 0 times.",
                    :echoed-request {:cookies {}, :session {}, :session/key nil}}
                   (dissoc first-attempt :headers)))
            (is (get-in first-attempt  [:headers "Set-Cookie"]))
            (is (nil? (get-in first-attempt [:echoed-request :cookies "ring-session" :value])))))

        (testing "First handler call has updated sessions"
          (is (= 1
                 (count (sut/sessions))))
          (is (= 1
                 (-> (sut/sessions) first second :count))))

        (let [key (ffirst (sut/sessions))]
          (testing "Session can be read"
            (is (.read-session mem-store
                              key)))

          (let [second-attempt (app-count {:headers {"cookie" (str "ring-session=" key)}})]
            (testing "Cookies found"
              (is (= key
                     (get-in second-attempt [:echoed-request :cookies "ring-session" :value]))))
            (testing "Key is found"
              (is (= key
                     (get-in second-attempt [:echoed-request :session/key]))))
            (testing "Session found"
              (is (= 1
                     (get-in second-attempt [:echoed-request :session :count]))))
            (testing "Second handler count call return the updated content"
              (is (= {:body "You accessed this page 1 times."}
                     (select-keys second-attempt [:body]))))
            (testing "Second handler call has updated the same session id with an incremented count"
              (is (= 1 (count (sut/sessions))))
              (is (= 2 (-> (sut/sessions) first second :count))))))))
    (testing "Testing should not influence dev env"
      (is (= (sut/sessions)
             before-session-store)))))
