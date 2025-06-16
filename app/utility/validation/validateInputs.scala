package utility.validation

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

def makeError(message: String, value: String): String = {
    s"${message} Fehlerquelle: \'${value}\'"
}
def makeError(message: String, value: ValidateAble[?]): String = {
    s"${message} Fehlerquelle: \'${value.getStr.slice(4,5)}\'"
}

abstract class ValidateAble[T](val value: T) {
    def get: T = {
        value
    }
    def getStr: String = {
        value.toString
    }
}

case class PostCode private(postcode: String) extends ValidateAble[String](postcode)
object PostCode {
    def validate(postcode: String): Validated[Seq[ErrorMessage], PostCode] = {
        Validated.cond(
            NoDoubleWhitespace(postcode),
            PostCode(postcode),
            Seq(ArgumentError(makeError("Eine Postleitzahl darf keine 2 Leerzeichen hintereinander enthalten.", postcode)))
        )
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

case class Year(year: String) extends ValidateAble(year)
object Year {
    def validate(year: String): Validated[Seq[ErrorMessage], Year] = {
        val validatedYear = ValidYear(year)
        Validated.cond(
            validatedYear.isDefined,
            Year(validatedYear.get),
            Seq(ArgumentError(makeError("Das Jahr eines Datums muss größer oder gleich 0 sein.", year)))
        )
    }
}

case class Month(month: String) extends ValidateAble(month)
object Month {
    def validate(month: String) = {
        val validatedMonth = ValidMonth(month)
        Validated.cond(
            validatedMonth.isDefined,
            Month(validatedMonth.get),
            Seq(ArgumentError(makeError("Der Monat eines Datum muss zwischen 1 und 12 liegen.", month)))
        )
    }
}

case class Day(day: String) extends ValidateAble(day)
object Day {
    def validate(day: String) = {
        val validatedDay = ValidYear(day)
        Validated.cond(
            validatedDay.isDefined,
            Day(validatedDay.get),
            Seq(ArgumentError(makeError("Der Tag eines Datums muss zwischen 1 und 31 liegen.", day)))
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

case class VATRate(rate: Double) extends ValidateAble[Double](rate)
object VATRate {
    def validate(rate: String): Validated[Seq[ErrorMessage], VATRate] = {
        Validated.cond(
            IsValidDouble(rate),
            VATRate(rate.toDouble),
            Seq(ArgumentError(makeError("Für einen Umsatzsteuersatz wurde keine gültige Zahl eingegeben.", rate)))).andThen{
                rate =>
                    Validated.cond(
                        NotNegative(rate.get),
                        rate,
                        Seq(ArgumentError(makeError("Der Umsatzsteuersatz darf nicht negativ sein.", rate))))
            }
    }
}

case class Quantity(quantity: Double) extends ValidateAble[Double](quantity)
object Quantity {
    def validate(quantity: String): Validated[Seq[ErrorMessage], Quantity] = {
        Validated.cond(
            IsValidDouble(quantity),
            Quantity(quantity.toDouble),
            Seq(ArgumentError(makeError("Für eine Menge wurde keine gültige Zahl eingegeben.", quantity)))).andThen{
                quantity =>
                    Validated.cond(
                        NotNegative(quantity.get),
                        quantity,
                        Seq(ArgumentError(makeError("Eine Menge darf nicht negativ sein.", quantity))))
            }
    }
}

case class NetPrice(netPrice: Double) extends ValidateAble[Double](netPrice)
object NetPrice {
    def validate(netPrice: String): Validated[Seq[ErrorMessage], NetPrice] = {
        Validated.cond(
            IsValidDouble(netPrice),
            NetPrice(netPrice.toDouble),
            Seq(ArgumentError(makeError("Für einen Stückpreis wurde keine gültige Zahl eingegeben.", netPrice)))).andThen{
                netPrice =>
                    Validated.cond(
                        NotNegative(netPrice.get),
                        netPrice,
                        Seq(ArgumentError(makeError("Der Stückpreis darf nicht negativ sein.", netPrice))))
            }
    }
}

case class NetAmount(amount: Double) extends ValidateAble[Double](amount)
object NetAmount {
    def validate(quantity: String, netPrice: String): Validated[Seq[ErrorMessage], NetAmount] = {
        (Quantity.validate(quantity),
        NetPrice.validate(netPrice)
        ).mapN((_,_) => NetAmount(quantity.toDouble * netPrice.toDouble))
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
            IsValidDouble(hours),
            Hours(hours.toDouble),
            Seq(ArgumentError(makeError("Für eine Anzahl an Stunden wurde keine gültige Zahl eingegeben.", hours)))).andThen{
                hours =>
                    Validated.cond(
                        NotNegative(hours.get),
                        hours,
                        Seq(ArgumentError(makeError("Eine Stundenanzahl darf nicht negativ sein.", hours.getStr))))
            }
    }
}

case class HourlyRate(rate: Double) extends ValidateAble[Double](rate)
object HourlyRate {
    def validate(hourlyrate: String): Validated[Seq[ErrorMessage], HourlyRate] = {
        Validated.cond(
            IsValidDouble(hourlyrate),
            HourlyRate(hourlyrate.toDouble),
            Seq(ArgumentError(makeError("Für einen Stundensatz wurde keine gültige Zahl eingegeben.", hourlyrate)))).andThen{
                hourlyrate =>
                    Validated.cond(
                        NotNegative(hourlyrate.get),
                        hourlyrate,
                        Seq(ArgumentError(makeError("Ein Stundensatz darf nicht negativ sein.", hourlyrate)))
                    )
            }
    }
}
