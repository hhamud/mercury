(ns mercury.twitter
  (:require [net.cgrand.enlive-html :as enlive]
            [org.httpkit.client :as http]
            [etaoin.api :as e]))

;; login to personel twitter
;; enter into bookmarks sections
;; slurp a link from each post
;; Start WebDriver for Firefox
;; ;; a Firefox window should appear
(def driver (e/with-firefox {:headless true}))

;; navigate to twitter
(e/go driver "https://www.twitter.com")

(def credentials
  {:username ""
   :password ""})

(defn scrape-twitter-bookmarks
  "Login into twitter using credentials."
  []
  ((e/with-firefox {:headless true} driver
     e/go driver "https://www.twitter.com")))

;; data-testid="login"
