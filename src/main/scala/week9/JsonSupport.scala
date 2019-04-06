package week9

import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._
import week9.models.{Hero, Heroes, Response}

trait JsonSupport {
  implicit val heroFormat: RootJsonFormat[Hero] = jsonFormat2(Hero)
  implicit val heroesFormat: RootJsonFormat[Heroes] = jsonFormat1(Heroes)
  implicit val responseFormat: RootJsonFormat[Response] = jsonFormat1(Response)
}
