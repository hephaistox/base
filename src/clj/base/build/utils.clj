(ns base.build.utils
  "Utility namespace for building apps"
  (:require
   [clojure.data.json :as json]
   [clojure.edn :as edn]
   [clojure.pprint :as pp]
   [clojure.java.shell :refer [sh]]
   [clojure.tools.build.api :as b]
   [clojure.tools.build.util.file :as file]

   [babashka.fs :as fs]))
;;TODO Penser à copier ce code dans build

(defn clean
  "Delete the content of `target-dir` directory"
  [target-dirs]
  (doseq [target-dir (if (seq? target-dirs)
                       target-dirs
                       (vec target-dirs))]
    (println (format "Build - clean step of `%s` directory" target-dir))
    (b/delete {:path target-dir})))

(defn create-version
  "Creates a map with the current build version. The keys in the result are:
  * :year the year as integer
  * :month the month as integer
  * :release is a composite value containing the date and the build version"
  []
  (let [now (new java.util.Date)
        year (.format (java.text.SimpleDateFormat. "yyyy") now)
        month (.format (java.text.SimpleDateFormat. "MM") now)
        release (format "%s.%s.%s" year month (b/git-count-revs nil))]
    {:year year
     :month month
     :release release}))

(defn push-to-clever
  "Commit `target-files` in `target-dir` and push them to `url` "
  [{:keys [target-dir remote-repo-url remote-repo]}]
  (println "Push to clevercloud remote git repo")
  (println "git" "remote" "add" remote-repo
           remote-repo-url
           :dir target-dir)
  (println "git" "push" "--force" "-u" remote-repo "master"
           :dir target-dir)

  (sh "git" "remote" "add" remote-repo
      remote-repo-url
      :dir target-dir)
  (sh "git" "push" "--force" "-u" remote-repo "master"
      :dir target-dir))

(defn push-to-remote-repo
  "Push the target-file.
  * target-dir is where to copy all files to create the local repo to push
  * target-files is the list of filenames to add to the repo, should already be in target-dir
  * url is a git server where target-files will be pushed
  "
  [target-dir target-files url]
  (println "Init git")
  (sh "git" "init"
      :dir target-dir)

  (println "Add target files to git")
  (doseq [target-file target-files]
    (sh "git" "add" target-file
        :dir target-dir))

  (println "Create git commit")
  (sh "git" "commit" "-m" "init"
      :dir target-dir)

  (push-to-clever {:target-dir target-dir
                   :remote-repo-url url
                   :remote-repo "remote"}))

(defn create-clever-jar-json
  "Create a map for a jar deployment in clever cloud"
  [uber-file]
  {:deploy {:jarName uber-file}})
;; (format "{\"deploy\": {\"jarName\": \"%s\"}}" uber-file)

(defn spit-clever-jar-json
  "Spit the content in a jar.json"
  [target-dir content]
  (file/ensure-dir (format "%s/clevercloud"
                           target-dir))
  (spit (format "%s/clevercloud/jar.json"
                target-dir)
        (json/json-str content)))

(defn execute-sentences
  "Execute sentences and show them"
  [sentences target-dir]
  (doseq [sentence sentences]
    (apply println sentence)
    (apply sh (concat sentence
                      [:dir target-dir]))))

(defn deploy-project-and-docker
  "Deploy the docker image to its github and clever cloud"
  [config]
  (println "Creates the clevercloud docker version")
  (let [{:keys [docker-dir deps-edn docker base-commit base-remote-repo dirs files]} config
        {:keys [remote-repo remote-name]} docker
        commit-id (slurp base-commit)
        deps-edn-map (-> (slurp deps-edn)
                         edn/read-string)]
    (println "In directory: " docker-dir)
    (spit deps-edn
          (with-out-str
            (pp/pprint
             (assoc-in deps-edn-map
                       [:deps 'caumond/base :git/sha]
                       commit-id))))
    (fs/delete-tree docker-dir)
    (fs/create-dirs docker-dir)
    (execute-sentences [["git" "init"]
                        ["git" "remote" "add" "origin" base-remote-repo]
                        ["git" "pull" "origin" "master"]]
                       docker-dir)
    (doseq [dir dirs]
      (let [src-dir dir
            target-dir (str docker-dir dir)]
        (println "cp -fr " src-dir " " target-dir)
        (fs/copy-tree src-dir target-dir {:replace-existing true})))
    (doseq [file files]
      (let [src-file file
            dst-file (str docker-dir file)]
        (println "cp -f" src-file " " dst-file)
        (fs/copy src-file dst-file {:replace-existing true})))
    (execute-sentences [["git" "add" "."]
                        ["git" "commit" "-m" "automatic commit"]
                        ["git" "push" "--force-with-lease" "-u" "origin" "master"]
                        ["git" "remote" "add" remote-name remote-repo]
                        ["git" "push" "--force" "-u" remote-name "master"]]
                       docker-dir)
    ;;TODO don't work, think about adding commit to this project, and
    ))
