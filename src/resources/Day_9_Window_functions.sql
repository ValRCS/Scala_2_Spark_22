--https://www.sqlite.org/windowfunctions.html
--https://www.sqlitetutorial.net/sqlite-window-functions/

--with window functions we do not group rows
--we use function for each row using some criteria (usually surrounding rows)

SELECT * FROM invoices i ;

SELECT *, 
LAST_VALUE(BillingCountry)
OVER (
	ORDER BY BillingCity 
)
FROM invoices i 
ORDER BY BillingCity;

SELECT * FROM tracks;

SELECT name, 
	Bytes 
FROM tracks t 
WHERE 
  AlbumId = 1;
 
--we want to put the smaller track name next to each track
--but we do not want to rearrange the track order
SELECT t.TrackId ,name, 
	Bytes,
	FIRST_VALUE(Name) OVER (
		ORDER BY Bytes
	) smallestTrack
FROM tracks t 
WHERE 
  AlbumId = 1;
 
--for last value it gets trickier
--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-last_value/

 SELECT t.TrackId ,name, 
	Bytes,
	LAST_VALUE(Name) OVER (
		ORDER BY Bytes
		RANGE BETWEEN UNBOUNDED PRECEDING AND 
        UNBOUNDED FOLLOWING
	) bigestTrack
FROM tracks t 
WHERE 
  AlbumId = 1;
 
 --usually you use RANK to show rank of row within that window
 --withou partition we rank over the whole selection
 SELECT
    TrackId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		ORDER BY Milliseconds DESC
	) LengthRank 
FROM
	tracks;

SELECT
    TrackId,
    AlbumId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		ORDER BY Milliseconds DESC
	) LengthRank 
FROM
	tracks
ORDER BY TrackId ; --i force it back to original insertion order

--lets add ranking to each song based on its length WITHIN its album
SELECT
    TrackId,
    AlbumId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		PARTITION BY AlbumId  -- so each row will calculate on its own partition/window
		ORDER BY Milliseconds DESC
	) LengthRank,
	RANK () OVER (
		PARTITION BY AlbumId
		ORDER BY Name ASC -- so alphabetical ranking within each album
	) NameRank
FROM
	tracks
WHERE albumid = 3
ORDER BY TrackId ;

--lets find some albums which have 5 songs
SELECT AlbumId FROM tracks t 
GROUP BY AlbumId 
HAVING COUNT(AlbumId) = 5;

SELECT
    TrackId,
    AlbumId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		PARTITION BY AlbumId  -- so each row will calculate on its own partition/window
		ORDER BY Milliseconds DESC
	) LengthRank,
	RANK () OVER (
		PARTITION BY AlbumId
		ORDER BY Name ASC -- so alphabetical ranking within each album
	) NameRank
FROM
	tracks
WHERE albumid IN (15,137)
ORDER BY TrackId ;

-- in this example where we have a single album filtered
--we do not really need to partition by album id
--if we have two albums
--then not using partition
--will providing ranking for the whole result set
--here that would be ranking over both albums
SELECT
    TrackId,
    AlbumId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		ORDER BY Milliseconds DESC
	) LengthRankOverAll,
	RANK () OVER (
		ORDER BY Name ASC 
	) NameRankOverAll
FROM
	tracks
WHERE albumid IN (15,137)
ORDER BY TrackId ;

--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-dense_rank/

SELECT
	AlbumId,
	Name,
	Milliseconds,
	DENSE_RANK () OVER ( 
		PARTITION BY AlbumId 
		--chances of having 2 songs with same millisecond length
		--in same album are quite small
		ORDER BY Milliseconds 
	) LengthRank
FROM
	tracks;



SELECT
	AlbumId,
	Name,
	Milliseconds,
	RANK () OVER ( 
		PARTITION BY AlbumId 
		--chances of having 2 songs with same millisecond length
		--in same album are quite small
		ORDER BY Milliseconds 
	) LengthRank
FROM
	tracks;

SELECT milliseconds FROM tracks t 
GROUP BY Milliseconds 
HAVING COUNT(Milliseconds) >=2;

--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-percent_rank/
--percent ranking 
SELECT
    Name,
    Milliseconds,
    PERCENT_RANK() OVER( 
        ORDER BY Milliseconds 
    ) LengthPercentRank,
    --RANK and DENSE RANK will be same since no tiebreaks
    RANK() OVER( 
        ORDER BY Milliseconds 
    ) LengthRank,
     DENSE_RANK() OVER( 
        ORDER BY Milliseconds 
    ) LengthDenseRank
FROM
    tracks 
WHERE
    AlbumId = 1;
   
--LAG function lets us access some previous row within our window
--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-lag/
SELECT
	CustomerId,
	STRFTIME('%Y',InvoiceDate) Year,
	--https://database.guide/how-to-extract-the-day-month-and-year-from-a-date-in-sqlite/
	Total
	FROM
	Invoices 
ORDER BY
	CustomerId,
	Year,
	Total;


SELECT
	CustomerId,
	STRFTIME('%Y',InvoiceDate) Year,
	Total,
	LAG ( Total, 1, 0 ) OVER ( 
		ORDER BY InvoiceDate 
	) PreviousYearTotal
FROM
	Invoices 
WHERE
	CustomerId = 4;

--if we want to do it for all customers
--then we will use partition by customerID
SELECT
	CustomerId,
	STRFTIME('%Y',InvoiceDate) Year,
	Total,
	--in LAG(column, offsetback, default when nothing to go backto)
	LAG ( Total,1,0) OVER ( 
		PARTITION BY CustomerId --more precise than year
		ORDER BY  InvoiceDate) PreviousYearTotal 
FROM
	Invoices;

CREATE VIEW v_customer_totals_years
AS
SELECT
	CustomerId,
	STRFTIME('%Y',InvoiceDate) Year,
	Total,
	--in LAG(column, offsetback, default when nothing to go backto)
	LAG ( Total,1,0) OVER ( 
		PARTITION BY CustomerId --more precise than year
		ORDER BY  InvoiceDate) PreviousYearTotal 
FROM
	Invoices;

SELECT * FROM v_customer_totals_years;

SELECT *, 
total-previousyeartotal delta
FROM v_customer_totals_years; 

--similarly we can use LEAD to access next rows within window
SELECT
	CustomerId,
	STRFTIME('%Y',InvoiceDate) Year,
	InvoiceDate,
	Total,
	LAG ( Total, 1, 0 ) OVER (
		PARTITION BY CustomerId 
		ORDER BY InvoiceDate)
		LastYearsTotal,
	LEAD ( Total, 1, 0 ) OVER (
		PARTITION BY CustomerId 
		ORDER BY InvoiceDate
	) NextYearTotal 
FROM
	Invoices;

--now we can try to get a running total(cumulative sum)
--
SELECT 
    CustomerID,
    InvoiceDate,
    Total,
    SUM(total) OVER (
        ORDER BY InvoiceDate
    ) RunningTotal
FROM
   invoices i2 
  WHERE CustomerId = 33;
--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-window-frame/

 --now we can create a running total for all customers
 SELECT 
    CustomerID,
    InvoiceDate,
    Total,
    SUM(total) OVER (
        PARTITION BY CustomerId --without partition it would be running total for ALL invoices
        ORDER BY InvoiceDate
    ) RunningTotal
FROM
   invoices i2 ;
  
--I can add running average as well
 SELECT 
    CustomerID,
    InvoiceDate,
    Total,
    SUM(total) OVER (
        PARTITION BY CustomerId --without partition it would be running total for ALL invoices
        ORDER BY InvoiceDate
    ) RunningTotal ,
     AVG(total) OVER (
        PARTITION BY CustomerId --without partition it would be running total for ALL invoices
        ORDER BY InvoiceDate
    ) RunningAverage ,
    COUNT(total) OVER (
    	PARTITION BY CustomerId
    	ORDER BY InvoiceDate
    ) RunningCount   
FROM
   invoices i2 ;

  
  --by default when we define OVER
  --we get access to window of current row and 
  --unbounded previous rows by our partitioning and ordering
  --https://www.sqlite.org/windowfunctions.html
  
  --The default frame-spec is:

--RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW EXCLUDE NO OTHERS
--The default means that aggregate window functions read all rows from the beginning of the partition up to and including the current row and its peers

