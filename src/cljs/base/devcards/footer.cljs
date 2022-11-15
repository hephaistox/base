(ns base.devcards.footer
  (:require
   [reagent.core :as r]

   [devcards.core :as dc :refer [defcard]]
   [base.components.footer :as sut]))

(def social-networks
  [{:href "#"
    :sr-name "Facebook"
    :icon :svg/facebook}
   {:href "#"
    :sr-name "Instagram"
    :icon :svg/instagram}
   {:href "#"
    :sr-name "Twitter"
    :icon :svg/twitter}
   {:href "#"
    :sr-name "Github"
    :icon :svg/github}])

(def footer-lists
  [{:title "Solutions"
    :items [{:text "Marketing"
             :href "#"}
            {:text "Analytics"
             :href "#"}
            {:text "Commerce"
             :href "#"}
            {:text "Insights"
             :href "#"}]}
   {:title "Support"
    :items [{:text "Pricing"
             :href "#"}
            {:text "Documentation"
             :href "#"}
            {:text "Guides"
             :href "#"}
            {:text "API Status"
             :href "#"}]}
   {:title "Compagny"
    :items [{:text "About"
             :href "#"}
            {:text "Blog"
             :href "#"}
            {:text "Jobs"
             :href "#"}
            {:text "Press"
             :href "#"}
            {:text "Partners"
             :href "#"}]}
   {:title "Legal"
    :items [{:text "Claim"
             :href "#"}
            {:text "Privacy"
             :href "#"}
            {:text "Terms"
             :href "#"}]}])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defcard footer
  (r/as-element
   [:div
    [:h1 "Show the footer"]
    [sut/component {:social-networks social-networks
                    :footer-lists footer-lists
                    :quotation "Making the world a better place through constructing elegant hierarchies."
                    :badge "https://tailwindui.com/img/logos/mark.svg?color=gray&shade=300",
                    :compagny-name "SAS CAUMOND"
                    :version "2022-1"
                    :title "© 2020 SAS caumond, Inc. All rights reserved!"}]]))
