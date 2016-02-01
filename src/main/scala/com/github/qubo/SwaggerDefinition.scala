package com.github.qubo

import java.util

import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.ReaderConfig
import io.swagger.models.{Info, Scheme, Swagger}
import io.swagger.util.Json

import scala.collection.JavaConversions._
import scala.reflect.runtime.universe.Type

abstract class SwaggerDefinition {
  val types: Seq[Type]
  val basePath: String = "/"
  val host: String = "localhost:8080"
  val info: Info = new Info()
  val description = ""
  val scheme: Scheme = Scheme.HTTP

  protected val swaggerConfig: Swagger = new Swagger()
    .scheme(scheme)
    .basePath(basePath)
    .host(host)
    .info(info)

  protected val reader = new Reader(swaggerConfig, new ReaderConfig() {
    override def getIgnoredRoutes: util.Collection[String] = List()
    override def isScanAllResources: Boolean = true
  })

  protected lazy val swagger = reader.read(types.map(t ⇒ {
    Class.forName(getClassNameForType(t))
  }).toSet)

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
