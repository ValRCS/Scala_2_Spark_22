package com.github.ValRCS

import com.datastax.driver.core._

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import scala.collection.mutable.ArrayBuffer

object CassandraExample {

  def getClusterSession(host: String, port: Int, username: String, password: String, caPath: String): (Cluster,Session) = {
    println(s"Going to connect to $host")
    val sslOptions = loadCaCert(caPath)
    //    var cluster = null
    val cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
    (cluster, cluster.connect) //not throwing exception
  }

  def runQuery(session: Session, keyspace: String, query: String):ResultSet = {
    session.execute(s"USE $keyspace") //use this database //no validity is provided here
    session.execute(query) //so for non SELECTS nothing will be returned
  }

  def rawQuery(session: Session, query: String):ResultSet = {
    session.execute(query) //will return ResultSet if SELECT
  }

  @throws[Exception]
  def cassandraExample(host: String, port: Int, username: String, password: String, caPath: String): Unit = {
    println(s"Going to connect to $host")
    val sslOptions = loadCaCert(caPath)
    //    var cluster = null
    val cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
    try {
      //      cluster: Cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
      val session = cluster.connect
      //first we create a new keyspace if it does not exist //so similar to creating a database
      session.execute("CREATE KEYSPACE IF NOT EXISTS example_keyspace WITH REPLICATION = {'class': 'NetworkTopologyStrategy', 'aiven': 3}")
      session.execute("USE example_keyspace") //use this database
      //now we create a new table if we do not have it
      session.execute("CREATE TABLE IF NOT EXISTS example_java (id int PRIMARY KEY, message text)")
      //similar to prepared statements in SQL we bind values
      //so to protect against CQL injection we bind the values
      session.execute("INSERT INTO example_java (id, message) VALUES (?, ?)", 123, "Hello from Java-Scala!")
      session.execute("INSERT INTO example_java (id, message) VALUES (?, ?)", 90, "Hello from Valdis")
      val rs = session.execute("SELECT id, message FROM example_java")
      import scala.jdk.CollectionConverters._
      //      val row = rs.one()
      rs.forEach(row => println(String.format("Row: id = %d, message = %s", row.getInt("id"), row.getString("message"))))

      //      while ( rs.next() ) {
      //
      //      }
      //      for (row <- rs) {
      //        System.out.println(String.format("Row: id = %d, message = %s", row.getInt("id"), row.getString("message")))
      //      }
    } finally if (cluster != null) cluster.close()
  }

  @throws[Exception]
  def setResults(host: String, port: Int, username: String, password: String, caPath: String, keyspace:String, id:Int, message:String): Unit = {
    println(s"Going to connect to $host")
    val sslOptions = loadCaCert(caPath)
    //    var cluster = null
    val cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
    try {
      //      cluster: Cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
      val session = cluster.connect
      session.execute(s"USE $keyspace") //FIXME worry about CQL injection attacks, this might not be safe if you depend on outside CQL
      //TODO remove hardcoded table example_java
      val rs = session.execute("INSERT INTO example_java (id, message) VALUES (?, ?)", id, message) //TODO think accepting arbitrary string safety
    }finally if (cluster != null) cluster.close()
  }



  @throws[Exception]
  def getResults(host: String, port: Int, username: String, password: String, caPath: String, keyspace:String, query: String): Seq[String] = {
    println(s"Going to connect to $host")
    val arrayBuf = ArrayBuffer[String]()
    val sslOptions = loadCaCert(caPath)
    //    var cluster = null
    val cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
    try {
      //      cluster: Cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
      val session = cluster.connect
      session.execute(s"USE $keyspace") //FIXME worry about CQL injection attacks
      val rs = session.execute(query) //TODO think accepting arbitrary string safety
      rs.forEach(row => arrayBuf += String.format("Row: id = %d, message = %s", row.getInt("id"), row.getString("message")))

    }finally if (cluster != null) cluster.close() //closes session AND cluster (some extra options)
    arrayBuf.toSeq
  }

  @throws[Exception]
  private def loadCaCert(caCertPath: String) = {
    val cf = CertificateFactory.getInstance("X.509")
    var fis: FileInputStream = null
    var caCert: X509Certificate = null
    try {
      fis = new FileInputStream(caCertPath)
      caCert = cf.generateCertificate(fis).asInstanceOf[X509Certificate]
    } finally if (fis != null) fis.close()
    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    val ks = KeyStore.getInstance(KeyStore.getDefaultType)
    ks.load(null)
    ks.setCertificateEntry("caCert", caCert)
    tmf.init(ks)
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, tmf.getTrustManagers, null)
    RemoteEndpointAwareJdkSSLOptions.builder.withSSLContext(sslContext).build
  }
}
