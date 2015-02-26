(ns pasmo-admin-users.auth-config
  (:require [environ.core :refer [env]]
            [friend-oauth2.util :refer [format-config-uri]]))

(def oauth-domain  (env :oauth-domain))

(def oauth-callback (env :oauth-callback))

(def client-id (env :client-id))

(def client-secret (env :client-secret))

(def auth-url (env :auth-url))

(def token-url (env :token-url))

#_(def csrf-token (env :csrf-token))

(def callback-url (env :callback-url))

(def client-config
  {:client-id client-id
   :client-secret client-secret
   :callback  {:domain oauth-domain :path oauth-callback}})


(def uri-config
  {:authentication-uri {:url auth-url
                        :query {:client_id (:client-id client-config)
                                :redirect_uri (format-config-uri client-config)
                                :response_type "code"
                                :scope "email"}}
  :access-token-uri {:url token-url
                     :query {:client_id (:client-id client-config)
                             :client_secret (:client-secret client-config)
                             :grant_type "authorization_code"
                             :redirect_uri (format-config-uri client-config)}}})
