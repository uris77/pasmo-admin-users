(ns pasmo-admin-users.users-list
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [pasmo-admin-users.state :as state]))

(defn- fetch-all
  "Fetch all users from the server."
  []
  (go (let [resp (<! (http/get "/api/users" {:headers {"accept" "application/json"}}))]
        (reset! state/users (:body resp)))))

(defn remove-user [user]
  (go (let [del-resp (<! (http/delete (str "/api/users/" (:_id user))))]
        (fetch-all))))

(defn user-row [user]
  (letfn [(handle-click! []
            (if (js/confirm (str  "Do you want to remove " (:email user) " ?"))
              (remove-user user)
              (prn "Cancelled removal of " (:email user))))] 
    [:tr
     [:td (:first-name user)]
     [:td (:middle-name user)]
     [:td (:last-name user)]
     [:td (:email user)]
     [:td
      [:button {:class "btn btn-danger btn-sm"
                :on-click handle-click!}
       [:span {:class "glyphicon glyphicon-trash"}]]]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View Component
(defn show-users-list []
  [:table {:class "table table-striped table-bordered"}
   [:thead
    [:tr
     [:th "First Name"]
     [:th "Middle Name"]
     [:th "Last Name"]
     [:th "Email"]
     [:th ]]]
   [:tbody
    (for [user @state/users]
      (do (user-row user)))]])

