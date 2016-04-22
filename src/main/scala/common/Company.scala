package common

import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json}

/**
 * Created by kenneththomas on 4/21/16.
 */
case class Competitor(name: String, uid: String, link: Option[String] = None)

object Competitor {
  implicit val jsonFormat = Json.format[Competitor]
}

case class Company(name: String, uid: String, businessEntity: Option[String] = None, competitors: List[Competitor] = List.empty, isDone: Boolean, repeatCount: Option[Int] = None, createdOn: DateTime = utils.Utils.currentUTCDateTime, updatedOn: Option[DateTime] = None)

object Company {
  implicit val jsonFormat = Json.format[Company]
}

case class CompanyData(uid: String, pageContent: Option[String] = None)

object CompanyData {
  implicit val jsonFormat = Json.format[CompanyData]
}

case class NoCompany(q: String, isDone: Boolean, createdOn: DateTime = utils.Utils.currentUTCDateTime)

object NoCompany {
  implicit val jsonFormat = Json.format[NoCompany]
}

case class AppError(sectionName: String, q: String, ex: String, createdOn: DateTime = utils.Utils.currentUTCDateTime)

object AppError {
  implicit val jsonFormat = Json.format[AppError]
}

case class ScrapedData(uid: String, jsObj: JsObject)

object ScrapedData {
  implicit  val jsonFormat = Json.format[ScrapedData]
}

