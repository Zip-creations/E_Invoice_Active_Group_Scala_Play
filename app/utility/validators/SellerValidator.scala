package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

sealed trait SellerValidator {
    def validateName(name: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            name,
            Seq(ArgumentError)
        )
    }
    def validateStreet(street: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            street,
            Seq(ArgumentError)
        )
    }
    def validateAddress(address: Address): Validated[Seq[ErrorMessage], Address] = {
        AddressValidator.validateAddress(address)
    }
    def validateTelephonenumber(number: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            number,
            Seq(ArgumentError)
        )
    }
    def validateWebsitelink(link: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            link,
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
    def validateVatidentifier(id: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            id,
            Seq(ArgumentError)
        )
    }
    def validateSeller(name: String, street: String, address: Address, telephonenumber: String, websitelink: String, email: String, id: String): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        (
            validateName(name),
            validateStreet(street),
            validateAddress(address),
            validateTelephonenumber("test"),
            validateWebsitelink("test"),
            validateEmail("test"),
            validateVatidentifier("test")
        ).mapN(InvoiceSeller.apply)
    }
    def validateSeller(seller: InvoiceSeller): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        validateSeller(
            seller.name,
            seller.street,
            seller.address,
            seller.telephonenumber,
            seller.websitelink,
            seller.email,
            seller.vatIdentifier
        )
    }
}
object SellerValidator extends SellerValidator
