package graph

import org.scalatest.FunSpec
import org.mockito.BDDMockito._
import org.scalatest.mock.MockitoSugar._
import facebook.{NetworkAnalyzer, User, GraphApi}

class NetworkAnalyzerSpec extends FunSpec {
  val me = User("0", "me")
  val user1 = User("1", "user1")
  val user2 = User("2", "user2")
  val user3 = User("3", "user3")
  val user4 = User("4", "user4")
  val user5 = User("5", "user5")


  describe("Centrality ranking") {
    val api = mock[GraphApi]

    it("small network") {
      given(api.myself()) willReturn me

      given(api.fetchFriends()) willReturn Set(user1, user2, user3, user4, user5)

      given(api.areFriends("1", "3")) willReturn true
      given(api.areFriends("1", "5")) willReturn true
      given(api.areFriends("2", "3")) willReturn true
      given(api.areFriends("2", "4")) willReturn true
      given(api.areFriends("2", "5")) willReturn true
      given(api.areFriends("3", "4")) willReturn true

      val analyzer = new NetworkAnalyzer(api)

      val ranking = analyzer.centralityRanking()

      assert(ranking(0) == me)
      assert((ranking drop 1 take 2).toSet == Set(user2, user3))
      assert((ranking drop 3).toSet == Set(user1, user4, user5))
    }
  }
}
