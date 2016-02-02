package com.github.qubo.router

import java.util.NoSuchElementException

import akka.actor.{Actor, ActorContext, ActorLogging}
import com.github.qubo.seed.swagger.{SwaggerDefinition, SwaggerDefinitionConfig}
import spray.http.MediaTypes._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.routing.{ExceptionHandler, HttpService, Route}

import scala.concurrent.ExecutionContext
import scala.reflect.runtime.universe.Type
import scala.reflect.runtime.universe.typeOf

class ApiRouterActor
  extends Actor
  with ActorLogging
  with DefaultJsonProtocol
  with SprayJsonSupport
  with UserApi
  with HttpService {
  def actorRefFactory: ActorContext = context

  private def apiTypes: Seq[Type] = Seq(
    typeOf[UserApi]
  )

  def receive: Receive = runRoute(
    respondWithMediaType(`application/json`) {
      apiRoute ~ swaggerRoute
    } ~ swaggerUiRoute
  )

  private def apiRoute: Route =
    (path("") & get & complete("OK")) ~
    userRoute

  private def swaggerRoute: Route =
    (path("swagger.json") & get & complete) {
      new SwaggerDefinition(SwaggerDefinitionConfig(types = apiTypes)).prettyJson
    }

  private def swaggerUiRoute: Route =
    get {
      path("swagger") {
        getFromResource("swagger-ui/index.html")
      } ~
        getFromResourceDirectory("swagger-ui")
    }

  implicit val exceptionHandler =
    ExceptionHandler {
      case e: NoSuchElementException =>
        complete(StatusCodes.NotFound)
      case x =>
        complete(StatusCodes.InternalServerError -> x.getMessage)
    }
}
