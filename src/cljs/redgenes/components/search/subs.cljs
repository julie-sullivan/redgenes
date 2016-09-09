(ns redgenes.components.search.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(reg-sub
  :search-term
  (fn [db _]
    (:search-term db)))

(reg-sub
  :quicksearch-selected-index
  (fn [db _]
    (:quicksearch-selected-index db)))