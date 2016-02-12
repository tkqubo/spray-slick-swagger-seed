package com.github.qubo.seed

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.github.qubo.seed.routes.ApiRouterActor
import com.github.qubo.seed.utils.Config
import com.github.qubo.seed.utils.Config.App
import org.slf4j.LoggerFactory
import spray.can.Http

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


object Boot extends App {
  protected val logger = LoggerFactory.getLogger(getClass)

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem(App.systemName)
  implicit val ec: ExecutionContext = system.dispatcher

  // create and start our service actor
  val apiRouterActor: ActorRef = system.actorOf(Props(classOf[ApiRouterActor]), App.userServiceName)

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  (IO(Http) ? Http.Bind(apiRouterActor, interface = App.interface, port = App.port))
    .mapTo[Http.Event]
    .map {
      case Http.Bound(address) =>
        logger.info(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        logger.error(s"REST interface could not bind to ${App.interface}:${App.port}, ${cmd.failureMessage}")
        system.shutdown()
    }
}