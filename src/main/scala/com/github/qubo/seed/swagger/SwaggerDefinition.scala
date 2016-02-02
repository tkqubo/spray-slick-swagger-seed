package com.github.qubo.seed.swagger

import java.util

import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.ReaderConfig
import io.swagger.models.Swagger
import io.swagger.util.Json
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.reflect.runtime.universe.Type

class SwaggerDefinition(config: SwaggerDefinitionConfig) {
  protected val logger = LoggerFactory.getLogger(getClass)
  protected val swaggerConfig: Swagger = new Swagger()
    .scheme(config.scheme)
    .basePath(config.basePath)
    .host(config.port.map(port => s"${config.host}:$port").getOrElse(config.host))
    .info(config.info)

  protected val reader = new Reader(swaggerConfig, new ReaderConfig() {
    override def getIgnoredRoutes: util.Collection[String] = List()
    override def isScanAllResources: Boolean = true
  })

  lazy val swagger: Swagger = reader.read(
    config.types
      .map(getClassNameForType)
      .map(Class.forName).toSet
  )

  def prettyJson: String =
    Json.pretty(swagger)

  def compactJson: String =
    Json.mapper().writeValueAsString(swagger)

  private def getClassNameForType(t: Type): String = {
    val typeSymbol = t.typeSymbol
    val fullName = typeSymbol.fullName
    if (typeSymbol.isModuleClass) {
      val idx = fullName.lastIndexOf('.')
      if (idx >= 0) {
        val mangledName = s"${fullName.slice(0, idx)}$$${fullName.slice(idx + 1, fullName.length)}$$"
        mangledName
      } else {
        fullName
      }
    } else {
      fullName
    }
  }
}
