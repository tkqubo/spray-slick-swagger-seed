package com.github.qubo.router

import akka.actor.{ActorContext, Actor, ActorLogging}
import com.github.qubo.SwaggerDefinition
import spray.http.MediaTypes._
import spray.routing.HttpService

import scala.reflect.runtime.universe.{Type, typeOf}


class ApiRouterActor extends Actor with ActorLogging with HttpService {
  def actorRefFactory: ActorContext = context

  def receive: Receive = runRoute(
    get {
      pathPrefix("api") {
        path("") {
          complete("OK")
        }
      } ~
      path("swagger.json") {
        respondWithMediaType(`application/json`) {
          complete {
            new SwaggerDefinition {
              override val types: Seq[Type] = Seq(typeOf[ProductApi])
            }.prettyJson
          }
        }
      } ~
        getFromDirectory("public") ~
        pathPrefix("webjars") {
          getFromResourceDirectory("META-INF/resources/webjars")
        }
    }
  )

}
