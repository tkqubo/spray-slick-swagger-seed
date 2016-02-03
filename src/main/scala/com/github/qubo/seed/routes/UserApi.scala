package com.github.qubo.seed.routes

import javax.ws.rs.{GET, Path, PathParam}

import com.github.qubo.seed.persistences.User
import com.github.qubo.seed.utils.Config
import io.swagger.annotations._
import spray.routing.{Route, StandardRoute}

@Api("user api")
trait UserApi extends ApiBase {
  def userRoute: Route =
    path("users") {
      handleGetUsers
    } ~
    path("users" / Segment) { name =>
      get {
        handleGetUser(name)
      }
    }

  @GET @Path("/users/{name}")
  @(ApiOperation)(value = "sample endpoint", response = classOf[User])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "User not found"),
    new ApiResponse(code = 400, message = "Invalid user name supplied")
  ))
  def handleGetUser(@PathParam("name") name: String): StandardRoute =
    complete {
      Config.dbConfig.db.run(User.selectByName(name))
        .map(_.getOrElse(throw new NoSuchElementException()))
    }

  @GET @Path(value = "/users")
  @ApiOperation(value = "sample endpoint", response = classOf[User], responseContainer = "List")
  def handleGetUsers: StandardRoute =
    complete {
      Config.dbConfig.db.run(User.selectAll)
    }
}

