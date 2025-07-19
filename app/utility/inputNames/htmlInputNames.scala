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
    case Placeholder1 // currently not used in xml
    case Placeholder2 // currently not used in xml
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
    case Placeholder3 // currently not used in xml
    case BuyerElectronicAddress
    // InvoicePaymentInformation
    case InvoiceCurrencyCode
    case PaymentMeansTypeCode
    case PaymentTerms


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

    // VAT group (of positions)
    case VATGroupExemptionReasonText(vatID: String)
    case VATGroupCategory(vatID: String)
    case VATGroupRate(vatID: String)
}
