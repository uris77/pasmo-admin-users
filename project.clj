(defproject pasmo-admin-users "0.1.0-SNAPSHOT"
  :description "User Management for PASMO apps."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cljsjs/react "0.12.2-5"]
                 [reagent "0.5.0-alpha3"]
                 [reagent-forms "0.4.3"]
                 [reagent-utils "0.1.2"]
                 [secretary "1.2.1"]
                 [com.novemberain/monger "2.0.0"]
                 [org.clojure/clojurescript "0.0-2850" :scope "provided"]
                 [ring "1.3.2" :exclusions [org.eclipse.jetty/jetty-http org.eclipse.jetty/jetty-continuation]]
                 [compojure "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [ring/ring-codec "1.0.0"]
                 [ring/ring-json "0.1.2"]
                 [hiccup "1.0.5"]
                 [prone "0.8.0"]
                 [selmer "0.8.0"]
                 [environ "1.0.0"]
                 [http-kit "2.1.16"]
                 [org.clojure/core.cache "0.6.4"]
                 [cheshire "5.3.1"]
                 [clj-http "1.0.1"]
                 [com.cemerick/friend "0.2.0" :exclusions [ring/ring-core org.clojure/core.cache org.apache.httpcomponents/httpclient]]
                 [friend-oauth2 "0.1.3" :exclusions [commons-logging org.apache.httpcomponents/httpcore]]
                 [lib-noir "0.9.5"]]

  :plugins [
            [lein-cljsbuild "1.0.4"]
            [lein-environ "1.0.0"]
            [lein-ring "0.9.1"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler pasmo-admin-users.handler/site-and-api
         :uberwar-name "pasmo-admin-users.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "pasmo-admin-users.jar"

  :main pasmo-admin-users.server

  :clean-targets ^{:protect false} ["resources/public/js"]

  :minify-assets
  {:assets
    {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        ;;:externs       ["react/externs/react.js"]
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev-common {:repl-options {:init-ns pasmo-admin-users.handler
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.3.2"]
                                  [leiningen "2.5.1"]
                                  [figwheel "0.2.5-SNAPSHOT"]
                                  [weasel "0.6.0-SNAPSHOT"]
                                  [com.cemerick/piggieback "0.1.6-SNAPSHOT"]
                                  [pjstadig/humane-test-output "0.6.0"]]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.2.3-SNAPSHOT"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3000
                              :css-dirs ["resources/public/css"]
                              :ring-handler pasmo-admin-users.handler/site-and-api}

                   :env {:dev? true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "pasmo-admin-users.dev"
                                                         :source-map true}}}}}
             :dev-env-vars {}
             :dev [:dev-common :dev-env-vars]

             :test-common {
                           :dependencies [[leiningen "2.5.1"]]
                           :env {:test? true}}
             :test-env-vars {}
             :test [:test-common :test-env-vars]

             :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                             {:source-paths ["env/prod/cljs"]
                                              :compiler
                                              {:optimizations :advanced
                                               :pretty-print false}}}}}

             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}
                          :cljsbuild {:builds {:app {:compiler {:main "pasmo-admin-users.prod"}}}}
                          }})
