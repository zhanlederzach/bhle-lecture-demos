package week8

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import akka.http.scaladsl.server.Directives._
import week8.actors.Shop

object Boot extends App with ShopRoutes {

  val log = LoggerFactory.getLogger(Boot.getClass.getSimpleName)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  val shop = system.actorOf(Shop.props(), "shop")

  val route = pathPrefix("shop") {
    concat(
      managerRoutes,
      customerRoutes
    )
  }


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  log.info("Listening on port 8080...")
}
