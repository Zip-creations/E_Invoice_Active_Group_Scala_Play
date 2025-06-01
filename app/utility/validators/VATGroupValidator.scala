package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait VATGroupValidator {
    def validateIdentifier(id: VATCategoryIdentifier): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        VATCategoryIdentifierValidator.validateVATCategoryIdentifier(id)
    }
    def validatePositions(positions: List[SimplePosition]): Validated[Seq[ErrorMessage], List[SimplePosition]] = {
        positions.traverse(SimplePositionValidator.validateSimplePosition)
    }
    def validateVATExemptionReason(reason: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            reason,
            Seq(ArgumentError)
        )
    }
    def validateVATGroup(identifier: VATCategoryIdentifier, positions: List[SimplePosition], vatExemptionReason: String=""): Validated[Seq[ErrorMessage], InvoiceVATGroup] = {
        (
            validateIdentifier(identifier),
            validatePositions(positions),
            validateVATExemptionReason(vatExemptionReason)
        ).mapN(InvoiceVATGroup.apply)
    }
    def validateVATGroup(vatGroup: InvoiceVATGroup): Validated[Seq[ErrorMessage], InvoiceVATGroup] = {
        validateVATGroup(
            vatGroup.identifier,
            vatGroup.positions,
            vatGroup.vatExemptionReason
        )
    }
}
object VATGroupValidator extends VATGroupValidator
