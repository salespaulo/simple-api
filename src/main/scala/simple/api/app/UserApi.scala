package simple.api

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import simple.api.domain.{UserFollower, User}

trait Auth {
  val basicAuth: BasicAuth = BasicAuth("simpleapi", "123456") //c2ltcGxlYXBpMTIzNDU2
}

object UserApi extends Auth {

  def endpoints = basicAuth {
    (listUsers :+: getUserById :+: userFollowers :+: postUser :+: patchUser :+: deleteUser :+: postFollower)
  }

  val mergedUser: Endpoint[User => User] = body.as[User => User]

  val userFollowers: Endpoint[Seq[User]] = get("users" :: long :: "followers") { userId: Long =>
    Ok( User followers userId )
  }

  val postFollower: Endpoint[UserFollower] = post("users" :: long :: "followers" :: long) { (userId: Long, followerId: Long) =>
    User.insertFollower(userId, followerId).map(t => Ok(UserFollower(t._1.id, t._2.id))).getOrElse(NotFound(new Exception("error following")))
  }

  val listUsers: Endpoint[Seq[User]] = get("users") {
    Ok( User.list )
  }

  val getUserById: Endpoint[User] = get("users" :: long) { id: Long =>
    User get(id) map(u => Ok(u)) getOrElse(NotFound(new Exception("error get")))
  }

  val postUser: Endpoint[User] = post("users" :: body.as[User]) { u: User =>
    def post = for {
      newId   <- User.insert(u)
      newUser <- User.get(newId)
    } yield {
      newUser
    }

    post map(Created(_)) getOrElse(BadRequest(new Exception("error post")))
  }

  val patchUser: Endpoint[User] = patch("users" :: long :: mergedUser) { (id: Long, t: (User => User)) =>
    def patch = for {
      oldUser    <- User.get(id)
      updateId   <- User.update(t(oldUser))
      updateUser <- User.get(updateId)
    } yield {
      updateUser
    }

    patch map(Ok(_)) getOrElse(BadRequest(new Exception("error patch")))
  }

  val deleteUser: Endpoint[User] = delete("users" :: long) { id: Long =>
    def delete = for {
      userDeleted <- User.get(id)
      idDeleted   <- User.delete(id).toOption
    } yield {
      userDeleted
    }

    delete map(Ok(_)) getOrElse(BadRequest(new Exception("error delete")))
  }

}
