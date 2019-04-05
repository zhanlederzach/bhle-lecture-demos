package week8.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.model
import week8.actors.Shop._
import week8.model.ItemModel

object Shop {
  def props() = Props(new Shop())

  case class AddItem(id: String, name: String, count: Int, price: Double)
  case class SetPrice(id: String, price: Double)
  case class Success(message: String)
  case class PurchaseItem(id: String, count: Int, money: Double)

  case class OnPurchase(id: String, resultMoney: Double)

  case class PurchaseResult(money: Double)

  case class Response(message: String)

  case class Items(items: List[ItemModel])

  case class GetItems()

  case class GotItem(item: ItemModel)

  case class RemoveItem(id: String)

}

class Shop extends Actor with ActorLogging {

  var items = Map.empty[String, ActorRef]

  def onPurchaseReceiver(replyTo: ActorRef): Receive = {
    case OnPurchase(id, resultMoney) => {
      replyTo ! PurchaseResult(resultMoney)
      context.become(receive)
    }
  }

  def itemGetReceiver(replyTo: ActorRef, size: Int, list: List[ItemModel]): Receive = {
    case GotItem(itemModel) => {
      log.info(s"Received: $itemModel")
      if (size <= 1) {
        replyTo ! Items(list :+ itemModel)
        context.become(receive)
      } else {
        context.become(itemGetReceiver(replyTo, size - 1, list :+ itemModel))
      }
    }
  }

  override def receive: Receive = {
    case AddItem(id, name, count, price) => {
      val item = context.actorOf(Item.props(id, name, count, price), s"item-$id")
      items = items + (id -> item)
      sender() ! Response("OK!")
    }
    case SetPrice(id, price) => {
      if (items.keySet.contains(id)) {
        items(id) ! Item.SetPrice(price)
        sender() ! Shop.Success("OK!")
      } else {
        sender() ! Shop.Success("Error!")
      }
    }
    case PurchaseItem(id, count, money) => {
      if (items.keySet.contains(id)) {
        items(id) ! Item.Purchase(count, money)
        context.become(onPurchaseReceiver(sender()))
      } else {

      }
    }

    case GetItems() => {
      for (idActorRef <- items) {
        idActorRef._2 ! Item.GetItem()
      }
      context.become(itemGetReceiver(sender(), items.size, List.empty[ItemModel]))
    }

    case RemoveItem(id) => {
      items = items - id
      sender() ! Shop.Response("OK!")
    }
  }
}
