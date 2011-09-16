organization := "com.example"

name := "github-commit-meow"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
   "net.databinder" %% "unfiltered-filter" % "0.5.0",
   "net.databinder" %% "unfiltered-jetty" % "0.5.0",
   "net.databinder" %% "unfiltered-json" % "0.5.0",
   "org.clapper" %% "avsl" % "0.3.1",
   "me.lessis" %% "meow" % "0.1.1"
)

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2",
  "less is" at "http://repo.lessis.me"
)
