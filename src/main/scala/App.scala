package com.example

import unfiltered.request._
import unfiltered.response._
import net.liftweb.json._
import meow._
import util.control.Exception.allCatch

import org.clapper.avsl.Logger

/** unfiltered plan */
class App extends unfiltered.filter.Plan {
  import QParams._

  implicit val formats = DefaultFormats
  val logger = Logger(classOf[App])

  def intent = {
    case req @ POST(Path(_)) =>
      logger.debug("POST")
      val payload = decode(Body.string(req).drop("payload=".size))      
      
      for {
        json <- JsonParser.parseOpt(payload)
        repository <- Some((json \ "repository"))
        repo <- (repository \ "name").extractOpt[String]
        owner <- (repository \ "owner" \ "name").extractOpt[String]
      } {
        val commits = for {                
          commit <- (json \ "commits").children
          author <- (commit \ "author" \ "name").extractOpt[String].toList
          id <- (commit \ "id").extractOpt[String].toList
          message <- (commit \ "message").extractOpt[String].toList
        } yield "%s: %s (%s)".format(id.take(6), message, author)

        val body = commits.mkString("\n")
        logger.info("Incoming: %d commits to %s/%s" format (commits.size, owner, repo))
        Growl.title("Push to %s/%s" format (owner, repo)).message(body).meow
      }       
      Ok
  }

  def decode(encoded: String) = {
    import java.net.URLDecoder
    URLDecoder.decode(encoded, "UTF-8") 
  }
}
    

/** embedded server */
object Server {
  val logger = Logger(Server.getClass)
  def main(args: Array[String]) {
    val port = allCatch opt { args(0).toInt } getOrElse 9090
    val http = unfiltered.jetty.Http(port) // this will not be necessary in 0.4.0
    http.filter(new App).run()
  }
}
