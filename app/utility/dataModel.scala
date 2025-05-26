package utility
import scala.xml.XML
import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import scala.collection.mutable

case class SimplePosition(identifier: VATCategory, netAmount: Double) // Stores positions with only the information the tax summary needs later on

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
    vatIdentifier: String
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
    vatGroups: List[InvoiceVATGroup],
    paymentTerms: String = "",
    vatExemptionReason: String = ""
)

case class VATCategory(
    vatCode: String,
    vatRate: Double
)

case class InvoiceVATGroup(
    identifier: VATCategory,
    positions: List[SimplePosition],
    vatExemptionReason: String = ""
)

case class InvoicePosition(
    id: String,
    name: String,
    vatCode: String,
    vatRate: Double,
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

val allowedCodes: Set[String] = Set("A", "B", "C") // or read from the same file frontend is using

enum allowedCodes2 {
    case A()
    case B()
    case C()
}  // Can an Enum be parsed from a file at runtime?

final case class Example2(value1: Double, value2: String)

sealed trait ErrorMessageHandling {
    def errorMessage: String
}

case object ValueIsZero extends ErrorMessageHandling {
    def errorMessage: String = "value1 can not be 0"
}

case object ValueNotInCodelist extends ErrorMessageHandling {
    def errorMessage: String = "given value for value2 was not found in the codelist"
}

sealed trait InputValidator {
    def validateValue1(value1: Double): Validated[Seq[ErrorMessageHandling], Double] =
        Validated.cond(
            !(value1 == 0),
            value1,
            Seq(ValueIsZero)
            )
    def validateValue2(value2: String): Validated[Seq[ErrorMessageHandling], String] =
        Validated.cond(
            allowedCodes.contains(value2),
            value2,
            Seq(ValueNotInCodelist)
        )
    def ValidateFormData(value1: Double, value2: String): Validated[Seq[ErrorMessageHandling], Example2] = {
            (validateValue1(value1),
            validateValue2(value2)).mapN(Example2.apply)
        }
}

object InputValidator extends InputValidator

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
