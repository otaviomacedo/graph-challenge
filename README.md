graph-challenge
===============

Setup
----------------

To build and run this code, you will need Scala's Simple Build Tool. Download and install it according to the [instructions][1] and then run `sbt` at the root directory of the project. In the console, to run the first part of the challenge, just type `runMain Part1`. It will prompt the user for the path of the file containing the graph definition.

Similarly, to run the second part, type `runMain Part2` and it will ask for the Facebook access token. To get one, go to the [API graph explorer][2] and copy it from the text box on the top.

System design
-------------

The code base is divided into two modules, each in its own package:

  * `graph`: its only class, `Graph`, contains the graph computation logic for this challenge: closeness and "farness" measures as well as the ranking computation for the graph.
  * `facebook`: provides an abstraction for interacting with Facebook's REST API and also a network analyzer, built on top of the `Graph` and the `GraphApi` classes. The analyzer applies the centrality metrics to the Facebook network of a certain user.

In the `Graph` class, the most substantial piece of code is dedicated to computing the shortest paths for every pair of vertices in the graph. This is done by an implementation of the [Floyd-Warshall algorithm][3], whose computational complexity is O(|V|³), where |V| is the number of vertices. 

The ranking corresponds to the list of vertices, sorted by -1/farness, that is, from the vertex with the greater closeness to the one with the lesser closeness. But the same result can be obtained by sorting it ascendingly by the farness measure, since both functions have the same monotonicity (only the latter is slightly faster).

For the Facebook part, there are three public methods: `fetchFriends`, `areFriends` and `myself`, which basically delegate to the REST API methods in the Graph API.

The `NetworkAnalyzer` class computes the centrality ranking of a user's network in two steps. First, it fetches the list of friends of the logged in user and adds all these connections to the graph. Then, for each pair of friends, it checks wether they are friends with each other or not. Pairs of friends are added to the graph. The ranking of this graph is returned.

[1]: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
[2]: https://developers.facebook.com/tools/explorer
[3]: http://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm