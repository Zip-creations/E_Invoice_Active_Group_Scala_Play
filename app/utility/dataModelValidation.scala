package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


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
}
object AddressValidator extends AddressValidator

// case class Address (
//     postCode: String,
//     city: String,
//     countryCode: String
// )
