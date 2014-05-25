package graph

import org.scalatest.FunSpec

class GraphSpec extends FunSpec {
  describe("Edge addition") {
    it("should create a single edge graph from an emtpy one") {
      val graph = new Graph[Int]() +(2, 9)
      assert(graph.vertices == Set(2, 9))
      assert(graph.edges == Set((2, 9)))
    }

    it("should add a new edge and a new vertex to a non-empty graph") {
      val before = new Graph(Set(1, 2, 3), Set((2, 3), (1, 3)))
      val after = before +(1, 4)
      assert(after.vertices == Set(1, 2, 3, 4))
      assert(after.edges == Set((2, 3), (1, 3), (1, 4)))
    }

    it("should not change the graph when an existing edge is added") {
      val before = new Graph(Set(1, 2, 3), Set((2, 3), (1, 3)))
      val after = before +(1, 3)
      assert(before.edges == after.edges)
    }
  }

  describe("Centrality measures") {
    val graph = new Graph(
      Set(1, 2, 3, 4, 5),
      Set((1, 3), (1, 5), (2, 5), (2, 3), (2, 4), (3, 4)))

    it("farness function values") {
      assert(graph.farness(1) == 6)
      assert(graph.farness(2) == 5)
      assert(graph.farness(3) == 5)
      assert(graph.farness(4) == 6)
      assert(graph.farness(5) == 6)
    }

    it("closeness function values") {
      assert(graph.closeness(1) > 0.1666 && graph.closeness(1) < 0.1667)
      assert(graph.closeness(2) == 0.2)
      assert(graph.closeness(3) == 0.2)
      assert(graph.closeness(4) > 0.1666 && graph.closeness(4) < 0.1667)
      assert(graph.closeness(5) > 0.1666 && graph.closeness(5) < 0.1667)
    }
  }
}
