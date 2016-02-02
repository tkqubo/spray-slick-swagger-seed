package com.github.qubo.seed.swagger

import io.swagger.models.{Info, Scheme}

import scala.reflect.runtime.universe.Type
import scalaz.syntax.std.option._

case class SwaggerDefinitionConfig(
  types: Seq[Type],
  basePath: String = SwaggerDefinitionConfig.defaultBasePath,
  host: String = SwaggerDefinitionConfig.defaultHost,
  port: Option[Int] = SwaggerDefinitionConfig.defaultPort.some,
  info: Info = new Info(),
  description: String = "",
  scheme: Scheme = SwaggerDefinitionConfig.defaultScheme
)

object SwaggerDefinitionConfig {
  val defaultBasePath: String = "/"
  val defaultHost: String = "localhost"
  val defaultPort: Int = 8000
  val defaultScheme: Scheme = Scheme.HTTP
}
