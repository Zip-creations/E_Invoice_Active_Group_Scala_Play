package utility
import scala.collection.mutable
import scala.xml.NodeSeq
class XMLUtility(){

    private case class StoredPosition(netAmount: Double, taxCode: String)
    private var storedPositions: List[StoredPosition] = Nil
    private val VATCodeToNum: Map[String, Double] = Map(
        "S" -> 0,
        "Z" -> 0,
        "E" -> 0,
        "AE" -> 0,
        "K" -> 0,
        "G" -> 0,
        "O" -> 0,
        "L" -> 0,
        "M" -> 0
    )

    def insertOptionalInput(value: String, xml: scala.xml.Elem): scala.xml.NodeSeq = {
      if (value != "") {
        return xml
      } else {
        return scala.xml.NodeSeq.Empty
      }
    }

    def CreateInvoiceXML(invoice: Invoice): scala.xml.Elem = {
        val xml =       
            <rsm:CrossIndustryInvoice xmlns:rsm="urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:qdt="urn:un:unece:uncefact:data:standard:QualifiedDataType:100" xmlns:ram="urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:100" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:100">
                <rsm:ExchangedDocumentContext>
                <ram:BusinessProcessSpecifiedDocumentContextParameter>
                    <ram:ID>urn:fdc:peppol.eu:2017:poacc:billing:01:1.0</ram:ID>
                </ram:BusinessProcessSpecifiedDocumentContextParameter>
                <ram:GuidelineSpecifiedDocumentContextParameter>
                    <ram:ID>urn:cen.eu:en16931:2017#compliant#urn:xeinkauf.de:kosit:xrechnung_3.0</ram:ID>
                </ram:GuidelineSpecifiedDocumentContextParameter>
                </rsm:ExchangedDocumentContext>
                {CreateMetaDataXML(invoice.metadata)}
                <rsm:SupplyChainTradeTransaction>
                {for (i <- invoice.positions)
                    yield CreatePositionXML(i)}
                <ram:ApplicableHeaderTradeAgreement>
                    <ram:BuyerReference>{invoice.buyer.reference}</ram:BuyerReference>
                    {CreateSellerXML(invoice.seller, invoice.sellerContact)}
                    {CreateBuyerXML(invoice.buyer)}
                </ram:ApplicableHeaderTradeAgreement>
                <ram:ApplicableHeaderTradeDelivery>
                </ram:ApplicableHeaderTradeDelivery>
                {CreatePaymentInformationXML(invoice.paymentInformation)}
                </rsm:SupplyChainTradeTransaction>
            </rsm:CrossIndustryInvoice>
        return xml
    }

    private def CreateMetaDataXML(meta: InvoiceMetaData): scala.xml.Elem = {
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

    private def CreateSellerXML(seller: InvoiceSeller, sellerContact: InvoiceSellerContact): scala.xml.Elem = {
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

    private def CreateSellerContactXML(sellerContact: InvoiceSellerContact): scala.xml.Elem = {
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

    private def CreateBuyerXML(buyer: InvoiceBuyer): scala.xml.Elem = {
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

    private def CreatePositionXML(position: InvoicePosition): scala.xml.Elem = {
        var unitcode = ""
        var chargedAmount = 0.0
        var totalAmount = 0.0
        var chargedQuantity = 0.0

        position.data match{
            case InvoicePositionData.Stundenposition(hours, hourlyrate) =>
                unitcode = "1"
                chargedAmount = hourlyrate
                chargedQuantity = 1
                totalAmount = hours * hourlyrate
            case InvoicePositionData.Leistungsposition(quantity) =>
                unitcode = "2"
                chargedAmount = quantity
                chargedQuantity = quantity
                totalAmount = quantity
        }
        storedPositions = storedPositions :+ StoredPosition(totalAmount, position.VATcategoryCode)
        print(storedPositions)

        val xml =
            <ram:IncludedSupplyChainTradeLineItem>
                <ram:AssociatedDocumentLineDocument>
                    <ram:LineID>{position.id}</ram:LineID>
                </ram:AssociatedDocumentLineDocument>
                <ram:SpecifiedTradeProduct>
                    <ram:Name>position.name</ram:Name>
                </ram:SpecifiedTradeProduct>
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
                        <ram:CategoryCode>{position.VATcategoryCode}</ram:CategoryCode>
                    </ram:ApplicableTradeTax>
                    <ram:SpecifiedTradeSettlementLineMonetarySummation>
                        <ram:LineTotalAmount>{totalAmount.toString}</ram:LineTotalAmount>
                    </ram:SpecifiedTradeSettlementLineMonetarySummation>
                </ram:SpecifiedLineTradeSettlement>
            </ram:IncludedSupplyChainTradeLineItem>
        return xml
    }

    private def CreatePaymentInformationXML(paymentInfo: InvoicePaymentInformation): scala.xml.Elem = {
        val xml =
            <ram:ApplicableHeaderTradeSettlement>
                <ram:InvoiceCurrencyCode>{paymentInfo.currencycode}</ram:InvoiceCurrencyCode>
                <ram:SpecifiedTradeSettlementPaymentMeans>
                    <ram:TypeCode>{paymentInfo.paymentMeansCode}</ram:TypeCode>
                </ram:SpecifiedTradeSettlementPaymentMeans>
                {
                    // Group all Positions based on their tax code, to create a tax summary for each code easily
                    var sortedByVATCode: mutable.Map[String, List[StoredPosition]] = mutable.Map.empty
                    for (pos <- storedPositions) {
                        val currentPos = sortedByVATCode.getOrElse(pos.taxCode, List.empty)
                        sortedByVATCode.update(pos.taxCode, currentPos :+ pos)
                    }
                    {for (vatCode <- sortedByVATCode.keys)
                        yield CreateTaxSummaryXML(vatCode, sortedByVATCode(vatCode))}
                }
                {CreateDocumentSummaryXML(storedPositions)}
            </ram:ApplicableHeaderTradeSettlement>
        return xml
    }

    // This part need to be repeated for every VAT category code
    // TODO: replace hardcoded values
    private def CreateTaxSummaryXML(vatCode: String, positions: List[StoredPosition]): scala.xml.Elem = {
        var totalAmount: Double = 0.0
        for (pos <- positions) {
            totalAmount = totalAmount + pos.netAmount
        }
        val xml = {
            <ram:ApplicableTradeTax>
                <ram:CalculatedAmount>{totalAmount}</ram:CalculatedAmount>
                <ram:TypeCode>VAT</ram:TypeCode>
                <ram:ExemptionReason>no</ram:ExemptionReason>
                <ram:BasisAmount>0.0</ram:BasisAmount>
                <ram:CategoryCode>{vatCode}</ram:CategoryCode>
                <ram:RateApplicablePercent>{VATCodeToNum(vatCode)}</ram:RateApplicablePercent>
            </ram:ApplicableTradeTax>
        }
        return xml
    }

    private def CreateDocumentSummaryXML(allPositions: List[StoredPosition]): scala.xml.Elem = {
        var totalNetAmount = 0.0
        var totalAmountWithoutVAT = 0.0
        var totalAmountWithVAT = 0.0
        var amountDue = 0.0
        for (i <- storedPositions) {
            val taxpercentage = VATCodeToNum(i.taxCode)
            totalNetAmount += i.netAmount
            totalAmountWithoutVAT += i.netAmount
            totalAmountWithVAT += i.netAmount * (1+ taxpercentage)
            totalAmountWithVAT += i.netAmount * (1+ taxpercentage)
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
