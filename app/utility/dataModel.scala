package utility

import codelists._

import scala.xml.XML
import scala.collection.mutable

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class Invoice(
    metadata: InvoiceMetaData,
    involvedParties: InvoiceInvolvedParties,
    positions: List[InvoicePosition],
    paymentInformation: InvoicePaymentInformation)

case class InvoiceMetaData(
    number: String,
    date: String,
    typ: String)

case class InvoiceInvolvedParties(
    seller: InvoiceSeller,
    sellerContact: InvoiceSellerContact,
    buyer: InvoiceBuyer)

case class InvoiceSeller(
    name: String,
    street: String,
    address: Address,
    telephonenumber: String,
    websitelink: String,
    email: String,
    vatIdentifier: String)

case class InvoiceSellerContact(
    name: String,
    telephonenumber: String,
    email: String)

case class InvoiceBuyer(
    reference: String,
    name: String,
    address: Address,
    iban: String,
    email: String)

case class VATCategoryIdentifier(
    vatCode: String,
    vatRate: Double)

case class InvoiceVATGroup(
    identifier: VATCategoryIdentifier,
    positions: List[SimplePosition],
    vatExemptionReason: String = "")

case class InvoicePaymentInformation(
    currencycode: String,
    paymentMeansCode: String,
    vatGroups: List[InvoiceVATGroup],
    paymentTerms: String = "")

case class SimplePosition(
    identifier: VATCategoryIdentifier,
    netAmount: Double) // Stores positions with only the information the tax summary needs later on

case class InvoicePosition(
    id: String,
    name: String,
    vatId: VATCategoryIdentifier,
    data: InvoicePositionData)

enum InvoicePositionData{
    case Stundenposition(
        hours: Double,
        hourlyrate: Double
    )
    case Leistungsposition(
        quantity: Double,
        pricePerPart: Double,
        measurementCode: String
    )}

case class Address (
    postCode: String,
    city: String,
    countryCode: CountryCode)


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
