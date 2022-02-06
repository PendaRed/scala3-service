import sbt.Keys.libraryDependencies

lazy val commonSettings = Seq(
  organization := "com.jgibbons",
  version := "0.1.1-SNAPSHOT",
  autoScalaLibrary := false,
  scalaVersion := "3.1.0",
  libraryDependencies ++= Dependencies.SvcCommonDependencies
)

lazy val SvcCommon = project
  .in(file("svc-common"))
  .settings(commonSettings:_*)
  .settings(
    name := "svc-common",
  )


lazy val SvcFirst = project
  .in(file("svc-first"))
  .settings(commonSettings:_*)
  .settings(
    name := "svc-first",
  ) dependsOn SvcCommon

lazy val Scala3Service = (project in file("."))
  .aggregate(SvcCommon, SvcFirst)