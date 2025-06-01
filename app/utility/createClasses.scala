package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


def createInvoice(metadata: Validated[Seq[ErrorMessage], InvoiceMetaData], involvedParties: Validated[Seq[ErrorMessage], InvoiceInvolvedParties], positions :List[Validated[Seq[ErrorMessage], InvoicePosition]], paymentInformation: Validated[Seq[ErrorMessage], InvoicePaymentInformation]): Validated[Seq[ErrorMessage], Invoice] = {
    InvoiceValidator.validateInvoice(metadata, involvedParties, positions, paymentInformation)
}

def createMetaData(number: String, date: String, typ: String): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
    MetaDataValidator.validateMetaData(number, date, typ)
}

def createInvolvedParties(seller: Validated[Seq[ErrorMessage], InvoiceSeller], sellerContact: Validated[Seq[ErrorMessage], InvoiceSellerContact], buyer: Validated[Seq[ErrorMessage], InvoiceBuyer]): Validated[Seq[ErrorMessage], InvoiceInvolvedParties] =  {
    InvolvedPartiesValidator.validateInvolvedParties(seller, sellerContact, buyer)
}

def createSeller(name: String, street: String, address: Validated[Seq[ErrorMessage], Address], telephonenumber: String, websitelink: String, email: String, vatIdentifier: String): Validated[Seq[ErrorMessage], InvoiceSeller] = {
    SellerValidator.validateSeller(name, street, address, telephonenumber, websitelink, email, vatIdentifier)
}

def createSellerContact(name: String, telephonenumber: String, email: String): Validated[Seq[ErrorMessage], InvoiceSellerContact] = {
    SellerContactValidator.validateSellerContact(name, telephonenumber, email)
}

def createBuyer(reference: String, name: String, address: Validated[Seq[ErrorMessage], Address], iban: String, email: String): Validated[Seq[ErrorMessage], InvoiceBuyer] = {
    BuyerValidator.validateBuyer(reference, name, address, iban, email)
}

def createVATCategoryIdentifier(vatCode: String, vatRate: Double): Validated[Seq[ErrorMessage], VATCategoryIdentifier] = {
    VATCategoryIdentifierValidator.validateVATCategoryIdentifier(vatCode, vatRate)
}

def createVATGroup(identifier: Validated[Seq[ErrorMessage], VATCategoryIdentifier], positions: List[Validated[Seq[ErrorMessage], SimplePosition]], vatExemptionReason: String=""): Validated[Seq[ErrorMessage], InvoiceVATGroup] = {
    VATGroupValidator.validateVATGroup(identifier, positions, vatExemptionReason)
}

def createPaymentInformation(currencycode: String, paymentMeansCode: String, vatGroups: List[Validated[Seq[ErrorMessage], InvoiceVATGroup]], paymentTerms: String = ""): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
    PaymentInformationValidator.validatePaymentInformation(currencycode, paymentMeansCode, vatGroups, paymentTerms)
}

def createSimplePosition(identifier: Validated[Seq[ErrorMessage], VATCategoryIdentifier], netAmount: Double): Validated[Seq[ErrorMessage], SimplePosition] = {
    SimplePositionValidator.validateSimplePosition(identifier, netAmount)
}

def createPosition(id: String, name: String, vatId: Validated[Seq[ErrorMessage], VATCategoryIdentifier], data: Validated[Seq[ErrorMessage], InvoicePositionData]): Validated[Seq[ErrorMessage], InvoicePosition] = {
    PositionValidator.validatePosition(id, name, vatId, data)
}

def createStundenposition(hours: Double, hourlyrate: Double): Validated[Seq[ErrorMessage], InvoicePositionData.Stundenposition] = {
    StundenpositionValidator.validateStundenposition(hours, hourlyrate)
}

def createLeistungsposition(quantity: Double, pricePerPart: Double, measurementCode: String): Validated[Seq[ErrorMessage], InvoicePositionData.Leistungsposition] = {
    LeistungspositionValidator.validateLeistungsposition(quantity, pricePerPart, measurementCode)
}

def createAddress(postCode: String, city: String, countryCode: String): Validated[Seq[ErrorMessage], Address] = {
    AddressValidator.validateAddress(postCode, city, countryCode)
}
