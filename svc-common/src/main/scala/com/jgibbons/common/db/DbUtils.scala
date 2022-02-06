package com.jgibbons.common.db

import java.sql.{Connection, PreparedStatement, ResultSet, Timestamp}

import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

object DbUtils extends LazyLogging {
  def queryPs[T](con:Connection, sql:String, params:Map[Int, Any],
                 ormFn:ResultSet => T) : Try[T] = {
    val startMs = System.currentTimeMillis()
    val queryStr = QueryLogger.getQueryStr(sql, params)
    val t = Try {
      val p = con.prepareStatement(sql)
      params.foreach(e=> addPsParam(p, e._1, e._2))
      val rs = p.executeQuery()
      val ret = ormFn(rs)
      rs.close()
      p.close()
      ret
    }
    t match {
      case r@Success(v) =>
        logger.info(s"Db call took [${System.currentTimeMillis() - startMs}]ms, Query: [$queryStr], returned [$v]")
        r
      case r@Failure(ex) =>
        logger.error(s"FAILED Db call took [${System.currentTimeMillis() - startMs}]ms, Query: [$queryStr]", ex)
        r
    }
  }

  private[db] def addPsParam(ps:PreparedStatement, index:Int, value:Any): Unit = {
    value match {
      case v:String => ps.setString(index,v)
      case v:Long => ps.setLong(index,v)
      case v:Int => ps.setInt(index,v)
      case v:Timestamp => ps.setTimestamp(index,v)
      case Some(v:String) => ps.setString(index,v)
      case Some(v:Long) => ps.setLong(index,v)
      case Some(v:Int) => ps.setInt(index,v)
      case Some(v:Timestamp) => ps.setTimestamp(index,v)
      case None => ps.setObject(index, null)
      case ty@_ => throw new Exception(s"Failed to handle parameter index [$index], type [${ty.getClass.getName}], value [$ty]")
    }
  }
}
