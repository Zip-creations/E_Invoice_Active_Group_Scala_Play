package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait SimplePositionValidator {
    def validateIdentifier(id: VATCategoryIdentifier): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        VATCategoryIdentifierValidator.validateVATCategoryIdentifier(id)
    }
    def validateNetAmount(amount: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            amount,
            Seq(ArgumentError)
        )
    }
    def validateSimplePosition(identifier: VATCategoryIdentifier, netAmount: Double): Validated[Seq[ErrorMessage], SimplePosition] = {
        (
            validateIdentifier(identifier),
            validateNetAmount(netAmount)
        ).mapN(SimplePosition.apply)
    }
    def validateSimplePosition(simplePosition: SimplePosition): Validated[Seq[ErrorMessage], SimplePosition] = {
        validateSimplePosition(
            simplePosition.identifier,
            simplePosition.netAmount
        )
    }
}
object SimplePositionValidator extends SimplePositionValidator
