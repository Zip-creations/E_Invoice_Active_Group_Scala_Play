package utility
import scala.collection.mutable
import scala.xml.NodeSeq
class XMLUtility(){

    private case class StoredPosition(netAmount: Double, vatCode: String, vatRate: Double)
    private var storedPositions: List[StoredPosition] = Nil

    // Assumes the vatRate in this format: A tax of 19.7% is given as 19.7
    // returns the value with a leading 1; e.g.
    // 19 -> 1.19
    // 2.7 -> 1.027
    private def getVATvalue(vatRate: Double): Double = {
        return (vatRate / 100) +1
    }

    // Round to nearest two digits after comma
    private def roundAmount(num: Double): Double = {
        return (math.rint(num * 100) / 100)
    }

    private def insertOptionalInput(value: String, xml: scala.xml.Elem): scala.xml.NodeSeq = {
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
                {CreateInvolvedPartiesXML(invoice.involvedParties)}
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

    private def CreateInvolvedPartiesXML(parties: InvoiceInvolvedParties): scala.xml.Elem = {
        val xml =
            <ram:ApplicableHeaderTradeAgreement>
                <ram:BuyerReference>{parties.buyer.reference}</ram:BuyerReference>
                {CreateSellerXML(parties.seller, parties.sellerContact)}
                {CreateBuyerXML(parties.buyer)}
            </ram:ApplicableHeaderTradeAgreement>
        return xml
    }

    private def CreateSellerXML(seller: InvoiceSeller, sellerContact: InvoiceSellerContact): scala.xml.Elem = {
        val xml =
            <ram:SellerTradeParty>
              <ram:Name>{seller.name}</ram:Name>
                {CreateSellerContactXML(sellerContact)}
                {CreateAddressXML(seller.address)}
                <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{seller.email}</ram:URIID>
                </ram:URIUniversalCommunication>
                <ram:SpecifiedTaxRegistration>
                    <ram:ID schemeID="VA">{seller.vatIdentifier}</ram:ID>
                </ram:SpecifiedTaxRegistration>
            </ram:SellerTradeParty>
        return xml
    }

    private def CreateAddressXML(address: Address): scala.xml.Elem = {
        val xml = 
            <ram:PostalTradeAddress>
                <ram:PostcodeCode>{address.postCode}</ram:PostcodeCode>
                <ram:CityName>{address.city}</ram:CityName>
                <ram:CountryID>{address.countryCode}</ram:CountryID>
            </ram:PostalTradeAddress>
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
                {CreateAddressXML(buyer.address)}
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
                unitcode = "HUR"  // Code for "hour" (see https://www.xrepository.de/details/urn:xoev-de:kosit:codeliste:rec20_3) ("labour hour" is LH)
                chargedAmount = hourlyrate
                chargedQuantity = 1
                totalAmount = hours * hourlyrate
            case InvoicePositionData.Leistungsposition(quantity, pricePerPart, measurementCode) =>
                unitcode = measurementCode
                chargedAmount = pricePerPart
                chargedQuantity = quantity
                totalAmount = quantity * pricePerPart
        }
        storedPositions = storedPositions :+ StoredPosition(totalAmount, position.vatCode, position.vatRate)

        val xml =
            <ram:IncludedSupplyChainTradeLineItem>
                <ram:AssociatedDocumentLineDocument>
                    <ram:LineID>{position.id}</ram:LineID>
                </ram:AssociatedDocumentLineDocument>
                <ram:SpecifiedTradeProduct>
                    <ram:Name>{position.name}</ram:Name>
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
                        <ram:CategoryCode>{position.vatCode}</ram:CategoryCode>
                        <ram:RateApplicablePercent>{position.vatRate}</ram:RateApplicablePercent>
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
                    var sortedByVATCode: mutable.Map[(String, Double), List[StoredPosition]] = mutable.Map.empty
                    for (pos <- storedPositions) {
                        val currentPos = sortedByVATCode.getOrElse((pos.vatCode, pos.vatRate), List.empty)
                        sortedByVATCode.update((pos.vatCode, pos.vatRate), currentPos :+ pos)
                    }
                    {for (vatInfo <- sortedByVATCode.keys)
                        yield CreateTaxSummaryXML(vatInfo, sortedByVATCode(vatInfo), paymentInfo.vatExemptionReason)}
                }
                {
                    val value = paymentInfo.paymentTerms
                    val xml =
                        <ram:SpecifiedTradePaymentTerms>
                            <ram:Description>{value}</ram:Description>
                        </ram:SpecifiedTradePaymentTerms>
                    insertOptionalInput(value, xml)
                }
                {CreateDocumentSummaryXML(storedPositions, paymentInfo.currencycode)}
            </ram:ApplicableHeaderTradeSettlement>
        return xml
    }

    // This part need to be repeated for every VAT category code
    // TODO: replace hardcoded values
    private def CreateTaxSummaryXML(vatInfo: (String, Double), positions: List[StoredPosition], vatExemptionReason: String): scala.xml.Elem = {
        var totalAmount = 0.0
        var totalAmountWithVAT = 0.0
        var totalVATAmount = 0.0
        for (pos <- positions) {
            val vatValue = getVATvalue(pos.vatRate)
            totalAmount = totalAmount + pos.netAmount
            totalAmountWithVAT = totalAmountWithVAT + pos.netAmount * vatValue
            totalVATAmount = totalVATAmount + (pos.netAmount * vatValue - pos.netAmount)
        }
        val xml = {
                <ram:ApplicableTradeTax>
                    <ram:CalculatedAmount>{roundAmount(totalVATAmount)}</ram:CalculatedAmount>
                    <ram:TypeCode>VAT</ram:TypeCode>
                    {
                        var value = vatExemptionReason
                        if ("SZLM".contains(vatInfo(0))){value = ""} // S / Z / L / M are the codes that can not contain an exemption reason. All other codes require one.
                        val xml= 
                            <ram:ExemptionReason>{value}</ram:ExemptionReason>
                        insertOptionalInput(value, xml)
                    }
                    <ram:BasisAmount>{roundAmount(totalAmount)}</ram:BasisAmount>
                    <ram:CategoryCode>{vatInfo(0)}</ram:CategoryCode>
                    <ram:RateApplicablePercent>{vatInfo(1)}</ram:RateApplicablePercent>
                </ram:ApplicableTradeTax>
        }
        return xml
    }

    private def CreateDocumentSummaryXML(allPositions: List[StoredPosition], currencycode: String): scala.xml.Elem = {
        var totalNetAmount = 0.0
        var totalVATAmount = 0.0
        var totalAmountWithoutVAT = 0.0
        var totalAmountWithVAT = 0.0
        var amountDue = 0.0
        for (i <- storedPositions) {
            val taxpercentage = getVATvalue(i.vatRate)
            totalNetAmount += i.netAmount
            totalVATAmount += i.netAmount * taxpercentage - i.netAmount
            totalAmountWithoutVAT += i.netAmount
            totalAmountWithVAT += i.netAmount * taxpercentage
        }
        val xml =
                <ram:SpecifiedTradeSettlementHeaderMonetarySummation>
                    <ram:LineTotalAmount>{roundAmount(totalNetAmount)}</ram:LineTotalAmount>
                    <ram:TaxBasisTotalAmount>{roundAmount(totalAmountWithoutVAT)}</ram:TaxBasisTotalAmount>
                    <ram:TaxTotalAmount currencyID={currencycode}>{roundAmount(totalVATAmount)}</ram:TaxTotalAmount>
                    <ram:GrandTotalAmount>{roundAmount(totalAmountWithVAT)}</ram:GrandTotalAmount>
                    <ram:DuePayableAmount>{roundAmount(totalAmountWithVAT)}</ram:DuePayableAmount>
                </ram:SpecifiedTradeSettlementHeaderMonetarySummation>
        return xml
    }
}
