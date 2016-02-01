package com.github.qubo.seed

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.github.qubo.seed.router.ApiRouterActor
import com.github.qubo.seed.utils.Config
import Config.app
import spray.can.Http

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem(app.systemName)
  implicit val ec: ExecutionContext = system.dispatcher

  // create and start our service actor
  val apiRouterActor: ActorRef = system.actorOf(Props(classOf[ApiRouterActor]), app.userServiceName)

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  (IO(Http) ? Http.Bind(apiRouterActor, interface = app.interface, port = app.port))
    .mapTo[Http.Event]
    .map {
      case Http.Bound(address) =>
        println(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        println("REST interface could not bind to " + s"${app.interface}:${app.port}, ${cmd.failureMessage}")
        system.terminate()
    }
}