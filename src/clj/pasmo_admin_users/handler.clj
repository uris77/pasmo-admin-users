(ns pasmo-admin-users.handler
  (:require [compojure.core :refer [GET defroutes routes]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
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
            [pasmo-admin-users.api-handlers :as api-handlers]
            [hiccup.page :as h]
            [cheshire.core :refer :all]
            [pasmo-admin-users.auth-config :refer [client-config uri-config credential-fn]]))

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

(defn api-app [] 
  (cheshire.generate/add-encoder org.bson.types.ObjectId cheshire.generate/encode-str)
  (->> api-handlers/api-routes
       (handler/api)
       (wrap-json-body)
       (wrap-json-response)))

(def site-and-api
  (wrap-defaults (routes (api-app) site-app) (assoc-in site-defaults [:security :anti-forgery] false)))


