package com.github.qubo.seed.utils

import com.typesafe.config.ConfigFactory
import slick.jdbc.{JdbcBackend, JdbcDataSource}
import slick.util.ClassLoaderUtil


object Config {
  private val config =  ConfigFactory.load()

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