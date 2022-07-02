--https://www.sqlitetutorial.net/sqlite-aggregate-functions/
SELECT * FROM invoices i ;
SELECT SUM(total), 
COUNT(total), -- rememember we can use COUNT to just count rows
MIN(total),
AVG(total),
MAX(total)
FROM invoices i ;

--we can use these aggregate functions to work on the results after filter
SELECT COUNT(*),
	'Invoices over 5.99',
	SUM(total),
	MIN(total),
	AVG(total),
	MAX(total)
FROM invoices i
WHERE total > 5.99;

SELECT total, 
COUNT(total), 
SUM(total),
MIN(total),AVG(total),MAX(total) --not useful because all grouped items are the same here
FROM invoices i
GROUP BY total;

SELECT * FROM invoices;

SELECT BillingCountry country, 
COUNT(invoiceid) FROM invoices i 
GROUP BY country;

--if we group by one column we can perform the aggregate functions
--on other columns
SELECT BillingCountry country, 
COUNT(invoiceid),
SUM(total), -- so summing all the sales for a particular country
SUM(DISTINCT total), -- so only unique totals are added
MIN(total), --smallest sale per country
ROUND(AVG(total),2), -- average sale per country
MAX(total) -- biggest sale per country
FROM invoices i 
GROUP BY country;

--AVG example
SELECT ROUND(AVG((Milliseconds) /60000),2) FROM tracks;

SELECT albumid,
	ROUND(AVG((Milliseconds) /60000),2) avg_minutes FROM tracks t 
GROUP BY albumid
ORDER BY avg_minutes DESC;

--if we want to know the album names we do join 
SELECT t.albumid, 
	ROUND(AVG((Milliseconds) / 60000),2) avg_minutes,
	COUNT(t.albumid) songCount,
	a2.Title albumName,
	t.Name, -- just the first track
	GROUP_CONCAT(t.name, ',') allSongs -- similar to mkString in Scala
FROM tracks t 
JOIN albums a2 
ON t.AlbumId = a2.albumid
GROUP BY t.albumid
ORDER BY avg_minutes DESC;

SELECT * FROM tracks t ;

SELECT COUNT(*) FROM tracks t;

SELECT t.albumid, 
	ROUND(AVG((Milliseconds) / 60000),2) avg_minutes,
	COUNT(t.albumid) songCount,
	a2.Title albumName,
	t.Name, -- just the first track
	GROUP_CONCAT(t.name, ',') allSongs -- similar to mkString in Scala
FROM tracks t 
JOIN albums a2 
ON t.AlbumId = a2.albumid
GROUP BY t.albumid
ORDER BY songCount DESC;

--calculate album length we would use sum on millseconds 
--grouped by album id
SELECT 
albumId,
SUM(milliseconds) / 60000 album_len_minutes
FROM tracks t 
GROUP BY albumid
ORDER BY album_len_minutes DESC;

--if you wanted to find the actual album name you would do the join on the above with albums table

SELECT MAX(milliseconds) / 60000 mins
FROM tracks;

--we could use a subquery to find the longest tracks
SELECT * FROM tracks t 
WHERE Milliseconds = (
	SELECT MAX(milliseconds) 
	FROM tracks
);

SELECT * FROM tracks t 
WHERE Milliseconds = (
	SELECT MIN(milliseconds) 
	FROM tracks
);

SELECT * 
FROM tracks t 
WHERE albumid = 200;

--WE CAN use GROUP_CONCAT to have comma separated list of track names
SELECT 
  GROUP_CONCAT(name) -- so ',' is the default
 FROM tracks t 
 WHERE AlbumId = 200;

SELECT 
  GROUP_CONCAT(name, '-\*/-') -- so ',' is the default
 FROM tracks t 
 WHERE AlbumId = 200;

--TODO 1
--Which city has the most invoices?
--Order by invoice count
SELECT BillingCity, COUNT(BillingCity) cnt FROM invoices i 
GROUP BY BillingCity 
ORDER BY cnt DESC;

--TODO 2
--Which cities has the best customers
--This means we want to have an ordered list
--5 best cities with highest sum of totals
SELECT BillingCity, SUM(Total) total FROM invoices i 
GROUP BY BillingCity 
ORDER BY total DESC
LIMIT 5;

--we could combine the above queries in a single one
--because we are using the same table and same group by
SELECT BillingCity, 
COUNT(BillingCity) orders, 
SUM(Total) total, 
ROUND(AVG(Total),2) averageOrder
FROM invoices i 
GROUP BY BillingCity 
ORDER BY total DESC
LIMIT 5;

--TODO 3 Find the biggest 3 spenders
--this might involve joining customers and invoices and invoice items
--then using GROUP BY and then SUM on grouped TOTAL
SELECT i.CustomerId, 
		c.FirstName, 
		c.LastName, 
		c.Country,
		c.City,
		i.Total 
--		SUM(total) totalByCustomer -- aggregate returns a single value
FROM invoices i 
JOIN customers c 
	ON i.CustomerId = c.CustomerId ;


SELECT i.CustomerId, 
		c.FirstName, 
		c.LastName, 
		c.Country,
		c.City,
		SUM(total) totalByCustomer 
FROM invoices i 
JOIN customers c 
	ON i.CustomerId = c.CustomerId 
GROUP BY i.CustomerId 
ORDER BY totalByCustomer DESC 
LIMIT 3;

--following query will give sum total multiple times
--so not correct!
--it happens because a single row represents a single item purchase
SELECT c.CustomerId, FirstName, LastName, SUM(i.Total) total_spent_amount FROM customers c 
JOIN invoices i
ON i.CustomerId = c.CustomerId
JOIN invoice_items ii 
ON i.InvoiceId = ii.InvoiceId 
GROUP BY i.CustomerId 
ORDER BY total_spent_amount DESC
LIMIT 3;

SELECT c.CustomerId, 
FirstName, 
LastName, 
i.Total,
i.InvoiceId ,
ii.UnitPrice,
ii.Quantity 
FROM customers c 
JOIN invoices i
ON i.CustomerId = c.CustomerId
JOIN invoice_items ii 
ON i.InvoiceId = ii.InvoiceId;

SELECT * FROM customers c 
WHERE city = 'Prague';

--here the problem is that unit price is not the full picture
SELECT c.CustomerID, 
c.Firstname, 
c.Lastname, 
SUM(ii.UnitPrice) as sumUnits
FROM invoices i 
JOIN invoice_items ii ON ii.InvoiceId = i.InvoiceId 
JOIN customers c ON c.CustomerId = i.InvoiceId 
GROUP BY c.CustomerId
ORDER by sumUnits DESC
LIMIT 3;

SELECT * FROM invoice_items ii
ORDER BY UnitPrice DESC;

SELECT * FROM invoice_items ii 
JOIN invoices i 
ON i.InvoiceId = ii.InvoiceId ;

SELECT i.CustomerId, SUM(UnitPrice) sumPrices FROM invoice_items ii 
JOIN invoices i 
ON i.InvoiceId = ii.InvoiceId 
JOIN customers c ON c.CustomerId = i.CustomerId 
GROUP BY i.CustomerId 
ORDER BY sumPrices DESC;



--TODO 4 find ALL listeners to classical music
-- include their names and emails and phone numbers
--this might not need aggregation
SELECT  c.FirstName, 
		c.LastName, 
		c.Email, 
		c.Phone 
FROM invoice_items ii  
JOIN invoices i 
	ON ii.InvoiceId = i.InvoiceId
JOIN customers c 
	ON i.CustomerId = c.CustomerId
JOIN tracks t 
	ON ii.TrackId = t.TrackId
JOIN genres g 
	ON t.GenreId = g.GenreId
WHERE g.Name = 'Classical';

--so we 
SELECT DISTINCT (FirstName || LastName), FirstName, LastName
FROM customers c 
JOIN invoices i 
ON c.CustomerId = i.CustomerId
JOIN invoice_items ii 
ON ii.InvoiceId = i.InvoiceId
JOIN tracks t 
ON t.TrackId = ii.TrackId 
JOIN genres g 
ON g.GenreId = t.GenreId
WHERE g.Name = 'Classical'
ORDER BY FirstName ;

SELECT c.CustomerId, FirstName, LastName, Email, Phone, 
COUNT(t.TrackId) classical_tracks_bought,
SUM(t.Milliseconds) / 60000 minutes_bought
FROM customers c 
JOIN invoices i
ON i.CustomerId = c.CustomerId
JOIN invoice_items ii 
ON i.InvoiceId = ii.InvoiceId 
JOIN tracks t 
ON t.TrackId = ii.TrackId 
JOIN genres g 
ON g.GenreId = t.GenreId 
WHERE g.Name = 'Classical'
GROUP BY i.CustomerId 
ORDER BY classical_tracks_bought DESC;

SELECT ii.TrackId, i.CustomerId, c.FirstName, c.LastName, c.Email, c.Phone, g.Name AS genreName
FROM invoice_items ii  
JOIN invoices i 
	ON ii.InvoiceId = i.InvoiceId
JOIN customers c 
	ON i.CustomerId = c.CustomerId
JOIN tracks t 
	ON ii.TrackId = t.TrackId
JOIN genres g 
	ON t.GenreId = g.GenreId
WHERE g.Name = 'Classical'
GROUP BY i.CustomerId 
ORDER BY i.CustomerId ;

SELECT * FROM customers c2 
WHERE c2.CustomerId BETWEEN 30 AND 40;

SELECT ii.TrackId, t.name, i.CustomerId, c.FirstName, c.LastName, c.Email, c.Phone, g.Name AS genreName
FROM invoice_items ii  
JOIN invoices i 
	ON ii.InvoiceId = i.InvoiceId
JOIN customers c 
	ON i.CustomerId = c.CustomerId
JOIN tracks t 
	ON ii.TrackId = t.TrackId
JOIN genres g 
	ON t.GenreId = g.GenreId
WHERE c.CustomerId = 33;

CREATE VIEW IF NOT EXISTS v_track_purchases
AS
SELECT ii.TrackId, t.name, i.CustomerId, c.FirstName, c.LastName, c.Email, c.Phone, g.Name AS genreName
FROM invoice_items ii  
JOIN invoices i 
	ON ii.InvoiceId = i.InvoiceId
JOIN customers c 
	ON i.CustomerId = c.CustomerId
JOIN tracks t 
	ON ii.TrackId = t.TrackId
JOIN genres g 
	ON t.GenreId = g.GenreId;

SELECT * FROM v_track_purchases vtp;

SELECT DISTINCT(CustomerId), FirstName , LastName FROM v_track_purchases vtp
WHERE genreName = 'Classical'
GROUP BY CustomerId ;
