package utility
import scala.xml.XML


case class Invoice(
    metadata: InvoiceMetaData,
    seller: InvoiceSeller,
    sellerContact: InvoiceSellerContact,
    buyer: InvoiceBuyer,
    positions: List[InvoicePosition],
    paymentInformation: InvoicePaymentInformation
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
    websitelink: String,
    email: String,
    identifier: String = ""
    )

case class InvoiceSellerContact(
    name: String,
    telephonenumber: String,
    email: String
    )

case class InvoiceBuyer(
    reference: String,
    name: String,
    postcode: String,
    city: String,
    country: String,
    iban: String,
    email: String
    )

case class InvoicePaymentInformation(
    currencycode: String,
    paymentMeansCode: String,
    paymentTerms: String = ""
)

case class InvoicePosition(
    id: String,
    name: String,
    VATcategoryCode: String,
    data: InvoicePositionData
)

enum InvoicePositionData{
    case Stundenposition(
        hours: Double,
        hourlyrate: Double
    )
    case Leistungsposition(
        quantity: Double,
        pricePerPart: Double,
        measurementCode: String
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
