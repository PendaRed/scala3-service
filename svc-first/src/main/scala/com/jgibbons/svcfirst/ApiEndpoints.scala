package com.jgibbons.svcfirst

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.server.middleware.{RequestLogger, ResponseLogger}
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.{PublicEndpoint, endpoint, plainBody, stringBody, stringToPath}
import cats.effect.*
import cats.syntax.all.*
import com.jgibbons.svcfirst.clientdto.ClientRowDto
import com.jgibbons.svcfirst.db.dto.RowDto
import io.circe.generic.auto.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint // allows jsonBody, see https://tapir.softwaremill.com/en/latest/endpoint/json.html

/**
 * Only the end points in Tapir format and swagger in here, no business logic.
 *
 * Its done in lots of lines of code so I can see what does what.  You can squash it up some.
 */
class ApiEndpoints(apiHandlers: ApiHandlers) {
  val ApiVersion = "v1"

  // Document the end point for Tapir
  private val countCharactersEndpoint: PublicEndpoint[String, Unit, String, Any] =
    endpoint.post
      .in("firstms" / ApiVersion / "count" / "chars")
      .in(stringBody).out(plainBody[String])

  import com.jgibbons.svcfirst.clientdto.RenderAsJson.given
  private val findByIdEndpoint = endpoint.post
      .in("firstms" / ApiVersion / "data" )
      .in(query[Long]("id")).out(jsonBody[ClientRowDto])


  // https://github.com/softwaremill/tapir/blob/master/examples/src/main/scala/sttp/tapir/examples/MultipleEndpointsDocumentationHttp4sServer.scala
  // generating and exposing the documentation in yml
  val swaggerUIRoutes: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(
      SwaggerInterpreter().fromEndpoints[IO](
        List(findByIdEndpoint, countCharactersEndpoint), "The FirstSvc", ApiVersion)
    )

  def makeRoutes() = {
    // Now create the http4s routes
    val fnDef: String => IO[Either[Unit, String]] = apiHandlers.countCharacters
    import sttp.tapir.server.http4s.Http4sServerInterpreter
    val svrLogic1: ServerEndpoint[Fs2Streams[IO], IO] = findByIdEndpoint.serverLogic(apiHandlers.findById)
    val svrLogic2: ServerEndpoint[Fs2Streams[IO], IO] = countCharactersEndpoint.serverLogic(fnDef )
    val countCharactersRoutes: HttpRoutes[IO] =
      Http4sServerInterpreter[IO]().toRoutes(
        List(svrLogic1,
          svrLogic2
        )
      )

    val loggedResponses = ResponseLogger.httpRoutes(logHeaders = true, logBody = true)(countCharactersRoutes)
    val loggedRoutes = RequestLogger.httpRoutes(logHeaders = true, logBody = true)(loggedResponses)

    loggedRoutes <+> swaggerUIRoutes
  }
}
