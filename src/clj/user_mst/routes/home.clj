(ns user-mst.routes.home
  (:require
   [user-mst.layout :as layout]
   [user-mst.db.core :as db]
   [conman.core :as conman]
   [user-mst.middleware :as middleware]
   [ring.util.http-response :as response]
   [struct.core :as st]))

(def user-schema
  [[:id st/required st/string]
   [:pass st/required st/string]
   [:last_name st/required st/string]
   [:first_name st/required st/string]
   [:email st/string]])

(defn validate-user [params]
  (first (st/validate params user-schema)))

(defn get-user [{:keys [params] :as request}]
  (layout/render request
                 "users.html"
                 {:users (db/get-user db/*db* params)
                  :params params}))

(defn disp-form [{:keys [flash params] :as request}]
  (->> (cond flash flash
             (:id params) (first (db/get-user db/*db* {:id (:id params)}))
             :else nil)
       (layout/render request "regist.html")))

(defn regist-user [{:keys [params]}]
  (if-let [errors (validate-user params)]
    (-> (response/found "/regist-user")
        (assoc :flash (assoc params :errors errors)))
    (do
      (conman/with-transaction [db/*db*]
                               (let [target-id (:target-id params)]
                                 (if (empty? target-id)
                                   (db/create-user! db/*db* params)
                                   (db/update-user! db/*db* params))))
      (response/found "/users"))))

(defn delete-user [{:keys [params]}]
  (conman/with-transaction [db/*db*]
                           (db/delete-user! db/*db* params))
  (response/found "/users"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/users" {:get get-user}]
   ["/regist-user" {:get disp-form
                    :post regist-user}]
   ["/delete-user" {:post delete-user}]])

