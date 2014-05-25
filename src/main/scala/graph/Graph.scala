package graph

import scala.math.min

/**
 * Representation of an immutable, undirected and unweighted graph.
 * @param vertices Set of vertices.
 * @param edges Set of edges.
 * @tparam V Type of the vertices.
 */
case class Graph[V](vertices: Set[V] = Set.empty[V], edges: Set[(V, V)] = Set.empty[(V, V)]) {

  /**
   * Adds a new edge to this graph.
   * @param edge The edge to be added.
   * @return a new Graph with the additional edge.
   */
  def +(edge: (V, V)): Graph[V] = Graph(vertices.+(edge._1, edge._2), edges + edge)

  /*
  Since f(x) = 1/x is a strictly decreasing function, we can replace it by a
  less expensive function that is also strictly decreasing. In this case,
  f(x) = -x. And since the goal here is to build a ranking, we can use its
  invers, f(x) = x, to generate a list sorted from the most central to the less
  one.
   */
  lazy val ranking = vertices.toList sortBy farness

  def closeness(vertex: V): Double = 1.0 / farness(vertex)

  def farness(vertex: V): Long = {
    val seq = (List.empty[Long] /: vertices) {
      (list, v) => (lengths get(vertex, v) getOrElse Infinity) :: list
    }
    seq.sum
  }

  private val Infinity: Long = Long.MaxValue / 2

  private lazy val lengths = {
    var pathLengths: Map[(V, V), Long] = Map()

    def distance(i: V, j: V): Long =
      pathLengths.get((i, j)) orElse pathLengths.get((j, i)) getOrElse Infinity

    // Floyd­Warshall algorithm
    for {
      i <- vertices
      j <- vertices
    } {
      val weight: Long =
        if (i == j) 0
        else if (edges.contains((i, j)) || edges.contains((j, i))) 1
        else Infinity
      pathLengths += ((i, j) -> weight)
    }

    for {
      k <- vertices
      i <- vertices
      j <- vertices
    } {
      val current = distance(i, j)
      val potentiallyBetter = distance(i, k) + distance(k, j)
      pathLengths = pathLengths + ((i, j) -> min(current, potentiallyBetter))
    }

    pathLengths
  }
}