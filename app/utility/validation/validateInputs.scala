package utility.validation

import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import scala.util.matching.Regex

import sharedUtility.error._
import sharedUtility.validation._
import java.net.URL

case class PostCode private(postcode: String) extends ValidateAble[String](postcode)
object PostCode {
    def validate(postcode: InputType): Validated[Seq[ErrorMessage], PostCode] = {
        val errors = basicTests(postcode, 50, "Die Postleitzahl")
        if (errors.isEmpty) {
            Valid(PostCode(postcode.value))
        } else {
            Invalid(errors)
        }
    }
}

case class City private(city: String) extends ValidateAble[String](city)
object City {
    def validate(city: InputType): Validated[Seq[ErrorMessage], City] = {
        val errors = basicTests(city, 100, "Der Stadtname")
        if (errors.isEmpty) {
            Valid(City(city.value))
        } else {
            Invalid(errors)
        }
    }
}

case class BuyerReference private(ref: String) extends ValidateAble[String](ref)
object BuyerReference {
    def validate(ref: InputType): Validated[Seq[ErrorMessage], BuyerReference] = {
        Validated.cond(
            true,  // Can be used for Leitweg-ID; depends on the Bundesland
            BuyerReference(ref.value),
            Seq(ArgumentError(ref))
        )
    }
}

case class Name private(name: String) extends ValidateAble[String](name)
object Name{
    def validate(name: InputType): Validated[Seq[ErrorMessage], Name] = {
        Validated.cond(
            name.value.length <= 100,
            Name(name.value),
            Seq(ArgumentError(name))
        )
    }
}

case class Iban private(iban: String) extends ValidateAble[String](iban)
object Iban{
    def validate(iban: InputType): Validated[Seq[ErrorMessage], Iban] = {
        Validated.cond(
            true,
            Iban(iban.value),
            Seq(ArgumentError(iban))
        )
    }
}

case class Email private(email: String) extends ValidateAble[String](email)
object Email {
    def validate(email: InputType): Validated[Seq[ErrorMessage], Email] = {
        val regex = "^(?!\\.)(?!.*\\.\\.)([a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*)@([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)$".r  // base for the regex has been taken from https://html.spec.whatwg.org/multipage/input.html#valid-e-mail-address
        Validated.cond(
            regex.matches(email.value),
            Email(email.value),
            Seq(ArgumentError(makeError("Eine Email hat ein ungültiges Format.", email)))
        )
    }
}

case class InvoiceIdentifier private(id: String) extends ValidateAble[String](id)
object InvoiceIdentifier {
    def validate(id: InputType): Validated[Seq[ErrorMessage], InvoiceIdentifier] = {
        val errors = basicTests(id, 50, "Die Rechnungsnummer")
        if (errors.isEmpty) {
            Valid(InvoiceIdentifier(id.value))
        } else {
            Invalid(errors)
        }
    }
}

case class Year private(year: Int) extends ValidateAble(year)
object Year {
    def validate(year: InputType): Validated[Seq[ErrorMessage], Year] = {
        val parsedYear = year.value.toInt
        Validated.cond(
            parsedYear >= 0,
            Year(parsedYear),
            Seq(ArgumentError(makeError("Das Jahr eines Datums muss größer oder gleich 0 sein.", year)))
        )
    }
}

case class Month private(month: Int) extends ValidateAble(month)
object Month {
    def validate(month: InputType) = {
        val parsedMonth = month.value.toInt
        Validated.cond(
            parsedMonth > 0 && parsedMonth <= 12,
            Month(parsedMonth),
            Seq(ArgumentError(makeError("Der Monat eines Datum muss zwischen 1 und 12 liegen.", month)))
        )
    }
}

case class Day private(day: Int) extends ValidateAble(day)
object Day {
    def validate(day: InputType) = {
        val parsedDay = day.value.toInt
        Validated.cond(
            parsedDay > 0 && parsedDay <= 31, // Can't detect errors like 31. April or 30. February
            Day(parsedDay),
            Seq(ArgumentError(makeError("Der Tag eines Datums muss zwischen 1 und 31 liegen.", day)))
        )
    }
}

case class PaymentTerms private(terms: String) extends ValidateAble[String](terms)
object PaymentTerms {
    def validate(input: InputType): Validated[Seq[ErrorMessage], PaymentTerms] = {
        val terms = input.value
        Validated.cond(
            terms.length <= 1000,
            PaymentTerms(terms),
            Seq(ArgumentError(makeError("Die Zahlungsbedingungen enthalten zu viele Zeichen.", input)))
        )
    }
}

case class TelephoneNumber private(number: String) extends ValidateAble[String](number)
object TelephoneNumber {
    def validate(input: InputType): Validated[Seq[ErrorMessage], TelephoneNumber] = {
        val number = input.value
        Validated.cond(
            true,
            TelephoneNumber(number),
            Seq(ArgumentError(input))
        )
    }
}

case class WebsiteLink private(link: String) extends ValidateAble[String](link)
object WebsiteLink {
    def validate(input: InputType): Validated[Seq[ErrorMessage], WebsiteLink] = {
        val link = input.value
        try {
            new URL(link)
            Valid(WebsiteLink(link))
        } catch {
            case _: Exception => Invalid(Seq(ArgumentError(input)))
        }
    }
}

case class Street private(street: String) extends ValidateAble[String](street)
object Street {
    def validate(input: InputType): Validated[Seq[ErrorMessage], Street] = {
        val street = input.value
        Validated.cond(
            street.length <= 100,
            Street(street),
            Seq(ArgumentError(makeError("Ein Straßenname enthält zu viele Zeichen.", input)))
        )
    }
}

case class SellerVATIdentifier private(id: String) extends ValidateAble[String](id)
object SellerVATIdentifier {
    def validate(id: InputType): Validated[Seq[ErrorMessage], SellerVATIdentifier] = {
        Validated.cond(
            true,  // See https://evatr.bff-online.de/eVatR/xmlrpc/ and https://www.ihk-muenchen.de/de/Service/Recht-und-Steuern/Steuerrecht/Umsatzsteuer/Umsatzsteuer-Identifikationsnummer/
            SellerVATIdentifier(id.value),
            Seq(ArgumentError(id))
        )
    }
}

case class VATRate private(rate: Double) extends ValidateAble[Double](rate)
object VATRate {
    def validate(input: InputType): Validated[Seq[ErrorMessage], VATRate] = {
        val rate = input.value
        Validated.cond(
            isValidDouble(rate),
            VATRate(rate.toDouble),
            Seq(ArgumentError(makeError("Für einen Umsatzsteuersatz wurde keine gültige Zahl eingegeben.", input)))).andThen{
                rate =>
                    Validated.cond(
                        notNegative(rate.get),
                        rate,
                        Seq(ArgumentError(makeError("Der Umsatzsteuersatz darf nicht negativ sein.", input))))
            }
    }
}

case class Quantity private(quantity: Double) extends ValidateAble[Double](quantity)
object Quantity {
    def validate(input: InputType): Validated[Seq[ErrorMessage], Quantity] = {
        val quantity = input.value
        Validated.cond(
            isValidDouble(quantity),
            Quantity(quantity.toDouble),
            Seq(ArgumentError(makeError("Für eine Menge wurde keine gültige Zahl eingegeben.", input)))).andThen{
                quantity =>
                    Validated.cond(
                        notNegative(quantity.get),
                        quantity,
                        Seq(ArgumentError(makeError("Eine Menge darf nicht negativ sein.", input))))
            }
    }
}

case class NetPrice private(netPrice: Double) extends ValidateAble[Double](netPrice)
object NetPrice {
    def validate(input: InputType): Validated[Seq[ErrorMessage], NetPrice] = {
        val netPrice = input.value
        Validated.cond(
            isValidDouble(netPrice),
            NetPrice(netPrice.toDouble),
            Seq(ArgumentError(makeError("Für einen Stückpreis wurde keine gültige Zahl eingegeben.", input)))).andThen{
                netPrice =>
                    Validated.cond(
                        notNegative(netPrice.get),
                        netPrice,
                        Seq(ArgumentError(makeError("Der Stückpreis darf nicht negativ sein.", input))))
            }
    }
}

case class NetAmount private(amount: Double) extends ValidateAble[Double](amount)
object NetAmount {
    def validate(inputQuantity: InputType, inputNetPrice: InputType): Validated[Seq[ErrorMessage], NetAmount] = {
        val quantity = inputQuantity.value
        val netPrice = inputNetPrice.value
        (Quantity.validate(inputQuantity),
        NetPrice.validate(inputNetPrice)
        ).mapN((_,_) => NetAmount(quantity.toDouble * netPrice.toDouble))
    }
}

case class VATExemptionReason private(reason: String) extends ValidateAble[String](reason)
object VATExemptionReason {
    def validate(input: InputType): Validated[Seq[ErrorMessage], VATExemptionReason] = {
        val reason = input.value
        Validated.cond(
            reason.length <= 1000,
            VATExemptionReason(reason),
            Seq(ArgumentError(makeError("Ein Befreiugsgrund enthält zu viele Zeichen.", input)))
        )
    }
}

case class PositionID private(id: String) extends ValidateAble[String](id)
object PositionID {
    def validate(input: InputType): Validated[Seq[ErrorMessage], PositionID] = {
        val id = input.value
        Validated.cond(
            true,
            PositionID(id),
            Seq(ArgumentError(input))
        )
    }
}

case class PositionName private(name: String) extends ValidateAble[String](name)
object PositionName {
    def validate(input: InputType): Validated[Seq[ErrorMessage], PositionName] = {
        val name = input.value
        Validated.cond(
            true,
            PositionName(name),
            Seq(ArgumentError(input))
        )
    }
}

case class Hours private(hours: Double) extends ValidateAble[Double](hours)
object Hours {
    def validate(input: InputType): Validated[Seq[ErrorMessage], Hours] = {
        val hours = input.value
        Validated.cond(
            isValidDouble(hours),
            Hours(hours.toDouble),
            Seq(ArgumentError(makeError("Für eine Anzahl an Stunden wurde keine gültige Zahl eingegeben.", input)))).andThen{
                hours =>
                    Validated.cond(
                        notNegative(hours.get),
                        hours,
                        Seq(ArgumentError(makeError("Eine Stundenanzahl darf nicht negativ sein.", input))))
            }
    }
}

case class HourlyRate private(rate: Double) extends ValidateAble[Double](rate)
object HourlyRate {
    def validate(input: InputType): Validated[Seq[ErrorMessage], HourlyRate] = {
        val hourlyrate = input.value
        Validated.cond(
            isValidDouble(hourlyrate),
            HourlyRate(hourlyrate.toDouble),
            Seq(ArgumentError(makeError("Für einen Stundensatz wurde keine gültige Zahl eingegeben.", input)))).andThen{
                hourlyrate =>
                    Validated.cond(
                        notNegative(hourlyrate.get),
                        hourlyrate,
                        Seq(ArgumentError(makeError("Ein Stundensatz darf nicht negativ sein.", input)))
                    )
            }
    }
}
