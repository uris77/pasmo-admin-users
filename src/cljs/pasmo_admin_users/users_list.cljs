(ns pasmo-admin-users.users-list
  (:require [ajax.core :refer [GET edn-response-format edn-request-format]]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened " status " " status-text)))

(defn- fetch-all
  "Fetch all users from the server."
  [handler]
  (GET "/api/users"
       {:response-format :edn
        :format (edn-request-format)
        :handler handler 
        :eror-handler error-handler}))
