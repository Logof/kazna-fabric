(ns build
  (:require [clojure.tools.build.api :as b]))

(def target-dir "target")
(def class-dir (str target-dir "/classes"))
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file "chaincode.jar")

; clj -T:build clean
(defn clean [_]
  (b/delete {:path target-dir}))

; clj -T:build uber
(defn uber [_]
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file (str target-dir "/" uber-file)
           :basis basis
           :main 'otr.demo}))
