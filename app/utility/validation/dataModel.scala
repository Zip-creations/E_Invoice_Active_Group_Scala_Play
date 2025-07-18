package utility.validation

import codelists._
import sharedUtility.validation._
import sharedUtility.error._
import sharedUtility.utility._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

case class VATGroup private (
    vatID: VATCategoryIdentifier,
    positions: List[InvoicePosition],
    exemptionReason: VATExemptionReason
)
object VATGroup {
    def validate(vatID: Validated[Seq[ErrorMessage], VATCategoryIdentifier], positions: List[Validated[Seq[ErrorMessage], InvoicePosition]], reason: InputType): Validated[Seq[ErrorMessage], VATGroup] =  {
        (
            vatID,
            positions.sequence,
            VATExemptionReason.validate(reason)
        ).mapN(VATGroup.apply)
    }
}

case class Invoice private(
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

case class InvoiceMetaData private(
    identifier: InvoiceIdentifier,
    date: Date,
    invoiceType: InvoiceTypeCode)
object InvoiceMetaData {
    def validate(number: InputType, date: InputType, typ: InputType): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
        (
            InvoiceIdentifier.validate(number),
            Date.validate(date),
            InvoiceTypeCode.validate(typ)
        ).mapN(InvoiceMetaData.apply)
    }
}

case class InvoiceInvolvedParties private(
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

case class InvoiceSeller private(
    name: Name,
    street: Street,
    address: Address,
    telephonenumber: TelephoneNumber,
    websitelink: WebsiteLink,
    email: Email,
    vatIdentifier: SellerVATIdentifier)
object InvoiceSeller {
    def validate(name: InputType, street: InputType, address: Validated[Seq[ErrorMessage], Address], telephonenumber: InputType, websitelink: InputType, email: InputType, vatIdentifier: InputType): Validated[Seq[ErrorMessage], InvoiceSeller] = {
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

case class InvoiceSellerContact private(
    name: Name,
    telephonenumber: TelephoneNumber,
    email: Email)
object InvoiceSellerContact {
    def validate(name: InputType, telephonenumber: InputType, email: InputType): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        (
            Name.validate(name),
            TelephoneNumber.validate(telephonenumber),
            Email.validate(email)
        ).mapN(InvoiceSellerContact.apply)
    }
}

case class InvoiceBuyer private(
    reference: BuyerReference,
    name: Name,
    address: Address,
    iban: Iban,
    email: Email)
object InvoiceBuyer {
    def validate(reference: InputType, name: InputType, address: Validated[Seq[ErrorMessage], Address], iban: InputType, email: InputType): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        (
            BuyerReference.validate(reference),
            Name.validate(name),
            address,
            Iban.validate(iban),
            Email.validate(email)
        ).mapN(InvoiceBuyer.apply)
    }
}

case class VATCategoryIdentifier private(
    vatCode: VATCategoryCode,
    vatRate: VATRate)
object VATCategoryIdentifier {
    def validate(vatCode: InputType, vatRate: InputType): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        (
            VATCategoryCode.validate(vatCode),
            VATRate.validate(vatRate)
        ).mapN(VATCategoryIdentifier.apply)
    }
}
case class InvoiceVATGroup private(
    id: VATCategoryIdentifier,
    positions: List[SimplePosition],
    vatExemptionReason: VATExemptionReason)
object InvoiceVATGroup {
    def validate(id: Validated[Seq[ErrorMessage], VATCategoryIdentifier], positions: List[Validated[Seq[ErrorMessage], SimplePosition]], vatExemptionReason: InputType= InputType.empty): Validated[Seq[ErrorMessage], InvoiceVATGroup] = {
        (
            id,
            positions.sequence,
            VATExemptionReason.validate(vatExemptionReason)
        ).mapN(InvoiceVATGroup.apply)
    }
}

case class InvoicePaymentInformation private(
    currencycode: CurrencyCode,
    paymentMeansCode: PaymentMeansTypeCode,
    vatGroups: List[InvoiceVATGroup],
    paymentTerms: PaymentTerms)
object InvoicePaymentInformation {
        def validate(currencycode: InputType, paymentMeansCode: InputType, vatGroups: List[Validated[Seq[ErrorMessage], InvoiceVATGroup]], paymentTerms: InputType = InputType.empty): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        (
            CurrencyCode.validate(currencycode),
            PaymentMeansTypeCode.validate(paymentMeansCode),
            vatGroups.sequence,
            PaymentTerms.validate(paymentTerms)
        ).mapN(InvoicePaymentInformation.apply)
    }
}

case class SimplePosition private(
    identifier: VATCategoryIdentifier,
    quantity: Quantity,
    netPrice: NetPrice,
    netAmount: NetAmount) // Stores positions with only the information the tax summary needs later on
object SimplePosition {
    def validate(identifier: Validated[Seq[ErrorMessage], VATCategoryIdentifier], quantity: InputType, netPrice: InputType): Validated[Seq[ErrorMessage], SimplePosition] = {
        (
            identifier,
            Quantity.validate(quantity),
            NetPrice.validate(netPrice),
            NetAmount.validate(quantity, netPrice)
        ).mapN(SimplePosition.apply)
    }
}

case class InvoicePosition private(
    id: PositionID,
    name: PositionName,
    vatId: VATCategoryIdentifier,
    data: InvoicePositionData)
object InvoicePosition {
    def validate(id: InputType, name: InputType, vatId: Validated[Seq[ErrorMessage], VATCategoryIdentifier], data: Validated[Seq[ErrorMessage], InvoicePositionData]): Validated[Seq[ErrorMessage], InvoicePosition] = {
        (
            PositionID.validate(id),
            PositionName.validate(name),
            vatId,
            data
        ).mapN(InvoicePosition.apply)
    }
}

enum InvoicePositionData private{
    case Stundenposition(
        hours: Hours,
        hourlyrate: HourlyRate,
        measurementCode: MeasurementCode
    )
    case Leistungsposition(
        quantity: Quantity,
        pricePerPart: NetPrice,
        measurementCode: MeasurementCode
    )}
object Stundenposition {
    def validate(hours: InputType, hourlyrate: InputType): Validated[Seq[ErrorMessage], InvoicePositionData.Stundenposition] = {
        (
            Hours.validate(hours),
            HourlyRate.validate(hourlyrate),
            // TODO: Whats a good default for the source for a hardcoded value?  
            MeasurementCode.validate(InputType("HUR", ""))// Code for "hour" (see https://www.xrepository.de/details/urn:xoev-de:kosit:codeliste:rec20_3) ("labour hour" is LH)
        ).mapN(InvoicePositionData.Stundenposition.apply)
    }
}
object Leistungsposition {
    def validate(quantity: InputType, pricePerPart: InputType, measurementCode: InputType): Validated[Seq[ErrorMessage], InvoicePositionData.Leistungsposition] = {
        (
            Quantity.validate(quantity),
            NetPrice.validate(pricePerPart),
            MeasurementCode.validate(measurementCode)
        ).mapN(InvoicePositionData.Leistungsposition.apply)
    }
}

case class Address private(
    postCode: PostCode,
    city: City,
    countryCode: CountryCode)
object Address {
    def validate(postCode: InputType, city: InputType, countrycode: InputType): Validated[Seq[ErrorMessage], Address] = {
        (
        PostCode.validate(postCode),
        City.validate(city),
        CountryCode.validate(countrycode)
        ).mapN(Address.apply)
    }
}

case class Date private(
    year: Year,
    month: Month,
    day: Day
)
object Date {
    def validate(input: InputType): Validated[Seq[ErrorMessage], Date] = {
        val date = input.value
        if (!isValidDateFormat(date)) {
            Invalid(Seq(ArgumentError(makeError("Das angebene Datum entspricht nicht dem geforderten Format YYYYMMDD.", input))))
        } else {
            (Year.validate(InputType(date.slice(0,4), input.source)),
            Month.validate(InputType(date.slice(4,6), input.source)),
            Day.validate(InputType(date.slice(6,8), input.source))).mapN((_,_,_)).andThen{
                case (validY, validM, validD) =>
                    val year = validY.get
                    val month = validM.get
                    val day = validD.get
                    if((month == 2 || month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
                        Invalid(Seq(ArgumentError(makeError("Das Datum enthält einen unmöglichen 31. Tag.", input))))
                    } else if(month == 2 && day == 30) {
                        Invalid(Seq(ArgumentError(makeError("Das Datum enthält den 30. Februar.", input))))
                    } else if (month == 2 && day == 29 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))){
                        // this way of testing for leap years can lead to inaccuracies around the year 4813. 
                        // See https://de.wikipedia.org/wiki/Schaltjahr#Gregorianischer_Kalender
                        Invalid(Seq(ArgumentError(makeError("Das Datum enthält den 29. Februar außerhalb eines Schaltjahres.", input))))
                    } else {
                        Valid(Date(validY,validM,validD))
                    }
                }
            }
        }
    def get(date: Date): String = {
        fillWithZero(date.year.get.toString, 4) + fillWithZero(date.month.get.toString, 2) + fillWithZero(date.day.get.toString, 2)
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
