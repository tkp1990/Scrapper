package common

import com.mongodb.casbah.Imports._

/**
 * Created by kenneththomas on 4/21/16.
 */
object Constants {

  val orderBy = MongoDBObject("_id" -> 1)
  val NAME_UNIQUE_INDEX = "NAME_UNIQUE_INDEX"
  val DB_NAME = "datacleaning"
  val COLLECTION_NAME = "ZPmainCollection"
  val LIMIT = 10000
  val CA_DB = "ca"
  val COMPANY_DATA = "company_data"
  val COMPANY = "company"

  val SUPPLIER = "seqSupplier"
  val CONSIGNEE = "seqConsignee"

  val SINGLE_LEVEL_TOKENS = "single"
  val DOUBLE_LEVEL_TOKENS = "double"
  val TRIPLE_LEVEL_TOKENS = "triple"
  val LOGISTICS = "logistics"

  val LOCALHOST = "localhost"
  val MONGODB_PORT = 27017

  val UNIQUE_DATA_DB_NAME = "uniqueData"

  //Regular Expressions

  val PRE_PROCESS_REGEX = """[-,\,,.,!,',/]""".r
}
