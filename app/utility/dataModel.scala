package utility

class Invoice(var invoicenumber: String):
    val invoiceNumber: InvoiceDataType.InvoiceNumber = InvoiceDataType.InvoiceNumber(invoicenumber)


class invoice2():
    var data: Map[InvoiceDataType, String] = Map.empty
