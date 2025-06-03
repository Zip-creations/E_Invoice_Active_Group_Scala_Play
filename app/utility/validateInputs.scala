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

case class BuyerName(name: String)
object BuyerName{
    def validate(name: String): Validated[Seq[ErrorMessage], BuyerName] = {
        Validated.cond(
            true,
            BuyerName(name),
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
