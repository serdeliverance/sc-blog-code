name := """play-akka-stream-typed-demo"""
organization := "com.scblog"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

lazy val akkaVersion = "2.6.5"

libraryDependencies ++= Seq(
  guice,
  // streams
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "2.0.2",
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)
