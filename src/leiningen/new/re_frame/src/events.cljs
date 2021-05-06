(ns {{ns-name}}.events
  (:require
   [re-frame.core :as re-frame]{{#re-pressed?}}
   [re-pressed.core :as rp]{{/re-pressed?}}
   [{{ns-name}}.db :as db]{{#10x?}}
   [day8.re-frame.tracing :refer-macros [fn-traced]]{{/10x?}}{{#crux?}}
   [wkok.re-frame-crux]{{/crux?}}))

(re-frame/reg-event-db
 ::initialize-db
 ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [_ _]
   db/default-db))
{{#routes?}}

(re-frame/reg-event-fx
  ::navigate
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel){{#re-pressed?}}
    :dispatch [::rp/set-keydown-rules
               {:event-keys [[[::set-re-pressed-example "Hello, world!"]
                              [{:keyCode 72} ;; h
                               {:keyCode 69} ;; e
                               {:keyCode 76} ;; l
                               {:keyCode 76} ;; l
                               {:keyCode 79} ;; o
                               ]]]
                :clear-keys
                [[{:keyCode 27} ;; escape
                  ]]}]{{/re-pressed?}}}))
{{/routes?}}
{{#re-pressed?}}

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))
{{/re-pressed?}}

{{#crux?}}
(re-frame/reg-event-db
 ::something-changed
 (fn [db [_ something]]
   (assoc db :something something)))

(re-frame/reg-event-fx
 ::put-something-in-crux
 (fn [{:keys [db]} [_ _]]
   {:crux/put {:doc        {:something/value (:something db)}
               :on-success [::put-succeeded]
               :on-failure #(js/alert (str "Failed!\n\nIs Crux running?\n\nError: " %))}}))

(re-frame/reg-event-db
 ::put-succeeded
 (fn [db [_ _]]
   (update db :things conj (:something db))))

(re-frame/reg-event-fx
 ::query-all-the-things
 (fn [_ [_ _]]
   {:crux/query {:query      '{:find  [?value]
                               :keys  [value]
                               :where [[?e :something/value ?value]]}
                 :on-success [::query-succeeded]
                 :on-failure #(js/console.log (str "Failed!\n\nIs Crux running?\n\nError: " %))}}))

(re-frame/reg-event-db
 ::query-succeeded
 (fn [db [_ things]]
   (assoc db :things (map :value things))))
{{/crux?}}