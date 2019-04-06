package week9

import akka.actor.{Actor, Props}
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._
import week9.models.{Hero, Heroes}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Dota {

  case class CreateHero(name: String)

  case object GetHeroes

  def props(db: MySQLProfile.backend.Database) = Props(new Dota(db))
}

class Dota(db: MySQLProfile.backend.Database) extends Actor {

  import Dota._

  val heroesTable = TableQuery[HeroesTable]
  val itemsTable = TableQuery[ItemsTable]

  val heroes: Seq[Hero] = Await.result(db.run(heroesTable.result), 5.seconds)
  val items = Await.result(db.run(itemsTable.result), 5.seconds)


  override def receive: Receive = {
    case CreateHero(newName) =>
    sender() ! db.run(
      heroesTable += Hero(name = newName)
    )


    case GetHeroes =>
      sender() ! Heroes(heroes)

  }
}
