package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait InvoiceValidator {
    def validateMetadata(metadata: Validated[Seq[ErrorMessage], InvoiceMetaData]): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
        metadata
    }
    def validateInvolvedparties(parties: Validated[Seq[ErrorMessage], InvoiceInvolvedParties]): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        parties
    }
    def validatePositions(positions: List[Validated[Seq[ErrorMessage], InvoicePosition]]): Validated[Seq[ErrorMessage], List[InvoicePosition]] = {
        positions.sequence
    }
    def validatePaymentInformation(info: Validated[Seq[ErrorMessage], InvoicePaymentInformation]): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        info
    }
    def validateInvoice(metadata: Validated[Seq[ErrorMessage], InvoiceMetaData], involvedParties: Validated[Seq[ErrorMessage], InvoiceInvolvedParties], positions: List[Validated[Seq[ErrorMessage], InvoicePosition]], paymentInformation: Validated[Seq[ErrorMessage], InvoicePaymentInformation]): Validated[Seq[ErrorMessage], Invoice] = {
        (
        validateMetadata(metadata),
        validateInvolvedparties(involvedParties),
        validatePositions(positions),
        validatePaymentInformation(paymentInformation)
        ).mapN(Invoice.apply)
    }
    // def validateInvoice(invoice: Invoice): Validated[Seq[ErrorMessage], Invoice] = {
    //     validateInvoice(
    //         invoice.metadata,
    //         invoice.involvedParties,
    //         invoice.positions,
    //         invoice.paymentInformation
    //     )
    // }
}
object InvoiceValidator extends InvoiceValidator
