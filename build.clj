(ns build
  (:refer-clojure :exclude [test])
  (:require #_[org.corfield.build :as bb]
            [clojure.tools.build.api :as b])) ; POD added

;;; (def lib 'net.clojars.wkok/openai-clojure)
;;; (def version "0.21.2")
;;;
;;; (defn test "Run the tests." [opts]
;;;   (bb/run-tests opts))
;;;
;;; (defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
;;;   (-> opts
;;;       (assoc :lib lib :version version :tag version)
;;;       (bb/run-tests)
;;;       (bb/clean)
;;;       (bb/jar)))
;;;
;;; (defn install "Install the JAR locally." [opts]
;;;   (-> opts
;;;       (assoc :lib lib :version version)
;;;       (bb/install)))
;;;
;;; (defn deploy "Deploy the JAR to Clojars." [opts]
;;;   (-> opts
;;;       (assoc :lib lib :version version)
;;;      (bb/deploy)))

;;; ============POD All my stuff below =====================================

;;; See https://clojure.org/guides/tools_build
;;;   clj -T:build clean
;;;   clj -T:build jar
;;;   clj -T:build install

(def lib 'com.github.pdenno/openai-clojure)
(def version (format "0.21.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (delay (b/create-basis {:project "deps.edn"})))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

;;; This does not work!
(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :resource-dirs ["resources"] ; Though there is not explicit resources directory, these land in classes, which works for wkok.
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"] :target-dir class-dir})
  (b/jar {:class-dir class-dir :jar-file jar-file})) ; This is what doesn't work: though the files openai.yaml etc are in classes, they don't make it into the .jar.

;;; :basis - required, used for :mvn/local-repo
(defn install [_]
  (let [opts {:lib lib :basis @basis :jar-file jar-file :class-dir class-dir :version version}]
    (b/install opts)))
