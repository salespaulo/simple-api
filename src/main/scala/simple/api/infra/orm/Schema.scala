package simple.api.infra.orm

import io.getquill._
import simple.api.domain.{UserFollower, User}

object Schema {

  lazy val ctx = new JdbcContext[PostgresDialect, SnakeCase]("db")

  import ctx._

  implicit class ReturningId[T](a: Action[T]) {
    def returningId = quote(infix"$a RETURNING ID".as[Action[T]])
  }

  val usersdb = quote {
    query[User].schema(
      _.entity("users")
        .generated(_.id)
        .columns(
          _.login    -> "email",
          _.password -> "senha"))
  }

  val followersdb = quote {
    query[UserFollower].schema(
      _.entity("followers")
    )
  }

}
