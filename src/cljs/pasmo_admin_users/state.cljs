(ns pasmo-admin-users.state
  (:require [reagent.core :as reagent :refer [atom]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Application State

(def new-user (atom {:doc {} :errors {}}))

(defn set-value! [id value]
  (swap! new-user assoc :saved? false)
  (swap! new-user assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @new-user [:doc id]))

(defn set-error! [id value]
  (swap! new-user assoc-in [:errors id] value))

(defn get-error [id]
  (get-in @new-user [:errors id]))



