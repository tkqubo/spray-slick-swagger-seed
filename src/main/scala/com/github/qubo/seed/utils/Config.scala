package com.github.qubo.seed.utils

import scalaz._,Scalaz._
import com.typesafe.config.ConfigFactory
import slick.jdbc.{JdbcBackend, JdbcDataSource}
import slick.util.ClassLoaderUtil


object Config {
  val environment = Option(System.getProperty("environment")).getOrElse("localhost")
  private val config =  ConfigFactory.parseResources(s"$environment.conf")

  object app {
    val appConf = config.getConfig("app")

    val systemName = appConf.getString("systemName")
    val interface = appConf.getString("interface")
    val port = appConf.getInt("port")
    val userServiceName = appConf.getString("userServiceName")
  }


  object dbConfig {
    lazy val dbConfig = config.getConfig("db")
    lazy val ds: JdbcDataSource = JdbcDataSource.forConfig(dbConfig, null, "master", ClassLoaderUtil.defaultClassLoader)
    val db = JdbcBackend.Database.forSource(ds)
  }
}