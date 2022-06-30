--https://www.sqlite.org/lang_createindex.html --official
--more detailed
-- https://www.sqlitetutorial.net/sqlite-index/
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

SELECT * FROM tracks
ORDER BY TrackId DESC;

SELECT * FROM tracks t2 
WHERE name = 'Sing Joyfully';

SELECT COUNT(*) FROM tracks t2 ;

SELECT COUNT(*) FROM playlist_track pt ;
--ON such tiny tables we will not see that much of a difference
--however on multiple subqueries involving various indexes 
--speedup can be considerable

EXPLAIN QUERY PLAN 
SELECT * FROM tracks t2 
WHERE name = 'Sing Joyfully';

--now inserting a new index using UNIQUE constraint might produce error
--if track names are same
CREATE INDEX idx_track_name
ON tracks(name);

--now our queries using name matches should be faster
EXPLAIN QUERY PLAN 
SELECT * FROM tracks t2 
WHERE name = 'Sing Joyfully';

--so we can check what indexes we have on some table
PRAGMA index_list('tracks');

PRAGMA index_list('contacts');

--let's get all indexes in our database
SELECT
	type,
	name,
	tbl_name, 
	sql
FROM
	sqlite_master
WHERE
	type='index';

--if we decide we do not need an index we can drop it
DROP INDEX IF EXISTS --if exists is optional
idx_tracks_name;

DROP INDEX -- will be error if index does not exist
idx_tracks_name;

SELECT * FROM tracks t2 
WHERE name = 'Sing Joyfully';

--let's see if we have duplicate track names
SELECT name, COUNT(trackid) counts
FROM tracks t 
GROUP BY name
ORDER BY counts DESC;

--THUS unique index will not work on column which has duplicates
CREATE UNIQUE INDEX idx_tracks_name
ON tracks(name);

--so we would create a regular index
CREATE INDEX idx_tracks_name
ON tracks(name);

EXPLAIN QUERY PLAN 
SELECT * FROM tracks t2 
WHERE name = 'Sing Joyfully';

--we might want to create an index bases on expressions  in some table columns
--https://www.sqlitetutorial.net/sqlite-index-expression/

SELECT * FROM customers;

SELECT customerid,
	company
FROM customers c 
WHERE length(company) > 10
ORDER BY length(company) DESC;

EXPLAIN QUERY PLAN 
SELECT customerid,
	company
FROM customers c 
WHERE length(company) > 10
ORDER BY length(company) DESC;

--so we want to create an index based on expression LENGTH(company)
CREATE INDEX idx_length_company_customers
ON  customers(LENGTH(company));

EXPLAIN QUERY PLAN 
SELECT customerid,
	company
FROM customers c 
WHERE length(company) > 10
ORDER BY length(company) DESC;

SELECT customerid,
	company
FROM customers c 
WHERE length(company) > 10
ORDER BY length(company) DESC;

DROP INDEX IF EXISTS --if exists is optional
idx_length_company_customers;

--expression based index will work ONLY if the expression is exactly the same
CREATE INDEX idx_invoice_line_amount
ON invoice_items(unitprice*quantity);

--so without exact match in WHERE (or ORDER BY) the index will not apply
EXPLAIN QUERY PLAN
SELECT InvoiceLineId ,
	InvoiceId ,
	UnitPrice * Quantity 
FROM invoice_items ii 
WHERE Quantity * UnitPrice > 9000;

--with column expression in correct order the index will work!
EXPLAIN QUERY PLAN
SELECT InvoiceLineId ,
	InvoiceId ,
	UnitPrice * Quantity 
FROM invoice_items ii 
WHERE UnitPrice * Quantity > 9000;




