package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

case class PostCode private(postcode: String)
object PostCode {
    def validate(postcode: String): Validated[Seq[ErrorMessage], PostCode] = {
        Validated.cond(
            postcode != "FEHLERTEST",
            PostCode(postcode),
            Seq(ArgumentError)
        )
    }
}

case class City private(city: String)
object City {
    def validate(city: String): Validated[Seq[ErrorMessage], City] = {
        Validated.cond(
            true,
            City(city),
            Seq(ArgumentError)
        )
    }
}

case class BuyerReference(ref: String)
object BuyerReference {
    def validate(ref: String): Validated[Seq[ErrorMessage], BuyerReference] = {
        Validated.cond(
            true,
            BuyerReference(ref),
            Seq(ArgumentError)
        )
    }
}

case class Name(name: String)
object Name{
    def validate(name: String): Validated[Seq[ErrorMessage], Name] = {
        Validated.cond(
            true,
            Name(name),
            Seq(ArgumentError)
        )
    }
}

case class Iban(iban: String)
object Iban{
    def validate(iban: String): Validated[Seq[ErrorMessage], Iban] = {
        Validated.cond(
            true,
            Iban(iban),
            Seq(ArgumentError)
        )
    }
}

case class Email(email: String)
object Email {
    def validate(email: String): Validated[Seq[ErrorMessage], Email] = {
        Validated.cond(
            true,
            Email(email),
            Seq(ArgumentError)
        )
    }
}

case class InvoiceIdentifier(id: String)
object InvoiceIdentifier {
    def validate(id: String): Validated[Seq[ErrorMessage], InvoiceIdentifier] = {
        Validated.cond(
            true,
            InvoiceIdentifier(id),
            Seq(ArgumentError)
        )
    }
}

case class Date(date: String)
object Date {
    def validate(date: String): Validated[Seq[ErrorMessage], Date] = {
        Validated.cond(
            true,
            Date(date),
            Seq(ArgumentError)
        )
    }
}

case class InvoiceTypeCode(typeCode: String)
object InvoiceTypeCode {
    def validate(typeCode: String): Validated[Seq[ErrorMessage], InvoiceTypeCode] = {
        Validated.cond(
            true,
            InvoiceTypeCode(typeCode),
            Seq(ArgumentError)
        )
    }
}

case class PaymentMeansCode(code: String)
object PaymentMeansCode {
        def validate(code: String): Validated[Seq[ErrorMessage], PaymentMeansCode] = {
        Validated.cond(
            true,
            PaymentMeansCode(code),
            Seq(ArgumentError)
        )
    }
}

case class PaymentTerms(terms: String)
object PaymentTerms {
        def validate(terms: String): Validated[Seq[ErrorMessage], PaymentTerms] = {
        Validated.cond(
            true,
            PaymentTerms(terms),
            Seq(ArgumentError)
        )
    }
}

case class TelephoneNumber(number: String)
object TelephoneNumber {
    def validate(number: String): Validated[Seq[ErrorMessage], TelephoneNumber] = {
        Validated.cond(
            true,
            TelephoneNumber(number),
            Seq(ArgumentError)
        )
    }
}

case class WebsiteLink(link: String)
object WebsiteLink {
    def validate(link: String): Validated[Seq[ErrorMessage], WebsiteLink] = {
        Validated.cond(
            true,
            WebsiteLink(link),
            Seq(ArgumentError)
        )
    }
}

case class Street(street: String)
object Street {
    def validate(street: String): Validated[Seq[ErrorMessage], Street] = {
        Validated.cond(
            true,
            Street(street),
            Seq(ArgumentError)
        )
    }
}

case class SellerVATIdentifier(id: String)
object SellerVATIdentifier {
    def validate(id: String): Validated[Seq[ErrorMessage], SellerVATIdentifier] = {
        Validated.cond(
            true,
            SellerVATIdentifier(id),
            Seq(ArgumentError)
        )
    }
}

case class VATCategoryCode(code: String)
object VATCategoryCode {
    def validate(code: String): Validated[Seq[ErrorMessage], VATCategoryCode] = {
        Validated.cond(
            true,
            VATCategoryCode(code),
            Seq(ArgumentError)
        )
    }
}

case class VATRate(rate: Double)
object VATRate {
    def validate(rate: String): Validated[Seq[ErrorMessage], VATRate] = {
        Validated.cond(
            true,  // Check here if the String can be converted to a Double
            VATRate(rate.toDouble),
            Seq(ArgumentError)
        )
    }
}

case class Quantity(quantity: Double)
object Quantity {
    def validate(quantity: String): Validated[Seq[ErrorMessage], Quantity] = {
        Validated.cond(
            true,
            Quantity(quantity.toDouble),
            Seq(ArgumentError)
        )
    }
}

case class NetPrice(netPrice: Double)
object NetPrice {
    def validate(netPrice: String): Validated[Seq[ErrorMessage], NetPrice] = {
        Validated.cond(
            true,
            NetPrice(netPrice.toDouble),
            Seq(ArgumentError)
        )
    }
}

case class NetAmount(amount: Double)
object NetAmount {
    def validate(quantity: String, netPrice: String): Validated[Seq[ErrorMessage], NetAmount] = {
        Validated.cond(
            true,
            NetAmount(quantity.toDouble * netPrice.toDouble),
            Seq(ArgumentError)
        )
    }
}

case class VATExemptionReason(reason: String)
object VATExemptionReason {
    def validate(reason: String): Validated[Seq[ErrorMessage], VATExemptionReason] = {
        Validated.cond(
            true,
            VATExemptionReason(reason),
            Seq(ArgumentError)
        )
    }
}

case class PositionID(id: String)
object PositionID {
    def validate(id: String): Validated[Seq[ErrorMessage], PositionID] = {
        Validated.cond(
            true,
            PositionID(id),
            Seq(ArgumentError)
        )
    }
}

case class PositionName(name: String)
object PositionName {
    def validate(name: String): Validated[Seq[ErrorMessage], PositionName] = {
        Validated.cond(
            true,
            PositionName(name),
            Seq(ArgumentError)
        )
    }
}

case class Hours(hours: Double)
object Hours {
    def validate(hours: String): Validated[Seq[ErrorMessage], Hours] = {
        Validated.cond(
            true,
            Hours(hours.toDouble),
            Seq(ArgumentError)
        )
    }
}

case class HourlyRate(rate: Double)
object HourlyRate {
    def validate(hourlyrate: String): Validated[Seq[ErrorMessage], HourlyRate] = {
        Validated.cond(
            true,
            HourlyRate(hourlyrate.toDouble),
            Seq(ArgumentError)
        )
    }
}
