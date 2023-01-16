(ns app.download
  (:require [pocket.retrieve :refer [get-articles]]
            [clojure.data.json :refer [read-str]]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))


(def home (System/getProperty "user.home"))
(def base-dir (str home "/Documents/downloaded_articles/"))
(def html-dir (str base-dir "html_files/"))
(def markdown-dir (str base-dir "markdown_files/"))

(defn parse-title
  "Makes unix friendly titles."
  [title]
  (str/replace (str/replace title #"[^a-zA-Z0-9_.-]+" "_") #"^_|_$" ""))

(defn create-hash-map
  "helper function for creating a hashmap out of the pocket api."
  [acc item]
  (assoc acc (parse-title (get item "resolved_title")) (get item "resolved_url")))

(defn get-article-links
  "Fetch all article links stored in Pocket."
  []
  (let [resp  (-> (get-articles)
                  read-str
                  (get "list")
                  vals)
        body (reduce create-hash-map  (hash-map) resp)]
    body))

(defn check-file
  "Checks to see if the file already exists in dir"
  [name dir]
  (let [file (io/File. dir (str name ".html"))]
    (.exists file)))

(defn download-article
  "Download any html article."
  [name link path-to-folder]
  (if-not (check-file name html-dir)

    (try
      (do
        (spit (str path-to-folder (str name ".html")) (slurp link))
        (println (str "Successfully downloaded: " name)))
      (catch Exception e
        (println (str "An Exception has occured for: " name " with error: " e)))))
  (println (str "Skipping " name, " .........")))

(defn html-to-markdown
  "Converts html file to markdown."
  [html-file output-file]
  (let [command (str "pandoc " html-file " -f html -t markdown -o " output-file)]
    (try
      (do
        (sh command)
        (println (str "Converted : " html-file, "to markdown")))
      (catch Exception e
        (println (str "Failed to convert file: " html-file))))))

(defn pocket-download
  "Downloads pocket articles concurrently and converts it all into markdown."
  []
  (let  [download
         (map (fn [item] (let [name (first item)
                               link (second item)]
                           (future (download-article name link html-dir)))) (get-article-links))]
    (doall (map deref download)))
  (println "DONE"))
