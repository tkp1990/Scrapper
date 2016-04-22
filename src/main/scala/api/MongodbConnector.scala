package api

import common._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ TimeoutException, Future }
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.Await
import scala.math._
import scala.util.Try
import reactivemongo.api.{MongoConnectionOptions, MongoDriver}
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection._

import scala.math.signum

/**
 * Created by kenneththomas on 4/21/16.
 */
object MongodbConnector {
  def init() = {
    val driver = new MongoDriver
    val connection = driver.connection(
      List("mongodb:27017"),
      MongoConnectionOptions(),
      Seq()
    )
    connection.db("ca")
  }

  val getConnection = init()

  def companiesCollection: JSONCollection = getConnection.collection[JSONCollection]("company")

  def getCompanies(isDone:Boolean = false): List[Company] = {
    val sortFilter = Json.obj("createdOn" -> signum(1))
    val pageSize = 10
    val r = companiesCollection.find(Json.obj("isDone" -> isDone)).sort(sortFilter).cursor[Company]().collect[List](pageSize)
    implicit val timeout = 60.seconds
    Await.result(r, timeout)
  }

  def insertCompany(company: Company):Try[Company] = {
    Try {
      companiesCollection.find(Json.obj("uid" -> company.uid)).one[Company].flatMap {
        case Some(c) =>
          companiesCollection.update(Json.obj("uid" -> company.uid), c.copy(repeatCount = c.repeatCount.map(x => x + 1)))
        case None =>
          companiesCollection.insert(company)
      }
      company
    }
  }

  def saveCompany(company: Company):Try[Company] = {
    Try {
      companiesCollection.find(Json.obj("uid" -> company.uid)).one[Company].flatMap {
        case Some(c) =>
          val isDone = if (c.isDone) c.isDone else company.isDone
          val comps = if (c.competitors.isEmpty) company.competitors else c.competitors
          companiesCollection.update(Json.obj("uid" -> company.uid), c.copy(isDone = isDone, competitors = comps, repeatCount = c.repeatCount.map(x => x + 1), updatedOn = Some(utils.Utils.currentUTCDateTime)))
        case None =>
          companiesCollection.insert(company)
      }
      company
    }
  }

  def companyDataCollection: JSONCollection = getConnection.collection[JSONCollection]("company_data")

  def saveCompanyData(company: CompanyData):Try[CompanyData] = {
    Try {
      companyDataCollection.find(Json.obj("uid" -> company.uid)).one[CompanyData].flatMap {
        case Some(_) =>
          companyDataCollection.update(Json.obj("uid" -> company.uid), company)
        case None =>
          companyDataCollection.insert(company)
      }
      company
    }
  }

  def getCompanyData(uid: String): List[CompanyData] = {
    val sortFilter = Json.obj("createdOn" -> signum(1))
    val pageSize = 10
    val r = companiesCollection.find(Json.obj("uid" -> uid)).sort(sortFilter).cursor[CompanyData]().collect[List](pageSize)
    implicit val timeout = 60.seconds
    Await.result(r, timeout)
  }

  def noCompanyDataCollection: JSONCollection = getConnection.collection[JSONCollection]("no_company")
  def saveNoCompany(company: NoCompany):Try[NoCompany] = {
    Try {
      noCompanyDataCollection.find(Json.obj("q" -> company.q)).one[NoCompany].flatMap {
        case Some(_) =>
          noCompanyDataCollection.update(Json.obj("q" -> company.q), company)
        case None =>
          noCompanyDataCollection.insert(company)
      }
      company
    }
  }

  def errorsCollection: JSONCollection = getConnection.collection[JSONCollection]("errors")
  def saveAppError(e: AppError):Try[AppError] = {
    Try {
      errorsCollection.insert(e)
      e
    }
  }

  def scrapedDataCollection: JSONCollection = getConnection.collection[JSONCollection]("scraped_data")

  def saveScrapedData(scrapedObj: ScrapedData):Try[ScrapedData] = {
    Try {
      companyDataCollection.find(Json.obj("uid" -> scrapedObj.uid)).one[ScrapedData].flatMap {
        case Some(_) =>
          companyDataCollection.update(Json.obj("uid" -> scrapedObj.uid), scrapedObj)
        case None =>
          companyDataCollection.insert(scrapedObj)
      }
      scrapedObj
    }
  }
}

