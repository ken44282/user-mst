(ns user-mst.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[user-mst started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[user-mst has shut down successfully]=-"))
   :middleware identity})
