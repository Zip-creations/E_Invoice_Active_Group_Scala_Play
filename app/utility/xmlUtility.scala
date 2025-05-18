package utility
class XMLUtility(formData: Option[Map[String, Seq[String]]]){
    def connectInput = (inputIdentifier: String) =>
      formData.flatMap(_.get(inputIdentifier).flatMap(_.headOption)).getOrElse("")

    def CreateMetaDataXML(meta: InvoiceMetaData) = {
        <rsm:ExchangedDocument>
          <ram:ID>
            {meta.number}
          </ram:ID>
          <ram:TypeCode>meta.typ</ram:TypeCode>
          <ram:IssueDateTime>
            {
              // format="102" is determined in the EN 16931 - CII Mapping scheme
            }
            <udt:DateTimeString format="102">
              {meta.date}
            </udt:DateTimeString>
          </ram:IssueDateTime>
        </rsm:ExchangedDocument>
    }

    def ProccessBuyerToCII(buyer: InvoiceBuyer): scala.xml.Elem = {
        val xml =
            <ram:BuyerTradeParty>
                <ram:Name>{buyer.name}</ram:Name>
                <ram:PostalTradeAddress>
                    <ram:PostcodeCode>{connectInput("BuyerPostCode")}</ram:PostcodeCode>
                    <ram:CityName>{connectInput("BuyerCity")}</ram:CityName>
                    <ram:CountryID>{connectInput("BuyerCountryCode")}</ram:CountryID>
                </ram:PostalTradeAddress>
                <ram:URIUniversalCommunication>
                    <ram:URIID schemeID="EM">{connectInput("BuyerElectronicAddress")}</ram:URIID>
                </ram:URIUniversalCommunication>
            </ram:BuyerTradeParty>
        return xml
    }
}
