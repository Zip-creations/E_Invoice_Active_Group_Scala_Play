package utility.inputNames

enum InputName {
    // def indexed(index: Int = 1) = {
    //     this match {
    //         case ItemName => f"$this$index"
    //         case _ => this
    //     }
    // }
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
    // Both can be called, but ItemName should only be callable with an index:
    // println(InputName.ItemName)
    // println(InputName.ItemName(5))
}

sealed trait InputName2
object InputName2 {
    case object InvoiceNumber extends InputName2
    case class ItemName(index: Int) extends InputName2
}

object InputName3 {
    def InvoiceNumber = InputName4.NoIndex("InvoiceNumber")
    def ItemName(index: Int) = InputName4.Indexed("ItemName", index)
}

enum InputName4 {
    case Indexed(name: String, index: Int)
    case NoIndex(name: String)
}
