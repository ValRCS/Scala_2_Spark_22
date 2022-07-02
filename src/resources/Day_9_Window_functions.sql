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

