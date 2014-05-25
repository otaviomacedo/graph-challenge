package facebook

import graph.Graph

class NetworkAnalyzer(api: GraphApi) {
  /**
   * Returns a ranking of users based on their centrality value.
   */
  def centralityRanking(): List[User] = {
    val me = api.myself()
    val friends = api.fetchFriends()

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
      case (f1, f2) => ((f1, f2), api.areFriends(f1.id, f2.id))
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