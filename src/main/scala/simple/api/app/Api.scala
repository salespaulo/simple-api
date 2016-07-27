package simple.api.app

import com.twitter.util.Await
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import com.twitter.finagle.Http
import simple.api.UserApi

object Api extends App with UserApi {
  Await.ready(
    Http.server.serve(":8080", (
      listUsers :+:
        getUserById
      ).toService)
  )
}