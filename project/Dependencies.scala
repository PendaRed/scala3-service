import sbt._

object Dependencies {
  private val ScalaTestVersion = "3.2.9"
  private val ScalaMockVersion = "5.1.0"
  private val TypesafeConfigVersion = "1.4.0"
  private val LogbackVersion = "1.2.3"
  private val ScalaLoggingVersion = "3.9.4"
  private val Fs2Version = "3.2.4"
  private val CirceVersion = "0.14.1"
  private val PostgresSqlVersion = "42.2.14"
  private val Http4sVersion = "0.23.7"
  private val TapirSwaggerVersion = "0.20.0-M6"

  val ConfigDependencies: Seq[ModuleID] = Seq(
    "com.typesafe" % "config" % TypesafeConfigVersion)

  // Multi project build file.  For val xxx = project, xxx is the name of the project and base dir
  val CommonSettingsDependencies: Seq[ModuleID] = Seq(
    "org.scala-lang" % "scala3-library_3" % "3.0.0",
    "org.scala-lang" % "scala3-compiler_3" % "3.0.0",
    "org.scalatest" % "scalatest_3" % ScalaTestVersion % Test,
    "com.typesafe.scala-logging" % "scala-logging_3" % ScalaLoggingVersion,
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "ch.qos.logback" % "logback-core" % LogbackVersion,
    "org.slf4j" % "slf4j-api" % "1.7.30"
  )

  val Fs2Dependencies: Seq[ModuleID] = Seq(
    "co.fs2" %% "fs2-core" % Fs2Version,
    "co.fs2" %% "fs2-io" % Fs2Version,
    "co.fs2" %% "fs2-reactive-streams" % Fs2Version,
    "org.typelevel" %% "cats-effect" % "3.3.4",
  )

  val JsonCircelibraryDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % CirceVersion)

  val ServiceDependencies = Seq(
    "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s"      %% "http4s-circe"        % Http4sVersion,
    "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirSwaggerVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirSwaggerVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirSwaggerVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-cats" % TapirSwaggerVersion
  )

  val SvcCommonDependencies: Seq[ModuleID] = Seq(
    "org.postgresql" % "postgresql" % PostgresSqlVersion,
    "com.zaxxer" % "HikariCP" % "3.4.5",
  ) ++ ConfigDependencies ++ CommonSettingsDependencies ++ JsonCircelibraryDependencies ++ Fs2Dependencies ++ ServiceDependencies

}
