import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import sbt.Keys._
import sbt._

object Build extends sbt.Build {
  val ScalaVersion = "2.11.7"
  val akkaVersion = "2.3.11"
  val akkaHttpV = "2.0.3"
  val sprayVersion = "1.3.3"
  val specs2Version = "3.7"
  val slickVersion = "3.1.1"
  val swaggerVersion = "1.5.6"
  val slf4jVersion = "1.7.14"

  val baseSettings = Defaults.defaultConfigs ++ Seq(
    scalaVersion := ScalaVersion,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    libraryDependencies ++= Seq(
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
      "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
      "io.strongtyped" %% "active-slick" % "0.3.3",
      "mysql" % "mysql-connector-java" % "5.1.34",
      "org.scalaz" %% "scalaz-core" % "7.2.0",
      "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
      "org.flywaydb" % "flyway-core" % "3.2.1",
      "net.ceedubs" %% "ficus" % "1.1.2"
    ),
    mainClass in Compile := Some("com.github.qubo.seed.Boot"),
    fork in Global := true,
    javaOptions in Test += "-Xmx2048m"
  )

  lazy val dbGenerateMigration = inputKey[Unit]("Generate a migration file")
  lazy val dbMigrate = taskKey[Unit]("Migrate the datebase")
  lazy val dbClean = taskKey[Unit]("Clean the datebase")

  private val _organization = "com.github.qubo"
  private val _name = "spray-slick-swagger-seed"
  private val _projectPackage = "seed"

  lazy val taskSettings = Seq(
    fullRunInputTask(dbGenerateMigration, Runtime, s"${_organization}.${_projectPackage}.tasks.DbGenerateMigration"),
    fullRunTask(dbMigrate, Runtime, s"${_organization}.${_projectPackage}.tasks.DbMigrate"),
    fullRunTask(dbClean, Runtime, s"${_organization}.${_projectPackage}.tasks.DbClean")
  )

  lazy val root: Project = Project(
    _name,
    file("."),
    settings = baseSettings ++ taskSettings ++ Seq(
      organization := _organization,
      name := _name,
      version := "0.1.0-SNAPSHOT"
    )
  )
    .enablePlugins(JavaServerAppPackaging)
}
