#!/usr/bin/env boot

(set-env!
  :source-paths #{"src/"}
  :resource-paths #{"static"}
  :dependencies
    '[[ring/ring-core "1.3.2"]
      [org.clojure/clojure "1.6.0"]
      [ring/ring-servlet "1.3.2"]
      [ring/ring-jetty-adapter "1.3.2"]
      [org.clojure/core.memoize "0.5.6"]
      [org.clojure/data.json "0.2.6"]
      [tentacles "0.3.0"]])

(require '[clojure.java.io :as io]
         '[boot.util :as util])

(task-options!
  pom {:project 'latest-boot
       :version"1.0.0"}
  jar {:main 'getboot.core}
  war {:file "getboot.war"}
  aot {:all true})

(deftask webxml
  "Add our local web.xml to the WEB-INF directory of the target"
  []
  (with-pre-wrap fileset
    (let [fileset (add-asset fileset (io/file "./meta"))]
      (util/info "Adding web.xml...\n")
      (commit! fileset))))

(deftask build-war
  "Create a standalone war file"
  []
  (comp 
    (aot) 
    (pom) 
    (uber :as-jars true) 
    (webxml) 
    (war)))

(deftask standalone
  "Create a standalone jar file that can be run via java -jar"
  []
  (comp 
    (aot) 
    (pom) 
    (uber)
    (jar)))
