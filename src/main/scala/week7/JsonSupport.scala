package week7

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import week7.actors.Library
import week7.model.ErrorInfo

trait JsonSupport {
  implicit val libraryResponseFormat: RootJsonFormat[Library.Response] = jsonFormat1(Library.Response)
  implicit val bookModelFormat: RootJsonFormat[model.Book] = jsonFormat4(model.Book)
  implicit val libraryBooksFormat = jsonFormat1(Library.Books)
  implicit val errorInfoFormat = jsonFormat2(ErrorInfo)
}
