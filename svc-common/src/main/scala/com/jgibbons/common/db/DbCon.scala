package com.jgibbons.common.db

import java.sql.Connection
import com.typesafe.scalalogging.LazyLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import scala.util.{Failure, Success, Try, Using}

// If using Oracle then also give a statement size cache and set it on the connection
// when you have a connection set the statement cache size, or it wil be SLOW (with oracle
// dbCon.using(s"inserting $dto", con=> {
//   val oracle: OracleCOnnection = dbCon.toOracle(con)
//   dbCon.setCacheSize(oracle, statementCacheSize)
// }
case class DbCon(hikaryConfig:HikariConfig) extends LazyLogging {
  val ds = new HikariDataSource(hikaryConfig)
  sys.addShutdownHook(ds.close())

  def open(): Try[Connection] = Try( ds.getConnection())
  def close(con:Connection) :Unit = con.close()

  def using[A](debugStr:String, f:Connection=>Try[A]):Try[A] = {
    val t0 = System.currentTimeMillis()
    Using(ds.getConnection())(con => f(con)).flatten match {
      case s@Success(v) =>
        logger.debug(s"$debugStr completed ${System.currentTimeMillis() - t0} ms")
        s
      case f@Failure(ex) =>
        logger.error(s"$debugStr Failed to execute DB operation", ex)
        f
    }
  }
}

