package com.github.qubo.seed.tasks

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory

object DbMigrate extends App {
  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()
  val flyway = new Flyway()

  flyway.setDataSource(config.getString("db.url"), config.getString("db.user"), config.getString("db.password"))
  flyway.setOutOfOrder(true)
  flyway.migrate()
}
