(ns base.endpoint.realtime.routes-test
  (:require
   [clojure.test :refer [is deftest testing]]

   [reitit.ring :as rring]
   [ring.util.http-response :as response]

   [base.endpoint.realtime.routes :as sut]
   [base.server.cst :as cst]
   ))

(deftest route2
  (let [router (rring/router (sut/routes* (constantly (response/ok "stub"))
                                          (constantly (response/ok "stub"))))
        app (rring/ring-handler router)
        page (app {:request-method :get
                   :query-string "client-id=d184907d-f1ee-491c-a49f-eb21fed56c1f"
                   :headers    {"accept-encoding" "gzip, deflate, br"}
                   :uri cst/realtime-uri})]
    ;(is (nil? page))
    (testing "Session are managed"
      (is (get-in page [:headers "Set-Cookie"])))
    (testing "Antiforgery token"
      (is (get-in page [:request-copied :anti-forgery-token])))
    (testing "Wrap params is keyed wrap-params and wrap-keyword-params"
      (is (keyword? (ffirst (get-in page [:request-copied :params])))))
    ))
