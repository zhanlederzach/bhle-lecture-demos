package week8.actors

import akka.actor.{Actor, ActorLogging, Props}
import week8.actors.Item.{GetItem, Purchase, SetPrice}
import week8.model.ItemModel

object Item {
  def props(id: String, name: String, count: Int, price: Double) = Props(new Item(id, name, count, price))

  case class SetPrice(price: Double)
  case class Purchase(count: Int, money: Double)

  case class GetItem()

}

class Item(id: String, name: String, var count: Int, var price: Double) extends Actor with ActorLogging {
  override def receive: Receive = {
    case SetPrice(newPrice) => {
      this.price = newPrice
    }
    case Purchase(count, money) => {
      val totalPrice = count * this.price
      val result = money - totalPrice

      this.count -= count

      sender() ! Shop.OnPurchase(this.id, result)
    }
    case GetItem() => {
      sender() ! Shop.GotItem(ItemModel(this.id, this.name, this.count, this.price))

    }
  }
}
