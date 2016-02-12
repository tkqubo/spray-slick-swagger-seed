package com.github.qubo.seed.utils

import com.github.qubo.seed.swagger.SwaggerDefinitionConfig
import com.typesafe.config.ConfigFactory
import io.swagger.models.{Info, Scheme}
import net.ceedubs.ficus.Ficus._
import slick.jdbc.{JdbcBackend, JdbcDataSource}
import slick.util.ClassLoaderUtil

import scala.reflect.runtime.universe.Type
import scalaz.Scalaz._


object Config {
  val environment = Option(System.getProperty("environment")).getOrElse("localhost")
  private val config = ConfigFactory.parseResources(s"$environment.conf").resolve()

  object App {
    private val appConf = config.getConfig("app")
    val systemName: String = appConf.as[String]("systemName")
    val userServiceName: String = appConf.as[String]("userServiceName")
    val scheme: Scheme = appConf.as[Option[String]]("scheme").map(Scheme.forValue).flatMap(Option(_)).getOrElse(Scheme.HTTP)
    val interface: String = appConf.as[String]("interface")
    val port: Int = appConf.as[Int]("port")
    val responseFormat: String = appConf.as[Option[String]]("responseFormat").getOrElse("")

    val applicationName = appConf.as[String]("applicationName")
    val applicationVersion = appConf.as[String]("applicationVersion")

    def swaggerDefinitionConfig(apiTypes: Seq[Type]): SwaggerDefinitionConfig =
      SwaggerDefinitionConfig(
        types = apiTypes,
        scheme = scheme,
        host = interface,
        port = port.some,
        info = new Info().title(applicationName).version(applicationVersion)
      )
  }

  object Database {
    private lazy val dbConfig = config.getConfig("db")
    // scalastyle:off null
    private lazy val ds: JdbcDataSource = JdbcDataSource.forConfig(dbConfig, null, "master", ClassLoaderUtil.defaultClassLoader)
    // scalastyle:on null
    val db: JdbcBackend.DatabaseDef = JdbcBackend.Database.forSource(ds)
  }
}