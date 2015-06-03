(ns getboot.servlet
  (:use [ring.util.servlet :only (defservice)])
  (:require [getboot.core :as core]
            [clojure.string :as string])
  (:gen-class :extends javax.servlet.http.HttpServlet))

(defn wrapper
  "Alter the request as appropriate so the handler functions without modification"
  [func]
  (fn [request]
    (let [context (:servlet-context-path request)
          prefix (re-pattern (str "^" context))
          uri (string/replace (:uri request) prefix "")
          request (assoc request :uri uri)]
          (func request))))

(defservice (wrapper core/basic-map))

