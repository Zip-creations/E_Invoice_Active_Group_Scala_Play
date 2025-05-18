package utility
class XMLUtility(){

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
}
