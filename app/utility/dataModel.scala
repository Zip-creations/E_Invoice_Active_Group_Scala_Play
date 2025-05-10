package utility

class Invoice2():
    var data: Map[InvoiceDataType, Null] = Map.empty
    def SetValue(name: InvoiceDataType) = {
        data = data.updated(name, null)
    }

case class Invoice3(
    invoicemetadata: InvoiceDataType.InvoiceMetaData,
    )
