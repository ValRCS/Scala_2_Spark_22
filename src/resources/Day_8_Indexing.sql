CREATE TABLE contacts (
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text NOT NULL
);

--By creating index we will speed up queries which use email column
-- as part of the query
CREATE UNIQUE INDEX idx_contacts_email
ON contacts(email);



INSERT INTO contacts(first_name, last_name, email)
VALUES ('Valdis','Saulespurens', 'valdis.s.coding@gmail.com');

INSERT INTO contacts(first_name, last_name, email)
VALUES ('Valdis','Saulespurens', 'valdis.s.coding@prosemind.com');

INSERT INTO contacts(first_name, last_name, email)
VALUES ('Alice','Doe', 'alice.doe@prosemind.com');

SELECT * FROM contacts c2 ;

--Can not insert a new row with the same e-mail 
--due to UNIQUE INDEX requirement for email column
INSERT INTO contacts(first_name, last_name, email)
VALUES ('Valdis','Saules', 'valdis.s.coding@prosemind.com');

SELECT *
FROM contacts c2 
WHERE email = 'valdis.s.coding@gmail.com';

--we can find out how the queries use the index(es) if at all
EXPLAIN QUERY PLAN
SELECT *
FROM contacts c2 
WHERE email = 'valdis.s.coding@gmail.com'; 

SELECT * FROM contacts c2
WHERE first_name = 'Valdis';

EXPLAIN QUERY PLAN
SELECT * FROM contacts c2
WHERE first_name = 'Valdis';
