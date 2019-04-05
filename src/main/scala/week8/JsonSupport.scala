package week8

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import week8.actors.{PurchaseModel, Shop}
import week8.model.{ItemModel, ItemModelOnlyId}

trait JsonSupport {
  implicit val shopResponseFormat: RootJsonFormat[Shop.Response] = jsonFormat1(Shop.Response)
  implicit val itemModelFormat: RootJsonFormat[ItemModel] = jsonFormat4(ItemModel)
  implicit val itemModelOnlyIdFormat: RootJsonFormat[ItemModelOnlyId] = jsonFormat1(ItemModelOnlyId)
  implicit val shopItemsFormat: RootJsonFormat[Shop.Items] = jsonFormat1(Shop.Items)
  implicit val purchaseModelFormat: RootJsonFormat[PurchaseModel] = jsonFormat3(PurchaseModel)
  implicit val shopPurchaseResult: RootJsonFormat[Shop.PurchaseResult] = jsonFormat1(Shop.PurchaseResult)
}
