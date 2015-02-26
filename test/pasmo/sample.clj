(ns pasmo.sample
  (:require [pasmo-admin-users.db.users :as users] 
   [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as coll])
  (:use clojure.test))

(deftest simple
  (let [d 1]
    (is (= d 1))))

(deftest it-creates-users
  (users/create-user "uris77@gmail.com" false {:gigi false :maps true})
  (is (not (nil? (users/find-user "uris77@gmail.com")))))

(deftest it-does-not-create-user-with-duplicate-emails
  (users/create-user "user@mail.com" false {})
  (is (nil? (users/create-user "user@mail.com" false {}))))


