package utility.validation

import codelists._
import sharedUtility.ValidateAble._
import sharedUtility.error._
import sharedUtility.utility._

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
    name: Name,
    street: Street,
    address: Address,
    telephonenumber: TelephoneNumber,
    websitelink: WebsiteLink,
    email: Email,
    vatIdentifier: SellerVATIdentifier)
object InvoiceSeller {
    def validate(name: String, street: String, address: Validated[Seq[ErrorMessage], Address], telephonenumber: String, websitelink: String, email: String, vatIdentifier: String): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        (
            Name.validate(name),
            Street.validate(street),
            address,
            TelephoneNumber.validate(telephonenumber),
            WebsiteLink.validate(websitelink),
            Email.validate(email),
            SellerVATIdentifier.validate(vatIdentifier)
        ).mapN(InvoiceSeller.apply)
    }
}

case class InvoiceSellerContact(
    name: Name,
    telephonenumber: TelephoneNumber,
    email: Email)
object InvoiceSellerContact {
    def validate(name: String, telephonenumber: String, email: String): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        (
            Name.validate(name),
            TelephoneNumber.validate(telephonenumber),
            Email.validate(email)
        ).mapN(InvoiceSellerContact.apply)
    }
}

case class InvoiceBuyer(
    reference: BuyerReference,
    name: Name,
    address: Address,
    iban: Iban,
    email: Email)
object InvoiceBuyer {
    def validate(reference: String, name: String, address: Validated[Seq[ErrorMessage], Address], iban: String, email: String): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        (
            BuyerReference.validate(reference),
            Name.validate(name),
            address,
            Iban.validate(iban),
            Email.validate(email)
        ).mapN(InvoiceBuyer.apply)
    }
}

case class VATCategoryIdentifier(
    vatCode: VATCategoryCode,
    vatRate: VATRate)
object VATCategoryIdentifier {
    def validate(vatCode: String, vatRate: String): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        (
            VATCategoryCode.validate(vatCode),
            VATRate.validate(vatRate)
        ).mapN(VATCategoryIdentifier.apply)
    }
}
case class InvoiceVATGroup(
    id: VATCategoryIdentifier,
    positions: List[SimplePosition],
    vatExemptionReason: VATExemptionReason)
object InvoiceVATGroup {
    def validate(id: Validated[Seq[ErrorMessage], VATCategoryIdentifier], positions: List[Validated[Seq[ErrorMessage], SimplePosition]], vatExemptionReason: String= ""): Validated[Seq[ErrorMessage], InvoiceVATGroup] = {
        (
            id,
            positions.sequence,
            VATExemptionReason.validate(vatExemptionReason)
        ).mapN(InvoiceVATGroup.apply)
    }
}

case class InvoicePaymentInformation(
    currencycode: CurrencyCode,
    paymentMeansCode: PaymentMeansTypeCode,
    vatGroups: List[InvoiceVATGroup],
    paymentTerms: PaymentTerms)
object InvoicePaymentInformation {
        def validate(currencycode: String, paymentMeansCode: String, vatGroups: List[Validated[Seq[ErrorMessage], InvoiceVATGroup]], paymentTerms: String = ""): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        (
            CurrencyCode.validate(currencycode),
            PaymentMeansTypeCode.validate(paymentMeansCode),
            vatGroups.sequence,
            PaymentTerms.validate(paymentTerms)
        ).mapN(InvoicePaymentInformation.apply)
    }
}

case class SimplePosition(
    identifier: VATCategoryIdentifier,
    quantity: Quantity,
    netPrice: NetPrice,
    netAmount: NetAmount) // Stores positions with only the information the tax summary needs later on
object SimplePosition {
    def validate(identifier: Validated[Seq[ErrorMessage], VATCategoryIdentifier], quantity: String, netPrice: String): Validated[Seq[ErrorMessage], SimplePosition] = {
        (
            identifier,
            Quantity.validate(quantity),
            NetPrice.validate(netPrice),
            NetAmount.validate(quantity, netPrice)
        ).mapN(SimplePosition.apply)
    }
}

case class InvoicePosition(
    id: PositionID,
    name: PositionName,
    vatId: VATCategoryIdentifier,
    data: InvoicePositionData)
object InvoicePosition {
    def validate(id: String, name: String, vatId: Validated[Seq[ErrorMessage], VATCategoryIdentifier], data: Validated[Seq[ErrorMessage], InvoicePositionData]): Validated[Seq[ErrorMessage], InvoicePosition] = {
        (
            PositionID.validate(id),
            PositionName.validate(name),
            vatId,
            data
        ).mapN(InvoicePosition.apply)
    }
}

enum InvoicePositionData{
    case Stundenposition(
        hours: Hours,
        hourlyrate: HourlyRate
    )
    case Leistungsposition(
        quantity: Quantity,
        pricePerPart: NetPrice,
        measurementCode: MeasurementCode
    )}
object Stundenposition {
    def validate(hours: String, hourlyrate: String): Validated[Seq[ErrorMessage], InvoicePositionData.Stundenposition] = {
        (
            Hours.validate(hours),
            HourlyRate.validate(hourlyrate)
        ).mapN(InvoicePositionData.Stundenposition.apply)
    }

}
object Leistungsposition {
    def validate(quantity: String, pricePerPart: String, measurementCode: String): Validated[Seq[ErrorMessage], InvoicePositionData.Leistungsposition] = {
        (
            Quantity.validate(quantity),
            NetPrice.validate(pricePerPart),
            MeasurementCode.validate(measurementCode)
        ).mapN(InvoicePositionData.Leistungsposition.apply)
    }
}

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

case class Date(
    year: Year,
    month: Month,
    day: Day
)
object Date {
    def validate(date: String): Validated[Seq[ErrorMessage], Date] = {
        if (IsValidDateFormat(date)) {
            (
                Year.validate(date.slice(0,4)),
                Month.validate(date.slice(4,6)),
                Day.validate(date.slice(6,8))
            ).mapN(Date.apply)
        } else {
            Invalid(Seq(ArgumentError(makeError("Das angebene Datum entspricht nicht dem geforderten Format YYYYMMDD.", date))))
        }
    }
    def get(date: Date): String = {
        date.year.get.toString + fillWithZero(date.month.get.toString) + fillWithZero(date.day.get.toString)
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
