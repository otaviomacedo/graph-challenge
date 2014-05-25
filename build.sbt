name := "Graph Challenge"

organization := "otaviomacedo"

version := "0.0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" withSources() withJavadoc(),
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1" withSources() withJavadoc(),
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3"
)

initialCommands := "import otaviomacedo.graphchallenge._"

