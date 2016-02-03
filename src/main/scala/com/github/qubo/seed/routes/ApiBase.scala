package com.github.qubo.seed.routes

import org.slf4j.LoggerFactory
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.routing.HttpService

import scala.concurrent.ExecutionContext

trait ApiBase
  extends HttpService
  with SprayJsonSupport
  with DefaultJsonProtocol {
  implicit def ec: ExecutionContext
  protected val logger = LoggerFactory.getLogger(getClass)
}
