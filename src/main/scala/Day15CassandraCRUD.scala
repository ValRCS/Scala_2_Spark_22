package com.github.ValRCS

import CassandraExample.{getClusterSession, rawQuery, runQuery}

import com.datastax.driver.core.Session

import scala.collection.mutable.ArrayBuffer

case class User(country: String,
                user_email: String,
                first_name: String,
                last_name: String,
                age: Int)

object Day15CassandraCRUD extends App {

  //TODO store host and port also in System Enviroment so no need for outside parties to know your host address
  val host = "cassandra-2fc8be97-valdis-9619.aivencloud.com" //your url will be slightly different
  val port = 22480 //port might also be a different
  val username = "avnadmin" //most likely the same
  val password = scala.util.Properties.envOrElse("CASSANDRA_PW", "")
  //  val caPath = "C:\\certs\\"
  val caPath = "./src/resources/certs/ca.pem" //you need to download your own cert, do not commit to GIT!!
  println("Opening up a connection to Cassandra cluster")
  val (cluster, session) = getClusterSession(host=host,port=port,username=username,password=password, caPath=caPath)

  //so instead of vs_kespace we could use any name for our database (keyspace)
  val keyspaceCreationQuery =
    """
      |CREATE KEYSPACE IF NOT EXISTS
      |vs_keyspace WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'aiven' : 3 };
      |""".stripMargin

  //should do nothing if keyspace is already present
  rawQuery(session, keyspaceCreationQuery)

  //dropping table from vs_keyspace keyspace/database
//  session.execute("DROP TABLE IF EXISTS vs_keyspace.users_by_country")

  //will do nothing if users_by_country already exist
  val tableCreationQuery =
    """
      |CREATE TABLE IF NOT EXISTS users_by_country (
      |    country text,
      |    user_email text,
      |    first_name text,
      |    last_name text,
      |    age int,
      |    PRIMARY KEY ((country), user_email)
      |)
      |""".stripMargin

  runQuery(session, "vs_keyspace", tableCreationQuery)


  //we need to convert our default int (4bytes) to 2 byte Short because that Column requires 2 byte integer
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?, ?,?,?,?)", "US", "john@example.com", "John","Wick",55.toShort)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?, ?,?,?,?)", "UK", "mrbean@example.com", "Rowan","Atkinson",65.toShort)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?, ?,?,?,?)", "LV", "kk@example.com", "Krišjānis","Kariņš",60.toShort)

//if column is regular int then we give it regular int of course
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "US", "john@example.com", "John","Wick",55)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "UK", "mrbean@example.com", "Rowan","Atkinson",65)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "UK", "boris@example.com", "Boris","Johnson",58)
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LV", "kk@example.com", "Krišjānis","Kariņš",60)
  //unlike SQL where primary key is usually inserted automatically the above queries would keep adding more data
  //here we just have the 4 rows (and also 3 partitions)

  val userResults = session.execute("SELECT * FROM vs_keyspace.users_by_country")
  userResults.forEach(row => println(row))

  //TODO add 2 more users, one from LV, one from LT
  //return Latvian users
  //return Lithuanian users
  //ideally you would not only print but save the users into a case class User - we can look at that later
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LV", "el@example.com", "Egils","Levits",67)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LT", "zi@example.com", "Žīdrūns","Ilgausks",47)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LV", "as@example.com", "Anastasija","Sevastova",32)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LT", "ls@example.com", "Lina","Stančiūtė",36)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LT", "Tomas@example.com", "Tomas","Kaukas",20)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LV", "katrina@example.com", "Katrina","Ozoliņš",60)
//  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
//    " VALUES (?,?,?,?,?)", "LT", "j.petraitis@gmail.com", "Jonas","Petraitis", 38)

  //if we are correcting some column that is part of primary key then there will be a new entry in Cassandra database
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LV", "laura.k@correctemail.com", "Laura","Kalnina", 27)
  //if we are changing something which is not part of primary key this will function as UPDATE
  session.execute("INSERT INTO vs_keyspace.users_by_country (country,user_email,first_name,last_name,age)" +
    " VALUES (?,?,?,?,?)", "LV", "laura.k@correctemail.com", "Laura","Kalnina", 26)

  //you could also use UPDATE to change column values - there are some subtle differences between insert
  //https://stackoverflow.com/questions/16532227/difference-between-update-and-insert-in-cassandra

  //deleting single columns with some matches we are using email column which is part of primary key
  session.execute("DELETE age FROM vs_keyspace.users_by_country " +
    "WHERE user_email = 'el@example.com' AND country = 'LV' ") //here we need BOTH user_mail and countery since they combined create a primary_key

  //we can delete the whole row by NOT specifying columns to be deleted
  session.execute("DELETE FROM vs_keyspace.users_by_country " +
    "WHERE user_email = 'laura.k@gmail.com' AND country = 'LV' ") //NO ERROR if nothing is found

  //we need primary key for our WHERE this will not work
  //https://docs.datastax.com/en/cql-oss/3.x/cql/cql_reference/cqlDelete.html
//  session.execute("DELETE FROM vs_keyspace.users_by_country " +
//    "WHERE country = 'LV' AND age < 60") //ERROR because age is not part of primary key!!

  println("Users from Latvia:")

  def getUsersFromCountry(session:Session, country:String):Array[User] = {
    val users = rawQuery(session, s"SELECT * FROM vs_keyspace.users_by_country WHERE country = '$country'") //Beware CQL injection
    val resultBuffer = ArrayBuffer[User]()
    users.forEach(row=> resultBuffer += User(
      row.getString("country"),
      row.getString("user_email"),
      row.getString("first_name"),
      row.getString("last_name"),
      row.getInt("age")))
    resultBuffer.toArray
  }

  val usersLV = getUsersFromCountry(session, "LV")
  println(usersLV.mkString(",\n"))

  println("Users from Lithuania:")
  val usersLT = getUsersFromCountry(session, "LT")
  println(usersLT.mkString(",\n"))


  //if the column is not part of primary key you will need to be explicit
  //https://www.freecodecamp.org/news/the-apache-cassandra-beginner-tutorial/
  //potentially this type of query could take looooong time
  val youngUserResults = session.execute("SELECT * FROM vs_keyspace.users_by_country WHERE  age < 50 ALLOW FILTERING")
  youngUserResults.forEach(row=> println(row))

  //we can cast to different data types in our CQL queries
  //https://www.geeksforgeeks.org/cast-function-in-cassandra/
  //here we needed to cast bigint to int
  val userCount = session.execute("SELECT CAST(COUNT(1) AS int) FROM vs_keyspace.users_by_country")
  println("User count")
  userCount.forEach(row=>println(row, row.getInt(0))) //there is only one column so we get the first column(0th)

  println("Will close our Cassandra Cluster")
  cluster.close()
  println("Connection should be closed now")
}
