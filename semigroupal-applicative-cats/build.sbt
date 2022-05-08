val scala3Version = "3.1.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "semigroupal-applicative-cats",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.7.0",
      "org.scalactic" %% "scalactic" % "3.2.11" % Test,
      "org.scalatest" %% "scalatest" % "3.2.11" % Test
    )
  )
