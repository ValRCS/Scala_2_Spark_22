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

--TODO 2
--query for all tracks that have been bought









