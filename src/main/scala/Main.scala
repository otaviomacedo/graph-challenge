import facebook.{NetworkAnalyzer, GraphApi}
import graph.Graph
import java.util.Scanner
import scala.io.Source

object Part1 {
  def makeEdge(line: String): (Long, Long) = try {
    val labels = line.split("\\s+")
    (labels(0).toLong, labels(1).toLong)
  } catch {
    case e: Exception =>
      println(s"Wrong data format at: $line")
      throw e
  }

  def main(args: Array[String]) {
    val graph = try {
      print("File path: ")
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
}

object Part2 {
  def main(args: Array[String]) {
    print("Token: ")
    val sc = new Scanner(System.in)
    val analyzer = new NetworkAnalyzer(new GraphApi(sc.nextLine()))
    val ranking = analyzer.centralityRanking()
    println(ranking.mkString("\n"))
  }
}

