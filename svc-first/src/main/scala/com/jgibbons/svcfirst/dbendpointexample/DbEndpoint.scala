package com.jgibbons.svcfirst.dbendpointexample

import com.jgibbons.svcfirst.clientdto.ClientRowDto
import com.jgibbons.svcfirst.db.dao.SimpleDao
import com.jgibbons.svcfirst.db.dto.RowDto
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}
import com.jgibbons.svcfirst.clientdto.ClientRowDto.asClientDto

/**
 * Functional folks argue that all db interaction should be wrapped in effects as close to the db as possible.
 * I disagree, lets keep the db layer as normal as possible.  The http4s layer can be effectful.
 */
case class DbEndpoint(dao: SimpleDao) extends LazyLogging {
  def findById(id: Long): Try[Option[ClientRowDto]] = dao.findById(id).map(r=>r.map(row=>row.asClientDto))
}
