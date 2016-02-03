package com.github.qubo.seed.routes


import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http._
import spray.routing._
import spray.util._

trait CorsHelper {
  this: HttpService =>
  val MaxAge = 1728000

  def p3p(value: String = """CP="CAO PSA OUR""""): Directive0 =
    mapHttpResponseHeaders(_ ++ List(RawHeader("P3P", value)))

  def cors(origins: Seq[String]): Directive0 =
    cors(SomeOrigins(origins.map(HttpOrigin(_))))

  def cors(origin: String): Directive0 =
    if (origin == "*") {
      cors(AllOrigins)
    } else {
      cors(SomeOrigins(Seq(HttpOrigin(origin))))
    }

  def cors(origins: AllowedOrigins): Directive0 = mapRequestContext { ctx =>
    val originHeader = if (origins == AllOrigins) {
      ctx.request.headers.findByType[`Origin`]
    } else {
      None
    }
    val resOrigins = originHeader.map { case Origin(origin) => SomeOrigins(origin) }.getOrElse(origins)
    ctx.withRouteResponseHandling({
      //It is an option request for a resource that responds to some other method
      case Rejected(x) if ctx.request.method.equals(HttpMethods.OPTIONS) =>
        ctx.complete(HttpResponse().withHeaders(
          allowOriginHeader(resOrigins) ++ fixedCorsHeaders
        ))
    }).withHttpResponseHeadersMapped { headers =>
      allowOriginHeader(resOrigins) ++ fixedCorsHeaders ++ headers
    }
  } & p3p()

  private def fixedCorsHeaders: List[HttpHeader] =
    List(
      `Access-Control-Allow-Methods`(GET, POST, PATCH, PUT, DELETE, OPTIONS),
      `Access-Control-Allow-Headers`("Content-Type,Accept-Language,Authorization"),
      `Access-Control-Allow-Credentials`(true),
      `Access-Control-Max-Age`(MaxAge)
    )

  private def allowOriginHeader(origins: AllowedOrigins): List[HttpHeader] =
    List(`Access-Control-Allow-Origin`(origins))
}
