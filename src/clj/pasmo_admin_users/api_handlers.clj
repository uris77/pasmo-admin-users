(ns pasmo-admin-users.api-handlers
  (:require [ring.util.response :as response]
            [compojure.core :refer [DELETE GET POST ANY defroutes]]
            [pasmo-admin-users.db.users :as users-db]
            [cheshire.core :as cheshire]
            [noir.response :refer [edn]]))

(defn with-keywords [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))

(defn gen [o]
  (cheshire.core/generate-string o {:pretty true}))

(defn create-user-handler [req]
  (let [user-map (with-keywords (:body req))
        created-user (users-db/create-user user-map)
        resp {:body created-user}]
    (if (nil? (:_id created-user))
      (merge resp {:status 500})
      resp)))

(defn users-list-handler [req]
  {:headers {"Content-Type" "application/json"}
   :body (gen (users-db/all))})

(defn delete-user! [id]
  (users-db/remove id)
  {:status 200})

(defroutes api-routes
  (GET "/api/users" req users-list-handler)
  (POST "/api/users" req create-user-handler)
  (DELETE "/api/users/:id" [id] (delete-user! id)))

