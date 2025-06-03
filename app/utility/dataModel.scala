package utility

import codelists._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class Invoice(
    metadata: InvoiceMetaData,
    involvedParties: InvoiceInvolvedParties,
    positions: List[InvoicePosition],
    paymentInformation: InvoicePaymentInformation)
object Invoice {
    def validate(metadata: Validated[Seq[ErrorMessage], InvoiceMetaData], involvedParties: Validated[Seq[ErrorMessage], InvoiceInvolvedParties], positions: List[Validated[Seq[ErrorMessage], InvoicePosition]], paymentInformation: Validated[Seq[ErrorMessage], InvoicePaymentInformation]): Validated[Seq[ErrorMessage], Invoice] = {
        (
            metadata,
            involvedParties,
            positions.sequence,
            paymentInformation
        ).mapN(Invoice.apply)
    }
}

case class InvoiceMetaData(
    identifier: InvoiceIdentifier,
    date: Date,
    invoiceType: InvoiceTypeCode)
object InvoiceMetaData {
    def validate(number: String, date: String, typ: String): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
        (
            InvoiceIdentifier.validate(number),
            Date.validate(date),
            InvoiceTypeCode.validate(typ)
        ).mapN(InvoiceMetaData.apply)
    }
}

case class InvoiceInvolvedParties(
    seller: InvoiceSeller,
    sellerContact: InvoiceSellerContact,
    buyer: InvoiceBuyer)
object InvoiceInvolvedParties {
    def validate(seller: Validated[Seq[ErrorMessage], InvoiceSeller], sellerContact: Validated[Seq[ErrorMessage], InvoiceSellerContact], buyer: Validated[Seq[ErrorMessage], InvoiceBuyer]): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        (
            seller,
            sellerContact,
            buyer
        ).mapN(InvoiceInvolvedParties.apply)
    }
}

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
    reference: BuyerReference,
    name: BuyerName,
    address: Address,
    iban: Iban,
    email: Email)
object InvoiceBuyer {
    def validate(reference: String, name: String, address: Validated[Seq[ErrorMessage], Address], iban: String, email: String): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        (
            BuyerReference.validate(reference),
            BuyerName.validate(name),
            address,
            Iban.validate(iban),
            Email.validate(email)
        ).mapN(InvoiceBuyer.apply)
    }
}

case class VATCategoryIdentifier(
    vatCode: String,
    vatRate: Double)

case class InvoiceVATGroup(
    identifier: VATCategoryIdentifier,
    positions: List[SimplePosition],
    vatExemptionReason: String = "")

case class InvoicePaymentInformation(
    currencycode: CurrencyCode,
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
    postCode: PostCode,
    city: City,
    countryCode: CountryCode)
object Address {
    def validate(postCode: String, city: String, countrycode: String): Validated[Seq[ErrorMessage], Address] = {
        (
        PostCode.validate(postCode),
        City.validate(city),
        CountryCode.validate(countrycode)
        ).mapN(Address.apply)
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
