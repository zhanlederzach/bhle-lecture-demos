package week8

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import week8.actors.{PurchaseModel, Shop}
import week8.model.{ItemModel, ItemModelOnlyId}
import akka.pattern.ask
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

trait ShopRoutes extends JsonSupport {
  def shop: ActorRef
  implicit val timeout = Timeout(30, TimeUnit.SECONDS)

  val managerRoutes = pathPrefix("manager") {
    concat(
      path("addItem") {
        post {
          entity(as[ItemModel]) { itemModel =>
            complete {
              (shop ? Shop.AddItem(itemModel.id, itemModel.name, itemModel.count, itemModel.price))
                .mapTo[Shop.Response]
            }
          }
        }
      },
      path("getItems") {
        get {
          complete {
            (shop ? Shop.GetItems()).mapTo[Shop.Items]
          }
        }
      },
      path("removeItem") {
        delete {
          entity(as[ItemModelOnlyId]) { itemModel =>
            complete {
              (shop ? Shop.RemoveItem(itemModel.id)).mapTo[Shop.Response]
            }
          }
        }
      }
    )
  }

  val customerRoutes = pathPrefix("customer") {
    concat(
      path("purchase") {
        post {
          entity(as[PurchaseModel]) { purchase =>
            complete {
              (shop ? Shop.PurchaseItem(purchase.id, purchase.count, purchase.money)).mapTo[Shop.PurchaseResult]
            }
          }
        }
      },
      path("getItems") {
        get {
          complete {
            (shop ? Shop.GetItems()).mapTo[Shop.Items]
          }
        }
      }
    )
  }



}
