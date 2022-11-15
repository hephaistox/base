(ns base.components.file-loader
  "Allow the loading of a local file"
  (:require
   [clojure.string :as str]

   [ajax.core :as ajax]
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]

   [base.components.alert :as alert]
   [base.log :as log]
   [base.session :as session]))

;; https://developer.mozilla.org/fr/docs/Web/HTML/Element/Input/file

;;TODO Add a timer, after which the loading is stopped
(defn component
  "Create an input html entity, wired with reframe and ajax to send the file to the uri.
  Parameters are:
  * tag: each instance of the component are identified with their tag
  * message: is the string to be displayed when no file is selected
  * uri: where to post the loaded file(s)
  * field-name is the name of the field where to post the file in the post http message"
  [{:keys [tag message uri urifield]
    :or {message "Select a file"}
    :as input}]
  (if (and tag uri urifield)
    (let [files (re-frame/subscribe [::files tag])
          upload-message (re-frame/subscribe [::upload-message tag])
          network-error (re-frame/subscribe [::network-error tag])
          loading? (re-frame/subscribe [::loading? tag])]
      [:div.flex.flex-wrap.-mx-4.file-loader
       [:div.w-full.px-4.mb-12 {:class "lg:w-5/12"}
        [:div.flex.flex-row
         (let [nb-files (if @files
                          (.-count @files)
                          0)]
           [:label {:for "image-upload"}
            (if (= 0 nb-files)
              message
              (str "File"
                   (when (> 1 nb-files) "s")
                   ": (" (str/join ", "
                                   (for [file @files]
                                     (.-name file))) ")"))])
         (when @loading?
           [:div.flex.justify-center.items-center.inline-block.ml-2.float-right
            [:div.spinner-border.animate-spin.inline.w-8.h-8.border-4.rounded-full {:role "status"}]
            [:span.visually-hidden "Loading ..."]])]
        [:input#image-upload (merge input
                                    {:type "file"
                                     :class "w-full border-[1.5px] border-form-stroke rounded-lg font-medium text-body-color placeholder-body-color outline-none focus:border-primary active:border-primary
transition disabled:bg-[#F5F7FD] disabled:cursor-default cursor-pointer
file:bg-[#F5F7FD] file:border-0 file:border-solid file:border-r file:border-collapse file:border-form-stroke
file:py-3 file:px-5 file:mr-5 file:text-body-color file:cursor-pointer file:hover:bg-primary file:hover:bg-opacity-10"
                                     :on-change #(re-frame/dispatch [::files-selected tag (->  % .-target .-files) uri urifield])})]
        (when @network-error
          [alert/component {:message @network-error
                            :title "Network error"}])
        (when @upload-message
          [alert/component {:message @upload-message
                            :title "Upload error"}])]])
    [:div "Error in the component parameters"]))

(re-frame/reg-event-fx
 ::files-selected
 (fn-traced [{:keys [db]} [_ tag files uri uri-field]]
            {:db (assoc-in db [:file-upload tag]
                           {:uri uri
                            :field-name uri-field
                            :files files})
             :fx [[:dispatch [::trigger-upload tag files]]]}))

(re-frame/reg-event-db
 ::file-upload-success
 (fn-traced [db [_ tag]]
            (assoc-in db [:file-upload tag :success] true)))

(re-frame/reg-event-fx
 ::trigger-upload
 (fn-traced [{:keys [db]} [_ tag files]]
            (let [uri (get-in db [:file-upload tag :uri])
                  form-data (js/FormData.)
                  file (aget files 0)
                  field-name (get-in db [:file-upload tag :field-name])]
              (.append form-data field-name file)
              {:db (assoc-in db [:file-upload tag :loading?] true)
               :http-xhrio         {:method          :post
                                    :body            form-data
                                    :format          (ajax/json-request-format)
                                    :headers         {:x-csrf-token session/?csrf-token}
                                    :uri             uri
                                    :timeout         8000
                                    :response-format (ajax/json-response-format {:keywords? true})
                                    :on-success      [::loading-success tag]
                                    :on-failure      [::error-message tag]}})))

(re-frame/reg-event-fx
 ::loading-success
 (fn-traced [{:keys [db]} [_ tag {:keys [status _uuid  _size _filename]
                                  :as upload-response}]]
            (log/trace "upload response  is  " upload-response)
            {:db (assoc-in db [:file-upload tag :loading?] false)
             :fx [[:dispatch [::reset-selector tag]]
                  (if (= "done" status)
                    [:dispatch [::new-file-uploaded tag upload-response]]
                    ;; TODO, faut surement que j'écrive que ça s'est bien passé,
                    ;; et que je surveille l'exécution du thread
                    [:dispatch [::file-upload-error tag]])]}))

(re-frame/reg-event-fx
 ::error-message
 (fn-traced [{:keys [db]} [_ tag result]]
            {:db (if (= 0 (:status result))
                   (assoc-in db [:file-upload tag :network-error] (:status-text result))
                   (assoc-in db [:file-upload tag :upload-error] (:status-text result)))
             :fx [[:dispatch [::stop-loading tag]]]}))

(re-frame/reg-event-db
 ::reset-selector
 (fn-traced [db [_ tag]]
            (let [image-upload (. js/document (getElementById "image-upload"))]
              (set! (.-value image-upload) "")
              (assoc-in db [:file-upload tag :files]
                        nil))))

(re-frame/reg-event-db
 ::file-upload-error
 (fn-traced [db [_ tag]]
            (assoc-in db [:file-upload tag :error?] true)))



(re-frame/reg-sub
 ::files
 (fn [db [_ tag]]
   (get-in db [:file-upload tag :files])))

(re-frame/reg-sub
 ::upload-message
 (fn [db [_ tag]]
   (get-in db [:file-upload tag :upload-error])))

(re-frame/reg-sub
 ::network-error
 (fn [db [_ tag]]
   (get-in db [:file-upload tag :network-error])))

(re-frame/reg-sub
 ::loading?
 (fn [db [_ tag]]
   (get-in db [:file-upload tag :loading?])))
