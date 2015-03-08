(ns pasmo-admin-users.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [reagent-forms.core :refer [init-field bind-fields value-of]]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [json-html.core :refer [edn->hiccup]]
              [pasmo-admin-users.users-list :as users-list]
              [pasmo-admin-users.user-create :as user-create]
              [pasmo-admin-users.state :as state])
    (:import goog.History))


(defn home-page []
  [:div [:h2 "Welcome to pasmo-admin-users"]
   [:div [:a {:href "#/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About pasmo-admin-users"]
   [:div [:a {:href "#/"} "go to the home page"]]])

;; -------------------------

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (users-list/fetch-all)
  (session/put! :current-page #'users-list/show-users-list))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/list" []
  (users-list/fetch-all)
  (session/put! :current-page #'users-list/show-users-list))

(secretary/defroute "/create" []
  (session/put! :current-page #'user-create/create-user-form))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn init! []
  (hook-browser-navigation!)
  (reagent/render-component [current-page] (.getElementById js/document "app")))


