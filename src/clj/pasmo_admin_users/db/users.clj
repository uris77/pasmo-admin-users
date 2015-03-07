(ns pasmo-admin-users.db.users
  (:require [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as coll]))


(def conn (mg/connect {:host (env :mongo-host) :port (env :mongo-port)}))
(def db (mg/get-db conn (env :users-db)))
(def users-coll "users")

(defn find-user [email]
  (coll/find-one-as-map db users-coll {:name email}))

(defn create-user [email admin apps]
  (let [user (find-user email)]
    (if (nil? user)
      (coll/insert db users-coll {:name email :admin admin :apps apps}))))

(defn all 
  "List all users."
  []
  (coll/find-maps db users-coll))

(defn add-api-token [email token]
  (let [user (find-user email)]
    (if (not (nil? user))
      (coll/update-by-id db users-coll (:id user) {:api-token token}))))
