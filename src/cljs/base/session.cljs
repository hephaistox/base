(ns base.session)

;; The CSRF token can be added through hidden fields, headers, and can be used with forms, and AJAX calls. Make sure that the token is not leaked in the server logs, or in the URL. CSRF tokens in GET requests are potentially leaked at several locations, such as the browser history, log files, network appliances that log the first line of an HTTP request, and Referer headers if the protected site links to an external site.
;; https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.md#synchronizer-token-pattern

(def ?csrf-token
  (when-let [el (.getElementById js/document "__anti-forgery-token")]
    (.getAttribute el "anti-forgery-token")))
