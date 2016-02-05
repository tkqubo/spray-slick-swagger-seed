package com.github.qubo.seed.utils

import scalaz._,Scalaz._
import com.typesafe.config.ConfigFactory
import slick.jdbc.{JdbcBackend, JdbcDataSource}
import slick.util.ClassLoaderUtil
import net.ceedubs.ficus.Ficus._


object Config {
  val environment = Option(System.getProperty("environment")).getOrElse("localhost")
  private val config = ConfigFactory.parseResources(s"$environment.conf")

  object App {
    val appConf = config.getConfig("app")
    val systemName = appConf.getString("systemName")
    val interface = appConf.getString("interface")
    val port = appConf.getInt("port")
    val userServiceName = appConf.getString("userServiceName")
    val responseFormat = appConf.as[Option[String]]("responseFormat").getOrElse("")
  }

  object Database {
    private lazy val dbConfig = config.getConfig("db")
    // scalastyle:off null
    private lazy val ds: JdbcDataSource = JdbcDataSource.forConfig(dbConfig, null, "master", ClassLoaderUtil.defaultClassLoader)
    // scalastyle:on null
    val db: JdbcBackend.DatabaseDef = JdbcBackend.Database.forSource(ds)
  }
}