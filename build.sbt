name := "spray-slick-swagger-seed"

version := "1.0"
scalaVersion := "2.11.7"
val environment = Option(System.getProperty("environment")).getOrElse("localhost")

javaOptions in Global ++= Seq(
  s"-Djava.util.Arrays.useLegacyMergeSort=true"
)
javaOptions in Global ++= Seq(
  s"-Denvironment=$environment",
  s"-Dconfig.file=${Option(System.getProperty("config.file")).getOrElse(s"src/main/resources/$environment.conf")}"
)
javaOptions in Test ++= Seq(
  s"-Denvironment=test",
  s"-Dconfig.file=${Option(System.getProperty("config.file")).getOrElse("src/main/resources/test.conf")}"
)
seq(flywaySettings: _*)

libraryDependencies ++= {
  val akkaVersion = "2.4.1"
  val akkaHttpV = "2.0.3"
  val sprayVersion = "1.3.3"
  val specs2Version = "3.7"
  val slickVersion = "3.1.1"
  val swaggerVersion = "1.5.6"
  val slf4jVersion = "1.7.14"
  Seq(
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.3.2",
    "io.spray" %% "spray-testkit" % sprayVersion % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaHttpV,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.typesafe" % "config" % "1.3.0",
    "io.swagger" % "swagger-core" % swaggerVersion,
    "io.swagger" % "swagger-jaxrs" % swaggerVersion,
    "io.swagger" %% "swagger-scala-module" % "1.0.0",
    "org.specs2" %% "specs2-core" % specs2Version % "test",
    "org.specs2" %% "specs2-core" % specs2Version % "test",
    "org.specs2" %% "specs2-matcher" % specs2Version % "test",
    "org.specs2" %% "specs2-matcher-extra" % specs2Version % "test",
    "org.specs2" %% "specs2-mock" % specs2Version % "test",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
    "org.slf4j" % "slf4j-nop" % slf4jVersion,
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "io.strongtyped" %% "active-slick" % "0.3.3",
    "mysql" % "mysql-connector-java" % "5.1.34",
    "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
    "org.flywaydb" % "flyway-core" % "3.2.1",
    "net.ceedubs" %% "ficus" % "1.1.2"
  )
}


fork in Test := false
parallelExecution in Test := false
Revolver.settings

fork in run := true