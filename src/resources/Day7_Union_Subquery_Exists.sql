SELECT * FROM employees e ;
SELECT *,'blank value' someColumn FROM customers c ;

--https://www.sqlitetutorial.net/sqlite-union/
SELECT FirstName, LastName, 'employee' AS category
FROM employees e
UNION
SELECT FirstName, LastName, 'customer'
FROM customers c ;

SELECT FirstName, LastName, 'employee' AS category
FROM employees e
UNION
SELECT FirstName, LastName, 'customer'
FROM customers c 
ORDER BY LastName ASC;

--so we can mix and match different columsn from the SELECTs
--as long as data types are compatible
--here we mix Fax from employees with Phone from customers
SELECT FirstName, LastName, Fax, 'employee' AS category
FROM employees e
UNION
SELECT FirstName, LastName, Phone, 'customer'
FROM customers c 
ORDER BY LastName ASC;

SELECT * FROM employees e ;

SELECT * FROM employees e 
WHERE LastName LIKE 'P%';

SELECT * FROM employees e 
WHERE FirstName LIKE 'M%';

SELECT * FROM employees e 
WHERE LastName LIKE 'P%'
UNION
SELECT * FROM employees e 
WHERE FirstName LIKE 'M%';

SELECT * FROM employees e 
WHERE LastName LIKE 'P%'
UNION ALL --so no checking for duplicate rows
SELECT * FROM employees e 
WHERE FirstName LIKE 'M%';

--https://www.sqlitetutorial.net/sqlite-except/
SELECT * FROM artists a
ORDER BY ArtistId DESC;

--so we find all artists ID who have no album in the albums table
SELECT ArtistID FROM artists a 
EXCEPT
SELECT ArtistId FROM albums a2 ;

--so with intersect we get
--distinct rows that are in BOTH queries
-- https://www.sqlitetutorial.net/sqlite-intersect/
--so here we get all artists with an album release
SELECT ArtistID FROM artists a 
INTERSECT
SELECT ArtistId FROM albums a2 ;

SELECT * FROM invoices i ;

--we could get all customer ids just from invoices
SELECT DISTINCT customerid FROM invoices i;

--same can be done with intersect
--intersect is not strictly needed here
--but we could use it for more complex selects
SELECT customerid
FROM customers c 
INTERSECT
SELECT customerid 
FROM invoices i 
ORDER BY customerid DESC;

--so subqueries are nested SELECT statements
--inside another statement
--https://www.sqlitetutorial.net/sqlite-subquery/
--we use (SELECT .... FROM ... ) for subqueries

SELECT * FROM tracks t ;
SELECT * FROM albums a ;

SELECT trackid, name, albumid
FROM tracks;
--we could join with album and use WHERE then
--or we could use a subquery
SELECT albumid FROM albums a2 
WHERE title = 'Jagged Little Pill';

SELECT trackid, name, albumid,Milliseconds/1000 seconds 
FROM tracks
WHERE albumid = (
	SELECT albumid 
	FROM albums a2 
	WHERE title = 'Jagged Little Pill'
)
ORDER BY seconds DESC;

SELECT trackid, name, albumid,Milliseconds/1000 seconds 
FROM tracks
WHERE albumid = (
	SELECT albumid 
	FROM albums a2 
	WHERE title LIKE 'J%'
);

SELECT albumid
FROM albums a2 
WHERE title LIKE 'J%';

--with = we got match on first row value in subquery
--so for possible multiple matches we will use IN
SELECT trackid, name, albumid,Milliseconds/1000 seconds 
FROM tracks
WHERE albumid IN (
	SELECT albumid
	FROM albums a2 
	WHERE title LIKE 'J%'
);

--you would use exists for checking subquery that returns anything
--https://www.sqlitetutorial.net/sqlite-exists/
-- we will find customers that have invoices

SELECT * 
FROM customers c ;
--it is typical to use 1 as non null value
SELECT 1 FROM Invoices;

SELECT * FROM customers c 
WHERE EXISTS (
	SELECT 1
	FROM invoices i 
	WHERE
	i.CustomerId = c.CustomerId 
);

SELECT FirstName, LastName, customerid FROM customers c
WHERE EXISTS (
	SELECT 1
	FROM invoices i 
	WHERE
	i.CustomerId = c.CustomerId 
)
ORDER BY LastName, FirstName;

--TODO 1
--query for all tracks that appear on any playlist 
--(playlist_track)
--order by track name
SELECT COUNT(*) FROM tracks t
WHERE EXISTS (
		SELECT '1' FROM playlist_track pt
		WHERE pt.TrackId = t.TrackId
		)
ORDER BY Name ASC;

SELECT COUNT(*) FROM tracks t; -- 3506 total tracks

SELECT '1' FROM playlist_track pt;
WHERE pt.TrackId = t.TrackId;

SELECT TrackId, Name FROM tracks t 
WHERE EXISTS (
	SELECT TrackId FROM tracks t
	INTERSECT
	SELECT TrackId FROM playlist_track pt)
ORDER BY Name;

SELECT TrackId, name
FROM tracks t
WHERE TrackId IN (
	SELECT TrackId
	FROM playlist_track pt)
ORDER BY Name;

--TODO 2
--query for all tracks that have been bought
SELECT * FROM tracks t
WHERE EXISTS (
		SELECT '1' FROM invoice_items ii 
		WHERE ii.TrackId = t.TrackId);

--CASE expression

SELECT DISTINCT country from customers c 
ORDER BY country;

--SIMPLE CASE expression
SELECT customerid,
       firstname,
       lastname,
       CASE country --column against which we will match
           WHEN 'USA' 
               THEN 'American' 
           WHEN 'Australia'
           		THEN 'Australian'
           ELSE 'Other' 
       END CustomerGroup -- CustomerGroup is the new column name
FROM 
    customers
ORDER BY 
    LastName,
    FirstName;
   
--SEARCHED CASE expression lets you use any form of comparison

SELECT
	trackid,
	name,
	milliseconds/1000, 
	CASE
		WHEN milliseconds < 60000 THEN
			'short'
		WHEN milliseconds > 60000 AND milliseconds < 300000 THEN 'medium'
		ELSE
			'long'
		END songType -- category is the new column name
FROM
	tracks;

--I could save results as a VIEW to simply further queries
CREATE VIEW v_customerCountries
AS
SELECT customerid,
       firstname,
       lastname,
       CASE country --column against which we will match
           WHEN 'USA' 
               THEN 'American' 
           WHEN 'Australia'
           		THEN 'Australian'
           ELSE 'Other' 
       END CustomerGroup -- CustomerGroup is the new column name
FROM 
    customers
ORDER BY 
    LastName,
    FirstName;
   
SELECT CustomerGroup,COUNT(CustomerId) FROM v_customerCountries vcc 
GROUP BY CustomerGroup ;

--WITH CTE - common table expressions
--we can create temporary views on the fly
--https://www.sqlite.org/lang_with.html

WITH c_customerCountries
AS ( SELECT customerid,
       firstname,
       lastname,
       CASE country --column against which we will match
           WHEN 'USA' 
               THEN 'American' 
           WHEN 'Australia'
           		THEN 'Australian'
           ELSE 'Other' 
       END CustomerGroup -- CustomerGroup is the new column name
FROM 
    customers
ORDER BY 
    LastName,
    FirstName
) SELECT * FROM c_customerCountries -- so I can use it like a regular table
LIMIT 15;

-- https://www.sqlitetutorial.net/sqlite-transaction/
--By default, SQLite operates in auto-commit mode. 
-- It means that for each command, SQLite starts, processes, and commits the transaction automatically.

CREATE TABLE accounts ( 
	account_no INTEGER NOT NULL, 
	balance DECIMAL NOT NULL DEFAULT 0,
	PRIMARY KEY(account_no),
        CHECK(balance >= 0)
);

CREATE TABLE account_changes (
	change_no INT NOT NULL PRIMARY KEY,
	account_no INTEGER NOT NULL, 
	flag TEXT NOT NULL, 
	amount DECIMAL NOT NULL, 
	changed_at TEXT NOT NULL 
);

INSERT INTO accounts (account_no,balance)
VALUES (100,20100);

INSERT INTO accounts (account_no,balance)
VALUES (200,10100);
	
SELECT * FROM accounts a ;

--SO we want the transfer of money to either succeed or fail
--as whole
--we do not want to have one account to have extra money
--and another account not have the money taken away
BEGIN TRANSACTION;

UPDATE accounts
   SET balance = balance - 1000
 WHERE account_no = 100;

UPDATE accounts
   SET balance = balance + 1000
 WHERE account_no = 200;
 
INSERT INTO account_changes(account_no,flag,amount,changed_at) 
VALUES(100,'-',1000,datetime('now'));

INSERT INTO account_changes(account_no,flag,amount,changed_at) 
VALUES(200,'+',1000,datetime('now'));

COMMIT;

SELECT * FROM accounts;

SELECT * FROM account_changes;

SELECT datetime('now');

INSERT INTO account_changes(change_no,account_no,flag,amount,changed_at) 
VALUES(1, 200,'+',1000,datetime('now'));

INSERT INTO account_changes(account_no,flag,amount,changed_at) 
VALUES(200,'+',1000,'2022');

BEGIN TRANSACTION;

UPDATE accounts
   SET balance = balance - 20000
 WHERE account_no = 100;

INSERT INTO account_changes(change_no,account_no,flag,amount,changed_at) 
VALUES(2, 100,'-',20000,datetime('now'));

ROLLBACK;

COMMIT;







