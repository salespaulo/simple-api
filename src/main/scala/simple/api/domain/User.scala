package simple.api.domain

import simple.api.infra.orm.Schema._

import scala.util.Try

case class User(id: Long, email: String, password: String, age: Int)

case class UserFollower(userId: Long, followerId: Long)

trait Following {
  import ctx._

  def insertFollower(userId: Long, followerId: Long) = for {
    u <- User.get(userId)
    f <- User.get(followerId)
    _ <- Try(run(followersdb.insert)(List(UserFollower(userId, followerId)))).toOption
  } yield {
    (u, f)
  }

  def followers(userId: Long): Seq[User] = run(quote {
    for {
      userAndFollowers <- usersdb.join(followersdb).on((u, f) => u.id == f.followerId).filter(_._2.userId == lift(userId))
    } yield {
      userAndFollowers._1
    }
  })

}

object User extends Following {
  import ctx._

  def list: Seq[User] = run(usersdb.sortBy(_.email))

  def get(id: Long): Option[User] = run(usersdb.filter(_.id == lift(id))).headOption

  def update(user: User): Option[Long] = run(usersdb.filter(_.id == lift(user.id)).update)(List(user)).headOption

  def insert(user: User): Option[Long] = run(usersdb.insert)(List(user)).headOption

  def delete(id: Long): Try[Long] = Try(run(usersdb.filter(_.id == lift(id)).delete))
}
