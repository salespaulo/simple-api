package simple.api

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import simple.api.domain.User

trait UserApi {

  private val user = User(1, "test", "test", 1)

  val listUsers: Endpoint[Seq[User]] = get("users") {
    Ok(User.list)
  }

  val getUserById: Endpoint[User] = get("users" :: long) { id: Long =>
    User.get(id).map(u => Ok(u)).getOrElse(NotFound(new Exception("error get")))
  }

  val postUser: Endpoint[User] = post("users" :: body.as[User]) { u: User =>
    def post = for {
      newId   <- User.insert(u)
      newUser <- User.get(newId)
    } yield {
      newUser
    }

    post.map(Created(_)).getOrElse(BadRequest(new Exception("error post")))
  }

  val mergedUser: Endpoint[User => User] = body.as[User => User]

  val patchUser: Endpoint[User] = patch("users" :: long :: mergedUser) { (id: Long, t: (User => User)) =>
    def patch = for {
      oldUser    <- User.get(id)
      updateId   <- User.update(t(oldUser))
      updateUser <- User.get(updateId)
    } yield {
      updateUser
    }

    patch.map(Ok(_)).getOrElse(BadRequest(new Exception("error patch")))
  }

  val deleteUser: Endpoint[User] = delete("users" :: long) { id: Long =>
    def delete = for {
      idDeleted   <- User.delete(id).toOption
      userDeleted <- User.get(idDeleted)
    } yield {
      userDeleted
    }

    delete.map(Ok(_)).getOrElse(BadRequest(new Exception("error delete")))
  }

  val userFollowers: Endpoint[Seq[User]] = get("user" :: long :: "followers") { userId: Long =>
    Ok(Seq(user))
  }

}