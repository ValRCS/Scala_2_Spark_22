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

--TODO 2
--Which cities has the best customers
--This means we want to have an ordered list
--5 best cities with highest sum of totals

--TODO 3 Find the biggest 3 spenders
--this might involve joining customers and invoices and invoice items
--then using GROUP BY and then SUM on grouped TOTAL

--TODO 4 find ALL listeners to classical music
-- include their names and emails and phone numbers
--this might not need aggregation
