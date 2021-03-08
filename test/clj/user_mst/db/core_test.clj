(ns user-mst.db.core-test
  (:require
   [user-mst.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [next.jdbc :as jdbc]
   [user-mst.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'user-mst.config/env
     #'user-mst.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
    (is (= 1 (db/create-user!
              t-conn
              {:id         "1"
               :first_name "Sam"
               :last_name  "Smith"
               :email      "sam.smith@example.com"
               :pass       "pass"}
              {})))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (first (db/get-user t-conn {:id "1"} {}))))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (first (db/get-user t-conn {:first_name "am"} {}))))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (first (db/get-user t-conn {:last_name "it"} {}))))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (first (db/get-user t-conn {:email "example"} {}))))
    (is (= 1 (db/update-user!
               t-conn
               {:id         "1"
                :first_name "Bob"
                :last_name  "Smith"
                :email      "bob.smith@example.com"
                :pass       "pass2"
                :target-id  "1"}
               {})))
    (is (= {:id         "1"
            :first_name "Bob"
            :last_name  "Smith"
            :email      "bob.smith@example.com"
            :pass       "pass2"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (first (db/get-user t-conn {:email "bob"} {}))))
    (is (= 1 (db/delete-user!
               t-conn
               {:id "1"}
               {})))))
