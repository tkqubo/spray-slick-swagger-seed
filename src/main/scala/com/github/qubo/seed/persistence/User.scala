package com.github.qubo.seed.persistence

import io.swagger.annotations.ApiModel
import slick.dbio.Effect.Read
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape
import slick.profile.SqlAction
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext


@ApiModel("user")
case class User(name: String, email: String)

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def name: Rep[String] = column[String]("name")
  def email: Rep[String] = column[String]("email")
  // scalastyle:off method.name
  override def * : ProvenShape[User] =
  // scalastyle:on method.name
    (email, name).shaped <> ((User.apply _).tupled, User.unapply)
}

object User {
  val tableQuery = TableQuery[UserTable]
  def selectAll(implicit ec: ExecutionContext): DBIOAction[Seq[User], NoStream, Read] = tableQuery.result
  def selectByName(name: String)(implicit ec: ExecutionContext): DBIOAction[Option[User], NoStream, Read] =
    tableQuery.filter(_.name === name).result.headOption

  implicit val userJsonFormat = jsonFormat2(User.apply)
}


