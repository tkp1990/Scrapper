package utils

import common.Company
import org.joda.time.{DateTime, DateTimeZone}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.io.Source
import org.jsoup.Jsoup
import scala.collection.JavaConverters._
import scala.util.Try
import scala.concurrent.{ TimeoutException, Future }
import scala.concurrent.duration._
import scala.language.postfixOps
/**
 * Created by kenneththomas on 4/21/16.
 */
object Utils {

  def getFileContent(filename: String):Option[String] = {
    val data = Source.fromFile(filename).mkString
    if (data.isEmpty) None else Some(data)
  }


  def makeCleanText(input: String):String = {
    import scala.util.matching.Regex
    val r = new Regex("""[^a-zA-Z0-9]+""")
    r.replaceAllIn(input, "")
  }

  def saveToFile(filename: String, input: String): Try[Boolean] = {
    Try {
      val p = new java.io.PrintWriter(filename)
      try {
        p.write(input)
      } catch {
        case e: Exception =>
          e.printStackTrace()
      } finally {
        p.close()
      }
      true
    }
  }

  def getCompanyFromSearchResults(html: String):Try[List[Company]] = {
    Try {
      val doc: Document = Jsoup.parse(html)
      val comps = doc.select("input[name=comp_id]").asScala.map(x => (x.attr("value"), x.attr("title")))
      println("Company IDs - " + comps)
      comps.map(x => Company(name = x._2, uid = x._1, isDone = false)).toList
    }
  }

  def getCompetitorsFromCompanyDetail(html: String):Try[List[Company]] = {
    Try {
      val doc: Document = Jsoup.parse(html)
      doc.getElementById("competitors").select("li[class=full").asScala.map(e => {
        val uids = e.select("a[href]").asScala.map(x => (x.attr("href").split("""\?cid=""")(1))).toList
        val names = e.select("span[class=companyname]").asScala.map(x => x.text()).toList
        println("uids - " + uids + " and names - " + names)
        Company(name = names.head, uid = uids.head, isDone = false)
      }).toList
    }
  }

  def getFinancialHref(html: String): Try[String] = {
    Try{
      val doc: Document = Jsoup.parse(html)
      doc.getElementById("historicalLinks").select("a").attr("href")
    }
  }

  def currentUTCDateTime = DateTime.now(DateTimeZone.UTC)

  implicit def stackTrace(exp: Throwable): String = {
    import java.io.PrintWriter
    import java.io.StringWriter

    val sw: StringWriter = new StringWriter();
    val pw: PrintWriter = new PrintWriter(sw)
    exp.printStackTrace(pw)
    sw.toString()
  }
}

