(ns getboot
  (:require
    [ring.adapter.jetty     :as jetty]
    [ring.middleware.params :refer [wrap-params]]
    [ring.util.response     :refer [resource-response response redirect not-found header]]
    [clojure.data.json :as json]
    [tentacles.repos :as repos]
    [clojure.java.io :as io]
    [clojure.core.memoize :as memo])
  (:gen-class))

(defn -get-latest-release
  []
  (first (repos/releases "boot-clj" "boot")))

(def get-latest-release (memo/ttl -get-latest-release :ttl/threshold 86400000))

(defn get-assets
  ([release]
    (map #(:browser_download_url %) (:assets release)))
  ([]
    (get-assets (get-latest-release))))

(defn get-mapping
  []
  (let [release (get-latest-release)
        assets (get-assets)
        tarball-url (:tarball_url release)
        urls (map #(str "/" (.getName (io/file %))) assets)
        mapping (zipmap urls assets)]
    (assoc mapping "/boot.tar.gz", tarball-url)))

(defn current-version
  "Return the current release version of boot"
  [request]
  (let [release (get-latest-release)
        url (:html_url release)
        version (:name release)
        tag_name (:tag_name release)
        data (json/write-str {:url url :version version :tag-name tag_name})]
     (header (response data) "Content-type" "application/json")))

(defn redirect-to-asset
  "Redirect the user to the assets associated with the latest release"
  [request]
  (let
    [urlmap (get-mapping)
     target (:uri request)
     mapped (get urlmap target)]

     (if (not (nil? mapped))
        (redirect mapped)
        (resource-response target))))

(defn basic-map
  "Basic url mapping"
  [request]
  (case (:uri request)
    "/" (resource-response "index.html")
    "/info" (current-version request)
    (redirect-to-asset request)))

(defn -main
  [& args]
  (jetty/run-jetty basic-map {:port 3001}))
