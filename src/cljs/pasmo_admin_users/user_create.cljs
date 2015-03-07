(ns pasmo-admin-users.user-create
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.react :as react]
            [pasmo-admin-users.state :as state]))

(defn text-input 
  "A Text Input."
  [id label]
  [:div {:class "form-group"}
   [:label {:class "col-xs-2 control-label"} label]
   [:div {:class "col-xs-4"}
    [:input {:class "form-control"
             :id id
             :on-change #(state/set-value! id (-> % .-target .-value))}]]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Form Validation
(defn- validate-email! []
  (if (empty? (state/get-value :email))
    (state/set-error! :email "Email is empty!")))

(defn- validate-first-name! []
  (if (empty? (state/get-value :first-name))
    (state/set-error! :first-name "First Name is empty!")))

(defn- validate-last-name! []
  (if (empty? (state/get-value :last-name))
    (state/set-error! :last-name "Last Name is empty!")))

(defn validate-form! []
  (let [new-user state/new-user]
    (swap! new-user assoc-in [:errors] {})
    (validate-first-name!)
    (validate-last-name!)
    (validate-email!)))

(defn submit-create-form 
  "Submit the form for creating a new user. The fields are validated before
  they are submitted."
  []
  (let [new-user state/new-user]
    (validate-form!)
    (if (empty? (get-in @new-user [:errors]))
      (swap! new-user assoc [:saved? true]))
    false))

(defn- error-messages [new-user-state]
  (let [errors (get-in @new-user-state [:errors])]
    (->> errors
         (map (fn [it] (last it))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Visual components

(defn- list-errors [error]
  [:li error])

(defn user-form-errors 
  "Displays validation errors."
  [new-user-state]
  (let [new-user new-user-state]
    (if (not (empty? (get-in @new-user [:errors])))
      [:div {:id "errors" :class "form-group"} 
       [:ul {:class "alert alert-danger col-xs-offset-2 col-xs-4" 
             :style {:list-style-type "none"}}
        (for [msg (error-messages new-user)]
          (do [list-errors msg]))]])))

(defn create-user-form 
  "The visual component for creating a user."
  []
  (let [new-user state/new-user
        errors (get-in @new-user [:errors :email])]
    [:div {:class "pure-form pure-form-aligned"}
     [:form {:class "form-horizontal"}
      (user-form-errors new-user)
      (text-input :first-name "First Name")
      (text-input :middle-name "Middle Name")
      (text-input :last-name "Last Name")
      (text-input :email "Email")
      [:div {:class "form-group"}
       [:div {:class "col-xs-offset-2 col-xs-10"}
        [:button {:class "btn btn-primary btn-lg col-xs-2"
                 :on-click submit-create-form} "Save"]]]]]))

