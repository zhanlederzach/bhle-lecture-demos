package week9

import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Tag}
import week9.models.{Hero, Item}

class HeroesTable(tag: Tag) extends Table[Hero](tag, "HEROES") {
  // This is the primary key column:
  def heroId: Rep[Int] = column[Int]("HERO_ID", O.PrimaryKey, O.AutoInc)
  def heroName: Rep[String] = column[String]("HERO_NAME")

  def * : ProvenShape[Hero] = (heroId.?, heroName) <> (Hero.tupled, Hero.unapply)
}

class ItemsTable(tag: Tag) extends Table[Item](tag, "ITEMS") {
  // This is the primary key column:
  def itemId: Rep[Int] = column[Int]("ITEM_ID", O.PrimaryKey, O.AutoInc)
  def itemName: Rep[String] = column[String]("ITEM_TYPE")

  def * : ProvenShape[Item] = (itemId.?, itemName) <> (Item.tupled, Item.unapply)
}