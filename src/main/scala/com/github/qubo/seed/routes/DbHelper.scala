package com.github.qubo.seed.routes

import com.github.qubo.seed.utils.Config
import slick.dbio.Effect.All
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.jdbc.JdbcBackend
import spray.http.{HttpData, HttpEntity, MediaTypes}
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.{Marshaller, MarshallingContext}
import spray.json.{DefaultJsonProtocol, JsonWriter, _}
import spray.routing.HttpService

import scala.concurrent.ExecutionContext


trait DbHelper
  extends HttpService
  with DefaultJsonProtocol
  with SprayJsonSupport {
  implicit val db: JdbcBackend.DatabaseDef
  implicit val ec: ExecutionContext

  private def marshalWithDBIO[A: JsonWriter, E <: Effect](database: JdbcBackend.DatabaseDef): Marshaller[DBIOAction[A, NoStream, E]] =
    Marshaller[DBIOAction[A, NoStream, E]] { (value: DBIOAction[A, NoStream, E], ctx: MarshallingContext) =>
      database.run(value)
        .map(implicitly[JsonWriter[A]].write)
        .map { value => ctx.marshalTo(HttpEntity(MediaTypes.`application/json`, HttpData(value))) }
        .recover { case error: Throwable => ctx.handleError(error) }
    }

  implicit def writeDBIOActionMarshaller[A: JsonWriter, E <: Effect]: Marshaller[DBIOAction[A, NoStream, E]] =
    marshalWithDBIO[A, E](db)

  implicit def writeStatusCodeDBIOActionMarshaller[A <: Unit]: Marshaller[DBIOAction[A, NoStream, All]] =
    Marshaller[DBIOAction[A, NoStream, All]] { (value: DBIOAction[A, NoStream, All], ctx: MarshallingContext) =>
      db.run(value)
        .map(_ => ctx.marshalTo(HttpEntity(HttpData.Empty)))
        .recover { case error: Throwable => ctx.handleError(error) }
    }

  implicit val printer: JsonPrinter = Config.App.responseFormat match {
    case "pretty" => PrettyPrinter
    case "compact" => CompactPrinter
    case _ => PrettyPrinter
  }
}
