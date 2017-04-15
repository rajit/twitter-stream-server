scalaVersion := "2.12.1"

val http4sVersion = "0.15.7"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.2",
  "com.danielasfregola" %% "twitter4s" % "5.0",
  "io.circe" %% "circe-generic" % "0.6.1",
  "org.spire-math" %% "jawn-parser" % "0.10.4"
)
