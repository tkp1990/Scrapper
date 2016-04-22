name := "StandAloneScraper"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.0" excludeAll ExclusionRule(organization = "jetty.servlet")
libraryDependencies += "com.sun.jersey" % "jersey-servlet" % "1.19"



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.2" ,
  "org.mongodb" %% "casbah" % "3.1.0" ,
  "com.databricks" %% "spark-csv" % "1.3.0" ,
  "com.typesafe.play" % "play-json_2.11" % "2.5.0" ,
  "com.github.tototoshi" %% "scala-csv" % "1.2.2",
  "org.jsoup" % "jsoup" % "1.8.3",
  "com.typesafe.play" % "play_2.11" % "2.5.2",
  "com.ning" % "async-http-client" % "1.7.16",
  "com.typesafe.play" % "play-ws_2.11" % "2.5.2",
  "org.elasticsearch" % "elasticsearch" % "2.3.0" excludeAll ExclusionRule(organization = "javax.servlet")
)


libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4" ,
  "com.typesafe.slick" %% "slick" % "2.1.0" ,
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.5.play24"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"