package utility.inputNames

enum InputName {
    // InvoiceMetaData
    case InvoiceNumber
    case InvoiceIssueDate
    // InvoiceSeller
    case SellerName
    case SellerAddressLine1
    case SellerPostCode
    case SellerCity
    case SellerCountryCode
    case TODO1
    case TODO3
    case SellerElectronicAddress
    case SellerVATIdentifier
    // InvoiceSellerContact
    case SellerContactPoint
    case SellerContactTelephoneNumber
    case SellerContactEmailAddress
    // InvoiceBuyer
    case BuyerReference
    case BuyerName
    case BuyerPostCode
    case BuyerCity
    case BuyerCountryCode
    case TODO4
    case BuyerElectronicAddress
    // InvoicePaymentInformation
    case InvoiceCurrencyCode
    case PaymentMeansTypeCode
    case PaymentTerms

    case VATExemptionReasonText

    // VATCategoryIdentifier
    case InvoicedItemVATCategoryCode(index: Int)
    case InvoicedItemVATRate(index: Int)
    // InvoicePosition
    case InvoiceLineIdentifier(index: Int)
    case ItemName(index: Int)
    case InvoicedQuantity(index: Int)
    case ItemNetPrice(index: Int)
    case InvoicedQuantityUnitOfMeasureCode(index: Int)
    case InvoiceLineNetAmount(index: Int)
}
