package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait InvoiceValidator {
    def validateMetadata(metadata: InvoiceMetaData): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
        MetaDataValidator.validateMetaData(metadata)
    }
    def validateInvolvedparties(parties: InvoiceInvolvedParties): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        InvolvedPartiesValidator.validateInvolvedParties(parties)
    }
    def validatePositions(positions: List[InvoicePosition]): Validated[Seq[ErrorMessage], List[InvoicePosition]] = {
        positions.traverse(PositionValidator.validatePosition)
    }
    def validatePaymentInformation(info: InvoicePaymentInformation): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        PaymentInformationValidator.validatePaymentInformation(info)
    }
    def validateInvoice(metadata: InvoiceMetaData, involvedParties: InvoiceInvolvedParties, positions: List[InvoicePosition], paymentInformation: InvoicePaymentInformation): Validated[Seq[ErrorMessage], Invoice] = {
        (
        validateMetadata(metadata),
        validateInvolvedparties(involvedParties),
        validatePositions(positions),
        validatePaymentInformation(paymentInformation)
        ).mapN(Invoice.apply)
    }
    def validateInvoice(invoice: Invoice): Validated[Seq[ErrorMessage], Invoice] = {
        validateInvoice(
            invoice.metadata,
            invoice.involvedParties,
            invoice.positions,
            invoice.paymentInformation
        )
    }
}
object InvoiceValidator extends InvoiceValidator
