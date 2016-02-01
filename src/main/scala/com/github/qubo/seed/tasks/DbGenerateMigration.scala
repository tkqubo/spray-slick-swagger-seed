package com.github.qubo.seed.tasks

import java.text.SimpleDateFormat
import java.util.Calendar

import org.slf4j.LoggerFactory

import scala.util.Random
import scalax.io._

object DbGenerateMigration {
  private val logger = LoggerFactory.getLogger(getClass)
  private val maxNum = 999

  def main(args: Array[String]): Unit = {
    val calendar = Calendar.getInstance()
    Random.setSeed(calendar.getTimeInMillis)
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
    val dateStr = dateFormat.format(calendar.getTime)
    args foreach { (name: String) =>
      val filename = f"./src/main/resources/db/migration/V$dateStr${Random.nextInt(maxNum)}%03d__$name.sql"
      logger.info(s"Generate a migration file: $filename")
      Resource.fromFile(filename).write("")
    }
  }
}
