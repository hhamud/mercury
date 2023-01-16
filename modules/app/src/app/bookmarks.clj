(ns app.bookmarks
  (:require
            [clojure.data.json :refer [read-str]]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [honeysql]
            [pocket.add :refer [pocket-add]]))


(def firefox-database "./Library/Application Support/Firefox/Profiles/4b7u0o54.default-release-1/places.sqlite")
;; create generic function that finds the database

(defn add-to-pocket
  "Pulls all the bookmarks and adds it to the user's personal pocket account."
  )
