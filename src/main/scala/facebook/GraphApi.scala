package facebook

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URL
import scala.annotation.tailrec

case class User(id: String, name: String)

case class Paging(next: Option[String], previous: Option[String])

case class Response(data: List[User], paging: Paging)

/**
 * An abstraction to Facebook's Graph API, containing only the methods necessary
 * to complete this challenge.
 * @param token the access token of a logged in user.
 */
class GraphApi(token: String) {
  val GraphApiHost = "https://graph.facebook.com"
  val mapper = new ObjectMapper()
  mapper registerModule DefaultScalaModule

  def fetchFriends(): Set[User] = fetch(url("me"))

  def areFriends(id1: String, id2: String): Boolean = !fetch(url(id1, id2)).isEmpty

  def myself(): User = {
    val url = new URL(s"$GraphApiHost/me?fields=id,name&access_token=$token")
    mapper.readValue(url, classOf[User])
  }

  private def url(id1: String, id2: String): URL = url(id1, Some(id2))

  private def url(id1: String, id2: Option[String] = None): URL = {
    val other = id2 getOrElse ""
    new URL(s"$GraphApiHost/$id1/friends/$other?access_token=$token")
  }

  @tailrec
  private def fetch(url: URL, friends: Set[User] = Set.empty): Set[User] = {
    val response = mapper.readValue(url, classOf[Response])
    val next = Option(response.paging) flatMap (_.next)
    if (next.isEmpty) friends
    else fetch(new URL(next.get), friends ++ response.data)
  }
}
