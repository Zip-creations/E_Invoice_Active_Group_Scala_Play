package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait InvolvedPartiesValidator {
    def validateSeller(seller: InvoiceSeller): Validated[Seq[ErrorMessage], InvoiceSeller] = {
        SellerValidator.validateSeller(seller)
    }
    def validateSellerContact(sellerContact: InvoiceSellerContact): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
        SellerContactValidator.validateSellerContact(sellerContact)
    }
    def validateBuyer(buyer: InvoiceBuyer): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
        BuyerValidator.validateBuyer(buyer)
    }
    def validateInvolvedParties(seller: InvoiceSeller, sellerContact: InvoiceSellerContact, buyer: InvoiceBuyer): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        (
            validateSeller(seller),
            validateSellerContact(sellerContact),
            validateBuyer(buyer)
        ).mapN(InvoiceInvolvedParties.apply)
    }
    def validateInvolvedParties(parties: InvoiceInvolvedParties): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] = {
        validateInvolvedParties(
            parties.seller,
            parties.sellerContact,
            parties.buyer
        )
    }
}
object InvolvedPartiesValidator extends InvolvedPartiesValidator

// case class InvoiceInvolvedParties(
//     seller: InvoiceSeller,
//     sellerContact: InvoiceSellerContact,
//     buyer: InvoiceBuyer
// )
