package graph

import scala.math.min
import java.util.Scanner
import scala.io.Source

case class Graph[V](vertices: Set[V] = Set.empty[V], edges: Set[(V, V)] = Set.empty[(V, V)]) {
  def +(edge: (V, V)): Graph[V] = {
    val (v1, v2) = edge
    Graph(vertices.+(v1, v2), edges + edge)
  }

  private val Infinity: Long = Long.MaxValue / 2

  lazy val lengths = {
    var pathLengths: Map[(V, V), Long] = Map()

    def distance(i: V, j: V): Long =
      pathLengths.get((i, j)) orElse pathLengths.get((j, i)) getOrElse Infinity

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

    // Floyd­Warshall algorithm
    for {
      k <- vertices
      i <- vertices
      j <- vertices
    } {
      val distance1 = distance(i, j)
      val distance2 = distance(i, k) + distance(k, j)
      pathLengths = pathLengths + ((i, j) -> min(distance1, distance2))
    }

    pathLengths
  }

  def farness(vertex: V): Long = {
    val seq = (List.empty[Long] /: vertices) {
      (list, v) => (lengths.get((vertex, v)) getOrElse Infinity) :: list
    }

    seq.sum
  }

  def closeness(vertex: V): Double = 1.0 / farness(vertex)

  /*
  Since f(x) = 1/x is a strictly decreasing function, we can replace it by a
  less expensive function that is also strictly decreasing. In this case,
  f(x) = -x
   */
  lazy val ranking = vertices.toList.sortBy(v => -farness(v))

  lazy val cl = ranking.map(closeness)
}


object Centrality extends App {
  def makeEdge(line: String): (Long, Long) = try {
    val labels = line.split("\\s+")
    (labels(0).toLong, labels(1).toLong)
  } catch {
    case e: Exception =>
      println(s"Wrong data format at: $line")
      throw e
  }

  val graph = try {
    val sc = new Scanner(System.in)
    val filename = sc.nextLine()
    val edges = Source.fromFile(filename).getLines() map makeEdge
    (new Graph[Long]() /: edges)(_ + _)
  } catch {
    case e: Exception =>
      println(s"Could not process the graph.")
      new Graph()
  }

  println(graph.ranking)
}