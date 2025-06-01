package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait InvolvedPartiesValidator {
    def validateSeller(seller: Validated[Seq[ErrorMessage], InvoiceSeller]): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        seller
    }
    def validateSellerContact(sellerContact: Validated[Seq[ErrorMessage], InvoiceSellerContact]): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        sellerContact
    }
    def validateBuyer(buyer: Validated[Seq[ErrorMessage], InvoiceBuyer]): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        buyer
    }
    def validateInvolvedParties(seller: Validated[Seq[ErrorMessage], InvoiceSeller], sellerContact: Validated[Seq[ErrorMessage], InvoiceSellerContact], buyer: Validated[Seq[ErrorMessage], InvoiceBuyer]): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        (
            validateSeller(seller),
            validateSellerContact(sellerContact),
            validateBuyer(buyer)
        ).mapN(InvoiceInvolvedParties.apply)
    }
    // def validateInvolvedParties(parties: InvoiceInvolvedParties): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
    //     validateInvolvedParties(
    //         parties.seller,
    //         parties.sellerContact,
    //         parties.buyer
    //     )
    // }
}
object InvolvedPartiesValidator extends InvolvedPartiesValidator
