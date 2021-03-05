(ns user-mst.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [user-mst.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[user-mst started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[user-mst has shut down successfully]=-"))
   :middleware wrap-dev})
