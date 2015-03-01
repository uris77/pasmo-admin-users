(ns pasmo-admin-users.db.users
  (:require [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as coll]))


(def conn (mg/connect {:host (env :mongo-host) :port (env :mongo-port)}))
(def db (mg/get-db conn (env :users-db)))

(defn find-user [email]
  (coll/find-one db "users" {:name email}))

(defn create-user [email admin apps]
  (let [user (find-user email)]
    (if (nil? user)
      (coll/insert db "users" {:name email :admin admin :apps apps}))))

(defn add-api-token [email token]
  (let [user (find-user email)]
    (if (not (nil? user))
      (coll/update-by-id db "users" (:id user) {:api-token token}))))
