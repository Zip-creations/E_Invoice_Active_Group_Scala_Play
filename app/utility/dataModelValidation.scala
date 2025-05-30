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
    def validateAddress(address: Address): Validated[Seq[ErrorMessage], Address] = {
        AddressValidator.validateAddress(address)
    }
    def validateSeller(address: Address): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        (
            validateName("test"),
            validateName("test"),
            validateAddress(address),
            validateName("test"),
            validateName("test"),
            validateName("test"),
            validateName("test")
        ).mapN(InvoiceSeller.apply)
    }
}
object SellerValidator extends SellerValidator

// case class InvoiceSeller(
//     name: String,
//     street: String,
//     address: Address,
//     telephonenumber: String,
//     websitelink: String,
//     email: String,
//     vatIdentifier: String
//     )

sealed trait AddressValidator {
    def validatePostcode(postcode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            postcode,
            Seq(ArgumentError)
        )
    }
    def validateCity(city: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            city,
            Seq(ArgumentError)
        )
    }
    def validateCountrycode(countrycode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true, // CountryCode.strInList(countrycode)
            countrycode,  // CountryCode.matchStr(str).get
            Seq(ArgumentError)
        )
    }
    def validateAddress(postCode: String, city: String, countrycode: String): Validated[Seq[ErrorMessage], Address] = {
        (
        validatePostcode(postCode),
        validateCity(city),
        validateCountrycode(countrycode)
        ).mapN(Address.apply)
    }
    def validateAddress(address: Address): Validated[Seq[ErrorMessage], Address] = {
        validateAddress(
            address.postCode,
            address.city,
            address.countryCode
        )
    }
}
object AddressValidator extends AddressValidator

// case class Address (
//     postCode: String,
//     city: String,
//     countryCode: String
// )
