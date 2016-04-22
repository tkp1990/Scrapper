package Scrapper


import common.{Executives, HrefData, CompetitorObj, CaCompanyAddr}
import org.jsoup.nodes.{Element, Document}

/**
 * Created by kenneththomas on 4/22/16.
 */
class CaScrapper {

  def getCompanyDetails(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val companyDom = dom.getElementById("company")
      detailsMap = getKeyValueDataMap(companyDom)
      //detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception GetCompanyDetails: "+ e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }

  def getProfileName(dom: Document): String = {
    var companyName = ""
    try {
      val companyNameDom = dom.getElementById("profileName")
      companyName = companyNameDom.text()
    } catch {
      case e: Exception => println("Exception while getting company profile: "+e.getMessage)
        e.printStackTrace()
    }
    companyName
  }

  def getCompanyDetailsSecondry(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val companyDom = dom.getElementById("companysecondary")
      detailsMap = getKeyValueDataMap(companyDom)
      //detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception GetCompanyDetails: "+ e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }


  def getAddressDetails(dom: Document): List[CaCompanyAddr] = {
    var addrList: List[CaCompanyAddr] = List[CaCompanyAddr]()
    try{
      val addressDiv = dom.getElementById("address")
      val addrUl = addressDiv.getElementsByClass("data").listIterator()
      while (addrUl.hasNext) {
        val d = addrUl.next()
        val addrType = d.select(".label").text()
        println(addrType)
        val details = d.select(".details").text()
        println(details)
        val obj = CaCompanyAddr(addrType, details)
        addrList = obj :: addrList
      }
    } catch {
      case e: Exception => println("Exception getAddress: " + e.getMessage)
        e.printStackTrace()
    } finally {

    }
    addrList
  }

  def getCompetitors(dom: Document) = {
    var competitorList: List[CompetitorObj] = List[CompetitorObj]()
    var id = ""
    try {
      val competitors = dom.getElementById("competitors")
      val li = competitors.select(".full")
      //println(li)
      val aTags = li.select("div > a").listIterator()
      while(aTags.hasNext) {
        val data = aTags.next()
        id = getCompId(data.attr("href"))
        val c = CompetitorObj(data.text(), data.attr("href"), id, false)
        competitorList = c :: competitorList
      }
    } catch {
      case e: Exception => println("Exception while getting company profile: "+e.getMessage)
    }
    competitorList.foreach(x => println(x))
  }

  def getCommunicationDetails(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val communication = dom.getElementById("communication")
      detailsMap = getKeyValueDataMap(communication)
      detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception =>
        println("Exception getCommunicationDetails: "+e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }

  def getCompanyReportDetails(dom: Document): Map[String, HrefData] = {
    var detailsMap: Map[String, HrefData] = Map[String, HrefData]()
    try {
      val companyreports = dom.getElementById("companyreports")
      detailsMap = getKeyValueHrefData(companyreports)
      detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
    }
    detailsMap
  }

  def getBusinessDesc(dom: Document): String = {
    var desc = ""
    try{
      val businessDesc = dom.getElementById("businessdesc")
      desc = businessDesc.getElementsByClass("data").text()
    } catch {
      case e: Exception => println("Exception getBusinessDesc: "+ e.getMessage)
    }
    println(desc)
    desc
  }

  def getFiniancials(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val financialsDom = dom.getElementById("financial")
      detailsMap = getKeyValueDataMap(financialsDom)
      //detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
    }
    detailsMap
  }

  def getStockExchangeDetails(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val stockExchanges = dom.getElementById("stockexchanges")
      detailsMap = getKeyValueDataMap(stockExchanges)
      detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception getStockExchangeDetails: "+ e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }


  def getNaicIndustryClassification(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val naics = dom.getElementById("naics")
      detailsMap = getKeyValueDataMap(naics)
      detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }

  def getSicIndustryClassification(dom: Document): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try {
      val sic = dom.getElementById("sic")
      detailsMap = getKeyValueDataMap(sic)
      detailsMap.foreach(x => println(x))
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }

  def getExecutivesData(dom: Document): Map[String, Executives] = {
    var detailsMap: Map[String, Executives] = Map[String, Executives]()
    try {
      val executives = dom.getElementById("executives")
      val ui = executives.getElementsByClass("data").listIterator()
      val uiAlt = executives.getElementsByClass("data-alt").listIterator()
      while (ui.hasNext){
        val elem = ui.next()
        val desg = elem.getElementsByClass("col1of3").text().toLowerCase()
        val name = elem.getElementsByClass("col2of3").text().toLowerCase()
        val contact = elem.getElementsByClass("col3of3").text().toLowerCase()
        if(!detailsMap.contains(name)){
          detailsMap = detailsMap + (name -> Executives(desg, name, contact))
        }
      }
      while (uiAlt.hasNext){
        val elem = uiAlt.next()
        val desg = elem.getElementsByClass("col1of3").text().toLowerCase()
        val name = elem.getElementsByClass("col2of3").text().toLowerCase()
        val contact = elem.getElementsByClass("col3of3").text().toLowerCase()
        if(!detailsMap.contains(name)){
          detailsMap = detailsMap + (name -> Executives(desg, name, contact))
        }
      }
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
    }
    detailsMap.foreach(x => println(x))
    detailsMap
  }

  def getDirectorData(dom: Document) = {
    try {
      val directors = dom.getElementById("directors")
    } catch {
      case e: Exception => println("Exception: "+e.getMessage)
    }
  }


  /**
   * Utility Functions
   */

  def getCompId(str: String): String = {
    try {
      val d = str.split("cid=")
      d(1)
    } catch {
      case e: Exception => println("Exception: " + e.getMessage)
        ""
    }
  }

  def getKeyValueDataMap(dom: Element): Map[String, String] = {
    var detailsMap: Map[String, String] = Map[String, String]()
    try{
      val ui = dom.getElementsByClass("data").listIterator()
      val uiAlt = dom.getElementsByClass("data-alt").listIterator()
      while (ui.hasNext){
        val elem = ui.next()
        val dataLabel = elem.getElementsByClass("label").text().toLowerCase()
        val dataDetails = elem.getElementsByClass("details").text().toLowerCase()
        if(!detailsMap.contains(dataLabel)){
          detailsMap = detailsMap + (dataLabel -> dataDetails)
        }
      }
      while (uiAlt.hasNext){
        val elem = uiAlt.next()
        val dataLabel = elem.getElementsByClass("label").text().toLowerCase()
        val dataDetails = elem.getElementsByClass("details").text().toLowerCase()
        if(!detailsMap.contains(dataLabel)){
          detailsMap = detailsMap + (dataLabel -> dataDetails)
        }
      }
    } catch {
      case e: Exception  => e.printStackTrace()
    }
    detailsMap
  }

  def getKeyValueHrefData(dom: Element): Map[String, HrefData] = {
    var detailsMap: Map[String, HrefData] = Map[String, HrefData]()
    try{
      val ui = dom.getElementsByClass("data").listIterator()
      val uiAlt = dom.getElementsByClass("data-alt").listIterator()
      while (ui.hasNext){
        val elem = ui.next()
        val dataLabel = elem.getElementsByClass("label").text().toLowerCase()
        val dataDetails = elem.getElementsByClass("details")
        val hrefElem = dataDetails.select("a")
        val href = hrefElem.attr("href")
        val hrefLabel = hrefElem.text()
        if(!detailsMap.contains(dataLabel)){
          detailsMap = detailsMap + (dataLabel -> HrefData(hrefLabel, href))
        }
      }

      while (uiAlt.hasNext){
        val elem = uiAlt.next()
        val dataLabel = elem.getElementsByClass("label").text().toLowerCase()
        val dataDetails = elem.getElementsByClass("details")
        val hrefElem = dataDetails.select("a")
        val href = hrefElem.attr("href")
        val hrefLabel = hrefElem.text()
        if(!detailsMap.contains(dataLabel)){
          detailsMap = detailsMap + (dataLabel -> HrefData(hrefLabel, href))
        }
      }
    } catch {
      case e: Exception => println("Exception getKeyValueHrefData: "+ e.getMessage)
        e.printStackTrace()
    }
    detailsMap
  }
}
