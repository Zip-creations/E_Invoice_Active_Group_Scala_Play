package utility
import scala.xml.XML


case class Invoice(
    metadata: InvoiceMetaData,
    involvedParties: InvoiceInvolvedParties,
    positions: List[InvoicePosition],
    paymentInformation: InvoicePaymentInformation
    )

case class InvoiceMetaData(
    number: String,
    date: String,
    typ: String
    )

case class InvoiceInvolvedParties(
    seller: InvoiceSeller,
    sellerContact: InvoiceSellerContact,
    buyer: InvoiceBuyer
)

case class InvoiceSeller(
    name: String,
    street: String,
    address: Address,
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
    address: Address,
    iban: String,
    email: String
    )

case class InvoicePaymentInformation(
    currencycode: String,
    paymentMeansCode: String,
    paymentTerms: String = "",
    vatExemptionReason: String = ""
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

case class Address (
    postCode: String,
    city: String,
    countryCode: String
)

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Prototypes for input validation

val allowedCodes: List[String] = List.apply("A", "B", "C") // or read from the same file frontend is using

enum allowedCodes2 {
    case A()
    case B()
    case C()
}

case class Example1(value1: Double, value2: String)

object Example1 {
    def create(value1: Double, value2: String): Option[Example1] = {
        if (value1 == 0) {
            None
        } else if (allowedCodes.contains(value2)) {
            None
        } else {
            Some(Example1(value1, value2))
        }
    }
}

// Example 1 with Exception
case class Example1_5(value1: Double, value2: String)

object Example1_5 {
    def create(value1: Double, value2: String): Example1_5 = {
        if (value1 == 0) {
            throw new IllegalArgumentException("value1 can not be 0")
        } else if (allowedCodes.contains(value2)) {
            throw new IllegalArgumentException("value2 must be from aspecific code list")
        } else {
            Example1_5(value1, value2)
        }
    }
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
