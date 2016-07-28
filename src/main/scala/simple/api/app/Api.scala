package simple.api.app

import com.twitter.finagle.Http
import com.twitter.util.{Base64StringEncoder, Await}
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import simple.api.UserApi

object Api extends App {

  private def endpoints = ( UserApi.endpoints )
  Await.ready( Http.server.serve(":8080", endpoints.toService) )

}
