package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait SimplePositionValidator {
    def validateIdentifier(id: Validated[Seq[ErrorMessage], VATCategoryIdentifier]): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        id
    }
    def validateNetAmount(amount: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            amount,
            Seq(ArgumentError)
        )
    }
    def validateSimplePosition(identifier: Validated[Seq[ErrorMessage], VATCategoryIdentifier], netAmount: Double): Validated[Seq[ErrorMessage], SimplePosition] = {
        (
            validateIdentifier(identifier),
            validateNetAmount(netAmount)
        ).mapN(SimplePosition.apply)
    }
    // def validateSimplePosition(simplePosition: SimplePosition): Validated[Seq[ErrorMessage], SimplePosition] = {
    //     validateSimplePosition(
    //         simplePosition.identifier,
    //         simplePosition.netAmount
    //     )
    // }
}
object SimplePositionValidator extends SimplePositionValidator
