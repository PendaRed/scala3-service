package com.jgibbons.svcfirst.db.dao

import com.typesafe.scalalogging.LazyLogging

import java.sql.PreparedStatement

// I dragged this in as it was useful for Oracle logging with Doobie once, and I dont want to lose the code.
trait SvcDao extends LazyLogging {
  def logPs(ps: PreparedStatement): Unit = logPs("", ps, "")

  def logPs(ps: PreparedStatement, postFix: String): Unit = logPs("", ps, postFix)

  def logPs(prefix: String, ps: PreparedStatement, postfix: String): Unit =
    logger.debug(s"Query $prefix: ${ps.toString.replace("\r\n", "").replace("\n", "")}$postfix") // Works with Postgres, not Oracle
}
