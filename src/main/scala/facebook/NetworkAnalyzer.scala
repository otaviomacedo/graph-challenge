package facebook

import graph.Graph
import java.util.Scanner

object NetworkAnalyzer {
  /**
   * Returns a ranking of users based on their centrality value.
   * @param token An access token of a logged in user.
   * @return
   */
  def centralityRanking(token: String): List[User] = {
    val service = new GraphApi(token)
    val me = service.myself()
    val friends = service.fetchFriends()

    val friendsTree = (new Graph[User]() /: friends) {
      (graph, friend) => graph +(me, friend)
    }

    // TODO A lazy sequence of pairs would be slightly more efficient
    val allPairs = for {
      f1 <- friends
      f2 <- friends
      if f1.id < f2.id
    } yield (f1, f2)

    val friendship = allPairs.par map {
      case (f1, f2) => ((f1, f2), service.areFriends(f1.id, f2.id))
    }

    val graph = (friendsTree /: friendship) {
      (graph, relation) =>
        val (pair, areFriends) = relation
        if (areFriends) graph + pair
        else graph
    }

    graph.ranking
  }
}

