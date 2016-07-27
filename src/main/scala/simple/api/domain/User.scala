package simple.api.domain

import simple.api.infra.orm.Schema._

import scala.util.Try

case class User(id: Long, login: String, password: String, age: Int)

case class UserFollower(userId: Long, followerId: Long)

trait Auth {
  import ctx._

  val authorize = quote { (login: String, password: String) =>
    usersdb.filter(u => u.login == login && u.password == password).take(1)
  }

}

object User extends Auth {
  import ctx._

  def list: Seq[User] = run(usersdb.sortBy(_.login))

  def get(id: Long): Option[User] = run(usersdb.filter(_.id == lift(id))).headOption

  def update(user: User): Option[Long] = run(usersdb.filter(_.id == lift(user.id)).update)(List(user)).headOption

  def insert(user: User): Option[Long] = run(usersdb.insert)(List(user)).headOption

  def followers(userId: Long): Seq[User] = run(quote {
    for {
      userAndFollowers <- usersdb.join(followersdb).on((u, f) => u.id == f.userId).filter(_._1.id == lift(userId))
    } yield {
      userAndFollowers._1
    }
  })

  def delete(id: Long): Try[Long] = Try(run(usersdb.filter(_.id == lift(id)).delete))

}