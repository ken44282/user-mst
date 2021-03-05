-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET id = :id, pass = :pass, first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :target-id

-- :name get-user :? :*
-- :doc retrieves a user record given the id
/* :require [clojure.string :as string] */
SELECT * FROM users
/*~
(let [condition
     (->>
         (vector
           (when (not (empty? (:id params))) "id = :id")
           (when (not (empty? (:first_name params))) "first_name like '%' || :first_name || '%'")
           (when (not (empty? (:last_name params))) "last_name like '%' || :last_name || '%'")
           (when (not (empty? (:email params))) "email like '%' || :email || '%'"))
         (remove nil?))]
     (if (empty? condition) nil (str " WHERE " (string/join " AND " condition))))
~*/
-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id
