(ns redgenes.sections.results.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as reagent]
            [redgenes.components.table :as table]
            [redgenes.sections.results.events]
            [redgenes.sections.results.subs]
            [redgenes.components.enrichment.views :as enrichment]
            [redgenes.components.bootstrap :refer [popover tooltip]]
            [clojure.string :refer [split]]
            [oops.core :refer [oget]]
            [accountant.core :as accountant]
            [json-html.core :as json-html]
            [im-tables.views.core :as tables]))

(defn adjust-str-to-length [length string]
  (if (< length (count string)) (str (clojure.string/join (take (- length 3) string)) "...") string))

(defn breadcrumb []
  (let [history       (subscribe [:results/history])
        history-index (subscribe [:results/history-index])]
    (fn []
      [:div.breadcrumb-container
       [:i.fa.fa-clock-o]
       (into [:ul.breadcrumb.inline]
             (map-indexed
               (fn [idx {{title :title} :value}]
                 (let [adjusted-title (if (not= idx @history-index) (adjust-str-to-length 20 title) title)]
                   [:li {:class (if (= @history-index idx) "active")}
                    [tooltip
                     [:a
                      {:data-placement "bottom"
                       :title          title
                       :on-click       (fn [x] (dispatch [:results/load-from-history idx]))} adjusted-title]]])) @history))])))

(defn no-results []
  [:div "Hmmm. There are no results. How did this happen? Whoopsie! "
   [:a {:on-click #(accountant/navigate! "/")} "There's no place like home."]])

(defn main []
  (let [are-there-results? (subscribe [:results/are-there-results?])]
    (fn []
      (if @are-there-results?
        ;;show results
        [:div.container.results
         [breadcrumb]
         [:div.results-and-enrichment
          [:div.col-md-8.col-sm-12.panel
             [tables/main [:results :fortable]]]
          [:div.col-md-4.col-sm-12
           [enrichment/enrich]
           ]]]
        ;;oh noes, somehow we made it here with noresults. Fail elegantly, not just console errors.
        [no-results]
        )
)))
