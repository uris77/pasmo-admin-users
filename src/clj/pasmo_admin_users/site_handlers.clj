(ns pasmo-admin-users.site-handlers
  (:require [ring.util.response :as response]
            [compojure.core :refer [GET POST ANY defroutes]]
            [compojure.route :refer [not-found resources]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [cemerick.friend :as friend]
            (cemerick.friend [credentials :as creds])
            [hiccup.page :as h]))

(defn credential-fn [token]
  {:identity token :roles #{::user}})

(defroutes site-routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (GET "/login" req
       (friend/authorize #{::user} "You are authorized"))
  (friend/logout (ANY "/logout" request (response/redirect "/")))
  (resources "/")
  (not-found "Not Found"))


