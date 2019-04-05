package week8

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import org.slf4j.LoggerFactory
import week8.Bot.Start
import week8.actors.Shop
import week8.actors.Shop._

object Bot {
  def props(): Props = Props(new Bot())
  case class Start()
}

class Bot extends Actor with ActorLogging {

  val shop = context.actorOf(Shop.props(), "shop")

  def waitItemPurchase(): Receive = {
    case Shop.PurchaseResult(money) => {
      log.info(s"$money")
    }
  }

  override def receive = {
    case Start() => {
      shop ! Shop.AddItem("1", "Moloko", 1, 12)
      shop ! Shop.AddItem("2", "Hleb", 2, 36)

      shop ! Shop.SetPrice("1", 10)
      shop ! Shop.PurchaseItem("1", 1, 99)
      context.become(waitItemPurchase())
    }
  }
}

object Main extends App {
  val system = ActorSystem("system")

  val bot = system.actorOf(Bot.props(), "bot")

  bot ! Bot.Start()
}