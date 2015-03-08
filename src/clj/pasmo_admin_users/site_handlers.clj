(ns pasmo-admin-users.site-handlers
  (:require [ring.util.response :as response]
            [compojure.core :refer [GET POST ANY defroutes]]
            [compojure.route :refer [not-found resources]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [cemerick.friend :as friend]
            (cemerick.friend [credentials :as creds])
            [hiccup.page :as h]
            [pasmo-admin-users.auth-config :as auth]
            [noir.response :refer [edn]]))

(defn users-handler [req]
  (render-file "templates/main.html" {:dev {env :dev?}}))

(defroutes site-routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (GET "/login" req
       (friend/authorize #{:pasmo-admin-users.auth-config/user} users-handler))
  (GET "/users" req
       (friend/authorize #{::auth/user} users-handler))
  (friend/logout (ANY "/logout" request (response/redirect "/")))
  (resources "/")
  (not-found "Not Found"))


