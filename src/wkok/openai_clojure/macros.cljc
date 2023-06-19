(ns wkok.openai-clojure.macros
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [martian.yaml :as yaml]))

(defmacro inline-resource-json [resource-path]
  (-> resource-path
      io/resource
      slurp
      json/decode))

(defmacro inline-resource-yaml [resource-path]
  (-> resource-path
      io/resource
      slurp
      yaml/yaml->edn))
