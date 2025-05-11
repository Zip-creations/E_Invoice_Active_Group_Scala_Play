package utility

enum InvoiceDataType {
    case InvoiceMetaData(number: String, date: String)
    case Seller(name: String, street: String, postcode: String, city: String, telephonenumber: String, faxnumber: String, websitelink: String, email: String)
    case Position(number:String, amount: String, category: String, positiontype: PositionType, currency: String, price: String, taxpercentage: String)
}

enum PositionType {
    case Item()
    case WorkedTime()
}
