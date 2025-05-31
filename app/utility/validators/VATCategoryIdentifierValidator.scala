package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait VATCategoryIdentifierValidator {
    def validateVatCode(code: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            code,
            Seq(ArgumentError)
        )
    }
    def validateVatRate(rate: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            rate,
            Seq(ArgumentError)
        )
    }
    def validateVATCategoryIdentifier(vatCode: String, vatRate: Double): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        (
            validateVatCode(vatCode),
            validateVatRate(vatRate)
        ).mapN(VATCategoryIdentifier.apply)
    }
    def validateVATCategoryIdentifier(vatCategoryIdentifier: VATCategoryIdentifier): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        validateVATCategoryIdentifier(
            vatCategoryIdentifier.vatCode,
            vatCategoryIdentifier.vatRate
        )
    }
}
object VATCategoryIdentifierValidator extends VATCategoryIdentifierValidator
