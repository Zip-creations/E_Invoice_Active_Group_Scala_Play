package utility
class XMLUtility(){

    private case class StoredPosition(netAmount: Double, taxpercentage: Double)
    private var storedPositions: List[StoredPosition] = Nil

    def CreateMetaDataXML(meta: InvoiceMetaData): scala.xml.Elem = {
        val xml = 
            <rsm:ExchangedDocument>
                <ram:ID>
                {meta.number}
                </ram:ID>
                <ram:TypeCode>{meta.typ}</ram:TypeCode>
                <ram:IssueDateTime>
                {
                    // format="102" is determined in the EN 16931 - CII Mapping scheme
                }
                <udt:DateTimeString format="102">
                    {meta.date}
                </udt:DateTimeString>
                </ram:IssueDateTime>
            </rsm:ExchangedDocument>
        return xml
    }

    def CreateSellerXML(seller: InvoiceSeller, sellerContact: InvoiceSellerContact): scala.xml.Elem = {
        val xml =
            <ram:SellerTradeParty>
              <ram:Name>{seller.name}</ram:Name>
                {CreateSellerContactXML(sellerContact)}
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{seller.postcode}</ram:PostcodeCode>
                <ram:CityName>{seller.city}</ram:CityName>
                <ram:CountryID>{seller.country}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{seller.email}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:SellerTradeParty>
        return xml
    }

    def CreateSellerContactXML(sellerContact: InvoiceSellerContact): scala.xml.Elem = {
        val xml =
            <ram:DefinedTradeContact>
                <ram:PersonName>{sellerContact.name}</ram:PersonName>
                <ram:TelephoneUniversalCommunication>
                    <ram:CompleteNumber>{sellerContact.telephonenumber}</ram:CompleteNumber>
                </ram:TelephoneUniversalCommunication>
                <ram:EmailURIUniversalCommunication>
                    <ram:URIID>{sellerContact.email}</ram:URIID>
                </ram:EmailURIUniversalCommunication>
                </ram:DefinedTradeContact>
        return xml
    }

    def CreateBuyerXML(buyer: InvoiceBuyer): scala.xml.Elem = {
        val xml =
            <ram:BuyerTradeParty>
                <ram:Name>{buyer.name}</ram:Name>
                <ram:PostalTradeAddress>
                    <ram:PostcodeCode>{buyer.postcode}</ram:PostcodeCode>
                    <ram:CityName>{buyer.city}</ram:CityName>
                    <ram:CountryID>{buyer.country}</ram:CountryID>
                </ram:PostalTradeAddress>
                <ram:URIUniversalCommunication>
                    <ram:URIID schemeID="EM">{buyer.email}</ram:URIID>
                </ram:URIUniversalCommunication>
            </ram:BuyerTradeParty>
        return xml
    }

    def CreatePositionXML(position: InvoicePosition): scala.xml.Elem = {
        var unitcode = ""
        var chargedAmount = 0.0
        var totalAmount = 0.0
        var chargedQuantity = 0.0

        position match{
            case InvoicePosition.Stundenposition(id, taxpercentage, hours, hourlyrate) =>
                unitcode = "1"
                chargedAmount = hourlyrate
                chargedQuantity = 1
                totalAmount = hours * hourlyrate
            case InvoicePosition.Leistungsposition(id, taxpercentage, amount, quantity) =>
                unitcode = "2"
                chargedAmount = amount
                chargedQuantity = quantity
                totalAmount = amount * quantity
        }
        storedPositions :+ StoredPosition(totalAmount, position.taxpercentage)

        val xml =
            <ram:IncludedSupplyChainTradeLineItem>
                <ram:AssociatedDocumentLineDocument>
                    <ram:LineID>{position.id}</ram:LineID>
                </ram:AssociatedDocumentLineDocument>
                <ram:SpecifiedLineTradeAgreement>
                    <ram:NetPriceProductTradePrice>
                        <ram:ChargeAmount>{chargedAmount.toString}</ram:ChargeAmount>
                    </ram:NetPriceProductTradePrice>
                </ram:SpecifiedLineTradeAgreement>
                <ram:SpecifiedLineTradeDelivery>
                    <ram:BilledQuantity unitCode={unitcode}>{chargedQuantity.toString}</ram:BilledQuantity>
                </ram:SpecifiedLineTradeDelivery>
                <ram:SpecifiedLineTradeSettlement>
                    <ram:ApplicableTradeTax>
                        {
                        // TypeCode=VAT is determined in the EN 16931 - CII Mapping scheme
                        }
                        <ram:TypeCode>VAT</ram:TypeCode>
                        <ram:CategoryCode>{position.taxpercentage}</ram:CategoryCode>
                    </ram:ApplicableTradeTax>
                    <ram:SpecifiedTradeSettlementLineMonetarySummation>
                        <ram:LineTotalAmount>{totalAmount.toString}</ram:LineTotalAmount>
                    </ram:SpecifiedTradeSettlementLineMonetarySummation>
                </ram:SpecifiedLineTradeSettlement>
            </ram:IncludedSupplyChainTradeLineItem>
        return xml
    }

    def CreateDocumentSummaryXML(): scala.xml.Elem = {
        var totalNetAmount = 0.0
        var totalAmountWithoutVAT = 0.0
        var totalAmountWithVAT = 0.0
        var amountDue = 0.0
        for (i <- storedPositions) {
            totalNetAmount += i.netAmount
            totalAmountWithoutVAT += i.netAmount
            totalAmountWithVAT += i.netAmount * (1+ i.taxpercentage)
            totalAmountWithVAT += i.netAmount * (1+ i.taxpercentage)
        }

        val xml =
            <ram:SpecifiedTradeSettlementHeaderMonetarySummation>
                <ram:LineTotalAmount>{totalNetAmount.toString}</ram:LineTotalAmount>
                <ram:TaxBasisTotalAmount>{totalAmountWithoutVAT.toString}</ram:TaxBasisTotalAmount>
                <ram:GrandTotalAmount>{totalAmountWithVAT.toString}</ram:GrandTotalAmount>
                <ram:DuePayableAmount>{amountDue.toString}</ram:DuePayableAmount>
            </ram:SpecifiedTradeSettlementHeaderMonetarySummation>
        return xml
    }
}
