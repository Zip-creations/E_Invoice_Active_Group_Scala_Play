package utility

// class Invoice2():
//     var data: Map[InvoiceDataType, Null] = Map.empty
//     def SetValue(name: InvoiceDataType) = {
//         data = data.updated(name, null)
//     }

case class Invoice3(
    invoicemetadata: InvoiceDataType.InvoiceMetaData,
    ) {
    val invoiceIdentifier = invoicemetadata.number
    // Invoice3.Get(InvoiceDataType.InvoiceMetaData.number)
    // def Get(datatype: InvoiceDataType) = {

    // }
    }

class Invoice4(invoicemetadata: InvoiceDataType.InvoiceMetaData) {
    val invoiceIdentifier = invoicemetadata.number
    val invoiceDate = invoicemetadata.date
}
