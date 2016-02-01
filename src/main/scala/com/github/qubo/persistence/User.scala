package com.github.qubo.persistence

import slick.dbio.Effect.Read
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext


case class User(host: String, user: String) {

}

class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def host: Rep[String] = column[String]("Host")
  def user: Rep[String] = column[String]("User")
  // scalastyle:off method.name
  override def * : ProvenShape[User] =
  // scalastyle:on method.name
    (host, user).shaped <> ((User.apply _).tupled, User.unapply)
}

object User {
  val tableQuery = TableQuery[UserTable]
  def selectAll(implicit ec: ExecutionContext): DBIOAction[Seq[User], NoStream, Read] = tableQuery.result

  implicit val userJsonFormat = jsonFormat2(User.apply)
}


