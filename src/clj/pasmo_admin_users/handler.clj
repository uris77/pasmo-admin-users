(ns pasmo-admin-users.handler
  (:require [compojure.core :refer [GET defroutes routes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.util.codec :as codec]
            [ring.util.response :as resp]
            [selmer.parser :refer [render-file]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [friend-oauth2.workflow :as oauth2]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [pasmo-admin-users.site-handlers :as site-handlers]
            [pasmo-admin-users.site-handlers :refer [credential-fn]]
            [hiccup.page :as h]
            [pasmo-admin-users.auth-config :refer [client-config uri-config]]))

(def site-app
  (-> site-handlers/site-routes
      (friend/authenticate {:allow-annon? true
                            :login-uri "/login"
                            :default-landing-uri "/"
                            :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                        resp/response
                                        (resp/status 401))
                            :workflows [(oauth2/workflow
                                         {:client-config client-config
                                          :uri-config uri-config
                                          :credential-fn credential-fn
                                          })]})
      handler/site))

(def site-and-api
  (wrap-defaults (routes site-app) (assoc-in site-defaults [:security :anti-forgery] false)))


