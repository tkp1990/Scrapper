package app

import Scrapper.Scrapper
import api.MongoConfig
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import common.Constants._
import org.bson.types.ObjectId
import play.api.libs.json._

/**
 * Created by kenneththomas on 4/22/16.
 */
class App{

  implicit val idReads: Reads[ObjectId] = new Reads[ObjectId] {
    override def reads(json: JsValue): JsResult[Imports.ObjectId] = {
      json.asOpt[String] map { str =>
        if (org.bson.types.ObjectId.isValid(str))
          JsSuccess(new ObjectId(str))
        else
          JsError("Invalid ObjectId %s".format(str))
      } getOrElse (JsError("Value is not an ObjectId"))
    }
  }
  implicit val idWrites = new Writes[ObjectId] {
    def writes(oId: ObjectId): JsValue = {
      JsString(oId.toString)
    }
  }

  def main(args: Array[String]) {

  }

  def getData(s: Integer) = {
    val finalCount = MongoConfig.getCount(CA_DB, COMPANY_DATA)
    var skip, c = s
    var lastId = new ObjectId("")
    val limit = 100
    while (finalCount >= skip ) {
      println(" Count: "+skip)
      val mongoClient = MongoConfig.getMongoClient("localhost", 27017)
      try {
        val collection = MongoConfig.getCollection("datacleaning", "ZPmainCollection", mongoClient)
        if(skip == c) {
          val data = collection.find().skip(skip).limit(limit).sort(orderBy)
          skip = skip + limit
          lastId = getContent(data)
          println("Last Id "+lastId)
        } else {
          val q = "_id" $gt (lastId)
          val data = collection.find(q).limit(limit)
          skip = skip + limit
          lastId = getContent(data)
          println("Last Id: "+lastId)
        }
      } catch {
        case e: Exception =>
          println("Exception: App/getData -> "+e.getMessage)
          e.printStackTrace()
      } finally {
        mongoClient.close()
      }
    }
  }

  def getContent(data: MongoCursor): ObjectId = {
    var uidList: List[String] = List.empty[String]
    var lastId = new ObjectId("")
    for(x <- data){
      val json = Json.parse(x.toString);
      lastId = (json \ "_id" ).as[ObjectId]
      val uid = (json \ "uid").as[String].trim
      uidList = uid :: uidList
    }
    val obj = new Scrapper
    obj.scrapeList(uidList)
    lastId
  }
}

