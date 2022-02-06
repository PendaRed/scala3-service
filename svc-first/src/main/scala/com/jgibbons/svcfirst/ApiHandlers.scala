package com.jgibbons.svcfirst

import cats.data.EitherT
import cats.effect.*
import com.jgibbons.svcfirst.clientdto.ClientRowDto
import com.jgibbons.svcfirst.dbendpointexample.DbEndpoint
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}


/**
 * Nothing here about rest or swagger.  Purely the business logic
 */
case class ApiHandlers(dbEnpoint: DbEndpoint) extends LazyLogging {
  extension[A] (t: Try[A])
    def failFastOnDbError: A = t match {
      case Failure(ex) =>
        val msg = "Database error indicating a DB connection error, or a coding bug.  Service should exit"
        logger.error(msg, ex)
        throw new RuntimeException(msg, ex)
      case Success(v) => v
    }

 def countCharacters(s: String): IO[Either[Unit, String]] =
    IO {Right[Unit, String](""+s.length)}

  def findById(id: Long): IO[Either[Unit, ClientRowDto]] = IO{dbEnpoint.findById(id).failFastOnDbError.toRight( () ) }
}
