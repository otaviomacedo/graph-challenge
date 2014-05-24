package graph

import scala.math.min
import java.util.Scanner
import scala.io.Source


case class Graph(vertices: Set[Long] = Set.empty, edges: Set[(Long, Long)] = Set.empty) {
  def +(edge: (Long, Long)): Graph = {
    val (e1, e2) = edge
    Graph(vertices.+(e1, e2), edges + edge)
  }

  private val numberOfVertices: Int = vertices.size
  private val range = 0L until numberOfVertices
  private val Infinity: Long = Long.MaxValue / 2

  lazy val lengths = {
    var pathLengths: Map[(Long, Long), Long] = Map()

    def distance(i: Long, j: Long): Long =
      pathLengths.get((i, j)) orElse pathLengths.get((j,i)) getOrElse Infinity

    /*
    Assuming the graph is undirected, the distance between vertices is
    commutative. So, there is only the need to compute a triangular matrix here.
     */
    for {
      i <- range
      j <- i until numberOfVertices
    } {
      val weight: Long =
        if (i == j) 0
        else if (edges.contains((i, j)) || edges.contains((j, i))) 1
        else Infinity
      pathLengths += ((i, j) -> weight)
    }

    /*
     Floyd­Warshall algorithm
     */
    for {
      k <- range
      i <- range
      j <- range
    } {
      val distance1: Long = distance(i, j)
      val distance2: Long = distance(i, k) + distance(k, j)
      pathLengths = pathLengths + ((i, j) -> min(distance1, distance2))
    }

    pathLengths
  }

  def farness(vertex: Long): Long = {
    val seq = range map {
      v => lengths get(vertex, v) getOrElse Infinity
    }
    seq.sum
  }

  def closeness(vertex: Long): Double = 1.0 / farness(vertex)

  /*
  Since f(x) = 1/x is a strictly decreasing function, we can replace it by a
  less expensive function that is also strictly decreasing. In this case,
  f(x) = -x
   */
  lazy val rank = range.sortBy(v => -farness(v))
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
    (new Graph() /: edges)(_ + _)
  } catch {
    case e: Exception =>
      println(s"Could not process the graph.")
      new Graph()
  }

  println(graph.rank)
}