package utility
import scala.xml.XML


case class Invoice(
    metadata: InvoiceMetaData,
    seller: InvoiceSeller,
    sellerContact: InvoiceSellerContact,
    buyer: InvoiceBuyer,
    positions: List[InvoicePosition]
    )

case class InvoiceMetaData(
    number: String,
    date: String,
    typ: String
    )

case class InvoiceSeller(
    name: String,
    street: String,
    postcode: String,
    city: String,
    country: String,
    telephonenumber: String,
    faxnumber: String,
    websitelink: String,
    email: String
    )

case class InvoiceSellerContact(
    name: String,
    telephonenumber: String,
    email: String
    )

case class InvoiceBuyer(
    name: String,
    postcode: String,
    city: String,
    country: String,
    iban: String,
    email: String
    )

trait BasePosition {
    def id: String
    def taxpercentage: Double
}

enum InvoicePosition extends BasePosition {
    case Stundenposition(
        id: String,
        taxpercentage: Double,
        hours: Double,
        hourlyrate: Double
    )
    case Leistungsposition(
        id: String, 
        taxpercentage: Double,
        amount: Double,
        quantity: Double
    )
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
