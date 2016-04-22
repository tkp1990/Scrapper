package Scrapper


import api.MongodbConnector
import common.ScrapedData
import org.jsoup.Jsoup
import play.api.libs.json.{JsString, Json}

/**
 * Created by kenneththomas on 4/22/16.
 */
class Scrapper {

  def scrapeList(uidList: List[String]) = {
    for(x <- uidList) {
      scrape(x)
    }
  }

  def scrape(uid: String) = {
    val caScrapper = new CaScrapper
    val companyDataList = MongodbConnector.getCompanyData(uid)
    for( x <- companyDataList ) {
      try{
        val doc = Jsoup.parse(x.pageContent.get)
        var jsObj = Json.obj()
        val companyDetails = caScrapper.getCompanyDetails(doc)
        var cDetailsjs = Json.obj()
        for( x <- companyDetails.keySet) {
          val obj = Json.obj(x -> companyDetails.get(x))
          cDetailsjs = cDetailsjs ++ obj
        }
        jsObj = jsObj + ("companyDetails" -> cDetailsjs)
        val profileName = caScrapper.getProfileName(doc)
        jsObj = jsObj + ("profileName" -> JsString(profileName))
        val companySecDetails = caScrapper.getCompanyDetailsSecondry(doc)
        var companySecDetailsJs = Json.obj()
        for( x <- companySecDetails.keySet) {
          val obj = Json.obj(x -> companySecDetails.get(x))
          companySecDetailsJs = companySecDetailsJs ++ obj
        }
        jsObj = jsObj + ("companySecondaryDetails" -> companySecDetailsJs)
        val addressDetails = caScrapper.getAddressDetails(doc)
        var addressDetailsJs = Json.obj()
        for( x <- addressDetails) {
          addressDetailsJs = addressDetailsJs + (x.addrType -> JsString(x.addr))
        }
        jsObj = jsObj + ("addressDetails" -> addressDetailsJs)
        val commDetails = caScrapper.getCommunicationDetails(doc)
        var commJs = Json.obj()
        for ( x <- commDetails.keySet ) {
          val obj = Json.obj(x -> commDetails.get(x))
          commJs = commJs ++ obj
        }
        jsObj = jsObj + ("communicationDetails" -> commJs)
        val financials = caScrapper.getFiniancials(doc)
        var financialsJs = Json.obj()
        for( x <- financials.keySet) {
          val obj = Json.obj(x -> financials.get(x))
          financialsJs = financialsJs ++ obj
        }
        jsObj = jsObj + ("financials" -> financialsJs)
        val stockExDetails = caScrapper.getStockExchangeDetails(doc)
        var stockExJs = Json.obj()
        for ( x <- stockExDetails.keySet ) {
          val obj = Json.obj(x -> stockExDetails.get(x))
          stockExJs = stockExJs ++ obj
        }
        jsObj = jsObj + ("stockExchange" -> stockExJs)
        val naicDetails = caScrapper.getNaicIndustryClassification(doc)
        var naicJs = Json.obj()
        for ( x <- naicDetails.keySet ) {
          val obj = Json.obj(x -> naicDetails.get(x))
          naicJs = naicJs ++ obj
        }
        jsObj = jsObj + ("naic" -> naicJs)
        val sicDetails = caScrapper.getSicIndustryClassification(doc)
        var sicJs = Json.obj()
        for ( x <- sicDetails.keySet ) {
          val obj = Json.obj(x -> sicDetails.get(x))
          sicJs = sicJs ++ obj
        }
        jsObj = jsObj + ("sic" -> sicJs)
        val scrapedObj = ScrapedData(uid, jsObj)
        MongodbConnector.saveScrapedData(scrapedObj)
      } catch {
        case e: Exception => println("Exception while scrapping Webpage for company information" + e.getMessage)
      }
    }
  }
}
