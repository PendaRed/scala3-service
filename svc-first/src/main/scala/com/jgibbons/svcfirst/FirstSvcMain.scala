package com.jgibbons.svcfirst

import cats.effect.{ExitCode, IO, IOApp, Resource}
import sttp.tapir.*
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.headers.Server
import com.jgibbons.common.db.DbCon

import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.server.middleware.*
import cats.data.Kleisli
import cats.effect.*
import cats.syntax.all.*
import com.jgibbons.svcfirst.db.dao.SimpleDao
import com.jgibbons.svcfirst.dbendpointexample.DbEndpoint
import io.circe.generic.auto.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object FirstSvcMain extends IOApp {

  def makeRouter(dbCon:DbCon) : Kleisli[IO, Request[IO], Response[IO]] = {
    val dbEndpoints = DbEndpoint(SimpleDao(dbCon))
    val apiEndpoints = new ApiEndpoints(ApiHandlers(dbEndpoints))
    val routes = apiEndpoints.makeRoutes()

    Router[IO](
      "" -> routes
    ).orNotFound
  }

  private def serveStream(dbCon:DbCon, serverConfig:ServerCfg)  = {
    import org.http4s.blaze.*
    for {
      server <- BlazeServerBuilder[IO]
        .bindHttp(serverConfig.portNum, serverConfig.hostName)
        .withHttpApp(makeRouter(dbCon))
        .resource
    } yield server
  }

  override def run(args:List[String]) : IO[ExitCode] = {
    val (serverCfg, hikariProperties) = MicroServiceConfig()
    val dbCon = DbCon(hikariProperties)
    serveStream(dbCon, serverCfg).use( _ => IO {
      println(s"Go to: http://${serverCfg.hostName}:${serverCfg.portNum}/docs")
      println("Press any key to exit ...")
      scala.io.StdIn.readLine()
    }).as(ExitCode.Success)
  }
}
