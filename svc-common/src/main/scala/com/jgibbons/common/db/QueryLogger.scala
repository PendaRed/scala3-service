package com.jgibbons.common.db

import java.sql.Timestamp
import java.time.format.DateTimeFormatter

object QueryLogger {
  private val df = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS")

  def getQueryStr(sql:String, params:Map[Int, Any]):String = {
    buildQuery(sql,params)
  }

  private [db] def buildQuery(s: String, a:Map[Int, Any]):String = {
    val sqlParts = s.split("\\?")
    val (query, _) = sqlParts.foldLeft((new StringBuilder(""),1)){
      case ((acc, index), str) =>
        acc.append(s"$str${addParam(a, index)}")
        (acc,index+1)
    }
    query.toString
  }

  private[db] def addParam(a:Map[Int, Any], index:Int) = {
    if (!a.contains(index)) s"*** BUG BAD INDEX $index ***"
    else {
      a(index) match {
        case v:String => s"'$v'"
        case v:Long => s"$v"
        case v:Int => s"$v"
        case v:Timestamp =>  s"${df.format(v.toLocalDateTime)}"
        case Some(v:String) => s"'$v'"
        case Some(v:Long) => s"'$v'"
        case Some(v:Int) => s"'$v'"
        case Some(v:Timestamp) =>  s"${df.format(v.toLocalDateTime)}"
        case None => "null"
        case ty@_ => throw new Exception(s"Failed to handle parameter index [$index], type [${ty.getClass.getName}], value [$ty]")
      }
    }
  }
}

