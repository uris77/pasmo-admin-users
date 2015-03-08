(ns pasmo-admin-users.db.users
  (:import org.bson.types.ObjectId)
  (:require [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as coll]))


(def conn (mg/connect {:host (env :mongo-host) :port (env :mongo-port)}))
(def db (mg/get-db conn (env :users-db)))
(def users-coll "users")

(defn find-user [email]
  (coll/find-one-as-map db users-coll {:name email}))

;; {:user :first-name :errors {:email :first-name :last-name}}
(defn- validate-field [user-map field]
  (if (empty? (get user-map field))
    {field (str field " can not be empty!")}))

(defn rev-merge [k m]
  (merge m k))

(defn validate-user [user]
  (let [errors {}]
    (->> errors
         (rev-merge (validate-field user :first-name))
         (rev-merge (validate-field user :last-name))
         (rev-merge (validate-field user :email)))))

(defn create-user [user-map]
  (let [user (find-user (:email user-map))
        errors (validate-user user-map)]
    (if (or (empty? errors) (nil? user))
      (coll/insert-and-return db users-coll user-map)
      errors)))

(defn all 
  "List all users."
  []
  (coll/find-maps db users-coll))

(defn add-api-token [email token]
  (let [user (find-user email)]
    (if (not (nil? user))
      (coll/update-by-id db users-coll (:id user) {:api-token token}))))


(defn remove
  [id]
  (let [oid (ObjectId. id)] 
    (coll/remove-by-id db users-coll oid)))
