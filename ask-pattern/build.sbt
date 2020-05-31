name := "ask-pattern"

version := "0.1"

scalaVersion := "2.13.2"

scalacOptions ++= Seq("-language:postfixOps")

val akkaVersion = "2.5.30"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.1.0"
)
