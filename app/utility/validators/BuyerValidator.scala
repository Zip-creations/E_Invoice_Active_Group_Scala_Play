package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait BuyerValidator {
    def validateReference(ref: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            ref,
            Seq(ArgumentError)
        )
    }
    def validateName(name: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            name,
            Seq(ArgumentError)
        )
    }
    def validateAddress(address: Validated[Seq[ErrorMessage], Address]): Validated[Seq[ErrorMessage], Address] = {
        address
    }
    def validateIban(iban: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            iban,
            Seq(ArgumentError)
        )
    }
    def validateEmail(email: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            email,
            Seq(ArgumentError)
        )
    }
    def validateBuyer(reference: String, name: String, address: Validated[Seq[ErrorMessage], Address], iban: String, email: String): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        (
            validateReference(reference),
            validateName(name),
            validateAddress(address),
            validateIban(iban),
            validateEmail(email)
        ).mapN(InvoiceBuyer.apply)
    }
    // def validateBuyer(buyer: InvoiceBuyer): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
    //     validateBuyer(
    //         buyer.reference,
    //         buyer.name,
    //         buyer.address,
    //         buyer.iban,
    //         buyer.email
    //     )
    // }
}
object BuyerValidator extends BuyerValidator
