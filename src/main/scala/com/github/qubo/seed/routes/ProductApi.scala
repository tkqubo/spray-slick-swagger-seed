package com.github.qubo.seed.routes

import javax.ws.rs.Path

import io.swagger.annotations._

@Api("product api")
@Path(value = "/pet")
trait ProductApi {
  @(ApiOperation)(value = "sample endpoint", response = classOf[Product], httpMethod = "GET")
  @Path(value = "{petId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "petId", value = "ID of pet that needs to be fetched", required = true, dataType = "integer", paramType = "path", allowableValues="1,100000")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Pet not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def sample: Int = 3 * 8
}
