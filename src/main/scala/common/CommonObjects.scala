package common

/**
 * Created by kenneththomas on 4/22/16.
 */
class CommonObjects {

}

case class HrefData(label: String, href: String)

case class Executives(desg: String, name: String, contact: String)

case class CaCompanyAddr(addrType: String, addr: String)

case class CompetitorObj(compName: String, url: String, id: String, crawled: Boolean)
