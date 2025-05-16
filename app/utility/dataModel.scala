package utility
import scala.xml.XML


case class Invoice(metadata: InvoiceMetaData, seller: Seller, buyer: Buyer, positions: List[InvoicePosition])

case class InvoiceMetaData(number: String, date: String)
case class Seller(name: String, street: String, postcode: String, city: String, telephonenumber: String, faxnumber: String, websitelink: String, email: String)
case class Buyer(name: String, address: String, iban: String)

enum InvoicePosition {
    case Stundenposition(id: String, currencode: String, hours: Float, hourlyrate: Float, taxpercentage: Float)
    case Leistungsposition(id: String, currencode: String, taxpercentage: Float, amount: Float)
}

// Other possible ways to create an Invoice-Datamodel:

// enum Rechnungstyp {
//     case StundenAbrechung()
//     case LeistungAbrechung()
// }
// case class Invoice2(rechungstyp: Rechnungstyp, meta: InvoiceMetaData)

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// enum RechnungA {
//     case StundenAbrechung(metadata: InvoiceMetaData, seller: Seller, buyer: Buyer, positions: List[InvoicePosition])
//     case LeistungAbrechung(metadata: InvoiceMetaData, seller: Seller, buyer: Buyer, positions: List[InvoicePosition])
// }
// def ProccessRechunungZuCII(rechnung: RechnungA): scala.xml.Elem = {
//     rechnung match {
//         case RechnungA.LeistungAbrechung(meta) => ???
//         case RechnungA.StundenAbrechung(meta) => ???
//     }
// }
