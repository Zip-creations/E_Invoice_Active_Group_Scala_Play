package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait SellerContactValidator {
    def validateName(postcode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            postcode,
            Seq(ArgumentError)
        )
    }
    def validateTelephonenumber(postcode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            postcode,
            Seq(ArgumentError)
        )
    }
    def validateEmail(postcode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            postcode,
            Seq(ArgumentError)
        )
    }
    def validateSellerContact(name: String, telephonenumber: String, email: String): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        (
            validateName(name),
            validateTelephonenumber(telephonenumber),
            validateEmail(email)
        ).mapN(InvoiceSellerContact.apply)
    }
    def validateSellerContact(sellerContact: InvoiceSellerContact): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        validateSellerContact(
            sellerContact.name,
            sellerContact.telephonenumber,
            sellerContact.email
        )
    }
}
object SellerContactValidator extends SellerContactValidator

// case class InvoiceSellerContact(
//     name: String,
//     telephonenumber: String,
//     email: String
//     )