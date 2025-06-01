package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait PositionValidator {
    def valideteId(id: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            id,
            Seq(ArgumentError)
        )
    }
    def valideteName(name: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            name,
            Seq(ArgumentError)
        )
    }
    def validateVATId(vatId: VATCategoryIdentifier): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
        VATCategoryIdentifierValidator.validateVATCategoryIdentifier(vatId)
    }
    def validateData(data: InvoicePositionData): Validated[Seq[ErrorMessage], InvoicePositionData] = {
        PositionDataValidator.validatePositionData(data)
    }
    def validatePosition(id: String, name: String, vatId: VATCategoryIdentifier, data :InvoicePositionData): Validated[Seq[ErrorMessage], InvoicePosition] = {
        (
            valideteId(id),
            valideteName(name),
            validateVATId(vatId),
            validateData(data)
        ).mapN(InvoicePosition.apply)
    }
    def validatePosition(position: InvoicePosition): Validated[Seq[ErrorMessage], InvoicePosition] = {
        validatePosition(
            position.id,
            position.name,
            position.vatId,
            position.data
        )
    }
}
object PositionValidator extends PositionValidator
