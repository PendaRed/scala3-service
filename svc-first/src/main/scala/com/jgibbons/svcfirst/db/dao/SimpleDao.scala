package com.jgibbons.svcfirst.db.dao

import com.jgibbons.common.db.DbCon
import com.jgibbons.common.db.QueryLogger.*
import com.jgibbons.svcfirst.db.dto.RowDto

import java.sql.{Connection, PreparedStatement}
import scala.util.{Try, Using}

/**
 * I used Doobie, still prefer jdbc, also note the total lack of cats and effects polluting simple db stuff.
 */
case class SimpleDao(dbCon: DbCon) {
  def findById(id:Long) : Try[Option[RowDto]] =
    dbCon.using("findById", con=>SimpleDao.findById(con, id))
}

/**
 * Separate out the implementations below so that you can compose them inside transactions if you want.
 * ie external to the case class which will inflict its own transaction boundaries.
 */
object SimpleDao extends SvcDao {

  private def createFromQuery(ps: PreparedStatement): Option[RowDto] = {
    val rs = ps.executeQuery()
    if (rs.next) {
      val dto = RowDto(
        id = rs.getLong(1),
        json = rs.getString(2),
        status = rs.getString(3),
        version = rs.getInt(4),
        createdAt = rs.getTimestamp(5),
        createdBy = rs.getString(6),
        updatedAt = rs.getTimestamp(7),
        updatedBy = rs.getString(8)
      )

      logPs(ps, s", returned $dto")
      Some(dto)
    } else {
      logPs(ps, ", returned 0 rows")
      None
    }
  }

  def findById(con:Connection, id:Long) : Try[Option[RowDto]] = {
    val sql = s"""SELECT ID, JSON, status, version, created_at, created_by,updated_at, updated_by
                 |FROM GAME_ARMY_DESIGN
                 |  WHERE ID=?
                 |""".stripMargin
    Using(con.prepareStatement(sql))(ps=> {
      ps.setLong(1, id)
      createFromQuery(ps)
    })
  }
}