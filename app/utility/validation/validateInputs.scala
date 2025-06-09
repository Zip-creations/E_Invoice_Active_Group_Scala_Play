package utility.validation

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

def makeError(message: String, value: String): String = {
    s"${message} Fehlerquelle: \"${value}\""
}

abstract class ValidateAble[T](val value: T) {
    def get: T = {
        value
    }
}

case class PostCode private(postcode: String) extends ValidateAble[String](postcode)
object PostCode {
    def validate(postcode: String): Validated[Seq[ErrorMessage], PostCode] = {
        (Validated.cond(
            postcode != "FEHLERTEST",
            PostCode(postcode),
            Seq(ArgumentError(postcode))
        )).map((_) => PostCode(postcode))
    }
}

case class City private(city: String) extends ValidateAble[String](city)
object City {
    def validate(city: String): Validated[Seq[ErrorMessage], City] = {
        (
            Validated.cond(
            NoDoubleWhitespace(city),
            City(city),
            Seq(ArgumentError(makeError("Der Name eine Stadt darf keine 2 Leerzeichen hintereinander enthalten.", city)))
        ),
            Validated.cond(  // testcode for more than 1 tests for a different error
            NoDoubleWhitespace(city),
            City(city),
            Seq(ArgumentError("anderer Fehler"))
        )
        ).mapN((_, _) => City(city))
    }
}

case class BuyerReference(ref: String) extends ValidateAble[String](ref)
object BuyerReference {
    def validate(ref: String): Validated[Seq[ErrorMessage], BuyerReference] = {
        Validated.cond(
            true,
            BuyerReference(ref),
            Seq(ArgumentError(ref))
        )
    }
}

case class Name(name: String) extends ValidateAble[String](name)
object Name{
    def validate(name: String): Validated[Seq[ErrorMessage], Name] = {
        Validated.cond(
            true,
            Name(name),
            Seq(ArgumentError(name))
        )
    }
}

case class Iban(iban: String) extends ValidateAble[String](iban)
object Iban{
    def validate(iban: String): Validated[Seq[ErrorMessage], Iban] = {
        Validated.cond(
            true,
            Iban(iban),
            Seq(ArgumentError(iban))
        )
    }
}

case class Email(email: String) extends ValidateAble[String](email)
object Email {
    def validate(email: String): Validated[Seq[ErrorMessage], Email] = {
        Validated.cond(
            true,
            Email(email),
            Seq(ArgumentError(email))
        )
    }
}

case class InvoiceIdentifier(id: String) extends ValidateAble[String](id)
object InvoiceIdentifier {
    def validate(id: String): Validated[Seq[ErrorMessage], InvoiceIdentifier] = {
        Validated.cond(
            true,
            InvoiceIdentifier(id),
            Seq(ArgumentError(id))
        )
    }
}

case class Date(date: String) extends ValidateAble[String](date)
object Date {
    def validate(date: String): Validated[Seq[ErrorMessage], Date] = {
        Validated.cond(
            true,
            Date(date),
            Seq(ArgumentError(date))
        )
    }
}

case class InvoiceTypeCode(typeCode: String) extends ValidateAble[String](typeCode)
object InvoiceTypeCode {
    def validate(typeCode: String): Validated[Seq[ErrorMessage], InvoiceTypeCode] = {
        Validated.cond(
            true,
            InvoiceTypeCode(typeCode),
            Seq(ArgumentError(typeCode))
        )
    }
}

case class PaymentTerms(terms: String) extends ValidateAble[String](terms)
object PaymentTerms {
        def validate(terms: String): Validated[Seq[ErrorMessage], PaymentTerms] = {
        Validated.cond(
            true,
            PaymentTerms(terms),
            Seq(ArgumentError(terms))
        )
    }
}

case class TelephoneNumber(number: String) extends ValidateAble[String](number)
object TelephoneNumber {
    def validate(number: String): Validated[Seq[ErrorMessage], TelephoneNumber] = {
        Validated.cond(
            true,
            TelephoneNumber(number),
            Seq(ArgumentError(number))
        )
    }
}

case class WebsiteLink(link: String) extends ValidateAble[String](link)
object WebsiteLink {
    def validate(link: String): Validated[Seq[ErrorMessage], WebsiteLink] = {
        Validated.cond(
            true,
            WebsiteLink(link),
            Seq(ArgumentError(link))
        )
    }
}

case class Street(street: String) extends ValidateAble[String](street)
object Street {
    def validate(street: String): Validated[Seq[ErrorMessage], Street] = {
        Validated.cond(
            true,
            Street(street),
            Seq(ArgumentError(street))
        )
    }
}

case class SellerVATIdentifier(id: String) extends ValidateAble[String](id)
object SellerVATIdentifier {
    def validate(id: String): Validated[Seq[ErrorMessage], SellerVATIdentifier] = {
        Validated.cond(
            true,
            SellerVATIdentifier(id),
            Seq(ArgumentError(id))
        )
    }
}

case class VATCategoryCode(code: String) extends ValidateAble[String](code)
object VATCategoryCode {
    def validate(code: String): Validated[Seq[ErrorMessage], VATCategoryCode] = {
        Validated.cond(
            true,
            VATCategoryCode(code),
            Seq(ArgumentError(code))
        )
    }
}

case class VATRate(rate: Double) extends ValidateAble[Double](rate)
object VATRate {
    def validate(rate: String): Validated[Seq[ErrorMessage], VATRate] = {
        Validated.cond(
            true,  // Check here if the String can be converted to a Double
            VATRate(rate.toDouble),
            Seq(ArgumentError(rate))
        )
    }
}

case class Quantity(quantity: Double) extends ValidateAble[Double](quantity)
object Quantity {
    def validate(quantity: String): Validated[Seq[ErrorMessage], Quantity] = {
        Validated.cond(
            true,
            Quantity(quantity.toDouble),
            Seq(ArgumentError(quantity))
        )
    }
}

case class NetPrice(netPrice: Double) extends ValidateAble[Double](netPrice)
object NetPrice {
    def validate(netPrice: String): Validated[Seq[ErrorMessage], NetPrice] = {
        Validated.cond(
            true,
            NetPrice(netPrice.toDouble),
            Seq(ArgumentError(netPrice))
        )
    }
}

case class NetAmount(amount: Double) extends ValidateAble[Double](amount)
object NetAmount {
    def validate(quantity: String, netPrice: String): Validated[Seq[ErrorMessage], NetAmount] = {
        Validated.cond(
            true,
            NetAmount(quantity.toDouble * netPrice.toDouble),
            Seq(ArgumentError(quantity, netPrice))
        )
    }
}

case class VATExemptionReason(reason: String) extends ValidateAble[String](reason)
object VATExemptionReason {
    def validate(reason: String): Validated[Seq[ErrorMessage], VATExemptionReason] = {
        Validated.cond(
            true,
            VATExemptionReason(reason),
            Seq(ArgumentError(reason))
        )
    }
}

case class PositionID(id: String) extends ValidateAble[String](id)
object PositionID {
    def validate(id: String): Validated[Seq[ErrorMessage], PositionID] = {
        Validated.cond(
            true,
            PositionID(id),
            Seq(ArgumentError(id))
        )
    }
}

case class PositionName(name: String) extends ValidateAble[String](name)
object PositionName {
    def validate(name: String): Validated[Seq[ErrorMessage], PositionName] = {
        Validated.cond(
            true,
            PositionName(name),
            Seq(ArgumentError(name))
        )
    }
}

case class Hours(hours: Double) extends ValidateAble[Double](hours)
object Hours {
    def validate(hours: String): Validated[Seq[ErrorMessage], Hours] = {
        Validated.cond(
            true,
            Hours(hours.toDouble),
            Seq(ArgumentError(hours))
        )
    }
}

case class HourlyRate(rate: Double) extends ValidateAble[Double](rate)
object HourlyRate {
    def validate(hourlyrate: String): Validated[Seq[ErrorMessage], HourlyRate] = {
        Validated.cond(
            true,
            HourlyRate(hourlyrate.toDouble),
            Seq(ArgumentError(hourlyrate))
        )
    }
}
