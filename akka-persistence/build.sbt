name := "akka-persistence"

version := "0.1"

scalaVersion := "2.13.2"

lazy val akkaVersion = "2.6.6"
lazy val leveldbVersion = "0.7"
lazy val leveldbjniVersion = "1.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-persistence" % akkaVersion,
  // local levelDB stores
  "org.iq80.leveldb"            % "leveldb"          % leveldbVersion,
  "org.fusesource.leveldbjni"   % "leveldbjni-all"   % leveldbjniVersion,
)
