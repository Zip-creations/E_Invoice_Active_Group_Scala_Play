package utility

import codelists._
import scala.xml.NodeSeq


class XMLUtility(){

    private case class StoredPosition(netAmount: Double, vatId: VATCategoryIdentifier)
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
                {meta.identifier.id}
                </ram:ID>
                <ram:TypeCode>{meta.invoiceType.typeCode}</ram:TypeCode>
                <ram:IssueDateTime>
                {
                    // format="102" is determined in the EN 16931 - CII Mapping scheme
                }
                <udt:DateTimeString format="102">
                    {meta.date.date}
                </udt:DateTimeString>
                </ram:IssueDateTime>
            </rsm:ExchangedDocument>
        return xml
    }

    private def CreateInvolvedPartiesXML(parties: InvoiceInvolvedParties): scala.xml.Elem = {
        val xml =
            <ram:ApplicableHeaderTradeAgreement>
                <ram:BuyerReference>{parties.buyer.reference.ref}</ram:BuyerReference>
                {CreateSellerXML(parties.seller, parties.sellerContact)}
                {CreateBuyerXML(parties.buyer)}
            </ram:ApplicableHeaderTradeAgreement>
        return xml
    }

    private def CreateSellerXML(seller: InvoiceSeller, sellerContact: InvoiceSellerContact): scala.xml.Elem = {
        val xml =
            <ram:SellerTradeParty>
              <ram:Name>{seller.name.name}</ram:Name>
                {CreateSellerContactXML(sellerContact)}
                {CreateAddressXML(seller.address)}
                <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{seller.email.email}</ram:URIID>
                </ram:URIUniversalCommunication>
                <ram:SpecifiedTaxRegistration>
                    <ram:ID schemeID="VA">{seller.vatIdentifier.id}</ram:ID>
                </ram:SpecifiedTaxRegistration>
            </ram:SellerTradeParty>
        return xml
    }

    private def CreateAddressXML(address: Address): scala.xml.Elem = {
        val xml = 
            <ram:PostalTradeAddress>
                <ram:PostcodeCode>{address.postCode.postcode}</ram:PostcodeCode>
                <ram:CityName>{address.city.city}</ram:CityName>
                <ram:CountryID>{address.countryCode}</ram:CountryID>
            </ram:PostalTradeAddress>
        return xml
    }

    private def CreateSellerContactXML(sellerContact: InvoiceSellerContact): scala.xml.Elem = {
        val xml =
            <ram:DefinedTradeContact>
                <ram:PersonName>{sellerContact.name.name}</ram:PersonName>
                <ram:TelephoneUniversalCommunication>
                    <ram:CompleteNumber>{sellerContact.telephonenumber.number}</ram:CompleteNumber>
                </ram:TelephoneUniversalCommunication>
                <ram:EmailURIUniversalCommunication>
                    <ram:URIID>{sellerContact.email.email}</ram:URIID>
                </ram:EmailURIUniversalCommunication>
                </ram:DefinedTradeContact>
        return xml
    }

    private def CreateBuyerXML(buyer: InvoiceBuyer): scala.xml.Elem = {
        val xml =
            <ram:BuyerTradeParty>
                <ram:Name>{buyer.name.name}</ram:Name>
                {CreateAddressXML(buyer.address)}
                <ram:URIUniversalCommunication>
                    <ram:URIID schemeID="EM">{buyer.email.email}</ram:URIID>
                </ram:URIUniversalCommunication>
            </ram:BuyerTradeParty>
        return xml
    }

    private def CreatePositionXML(position: InvoicePosition): scala.xml.Elem = {
        var unitcode = "" //: MeasurementCode = MeasurementCode.A10  // a default is requiered by Scala. A10 is just the lexicographical first member
        var chargedAmount = 0.0
        var totalAmount = 0.0
        var chargedQuantity = 0.0

        position.data match{
            case InvoicePositionData.Stundenposition(hours, hourlyrate) =>
                unitcode = "HUR"  // Code for "hour" (see https://www.xrepository.de/details/urn:xoev-de:kosit:codeliste:rec20_3) ("labour hour" is LH)
                chargedAmount = hourlyrate.rate
                chargedQuantity = 1
                totalAmount = hours.hours * hourlyrate.rate
            case InvoicePositionData.Leistungsposition(quantity, pricePerPart, measurementCode) =>
                unitcode = measurementCode.toString
                chargedAmount = pricePerPart.netPrice
                chargedQuantity = quantity.quantity
                totalAmount = quantity.quantity * pricePerPart.netPrice
        }
        storedPositions = storedPositions :+ StoredPosition(totalAmount, position.vatId)

        val xml =
            <ram:IncludedSupplyChainTradeLineItem>
                <ram:AssociatedDocumentLineDocument>
                    <ram:LineID>{position.id.id}</ram:LineID>
                </ram:AssociatedDocumentLineDocument>
                <ram:SpecifiedTradeProduct>
                    <ram:Name>{position.name.name}</ram:Name>
                </ram:SpecifiedTradeProduct>
                <ram:SpecifiedLineTradeAgreement>
                    <ram:NetPriceProductTradePrice>
                        <ram:ChargeAmount>{chargedAmount}</ram:ChargeAmount>
                    </ram:NetPriceProductTradePrice>
                </ram:SpecifiedLineTradeAgreement>
                <ram:SpecifiedLineTradeDelivery>
                    <ram:BilledQuantity unitCode={unitcode}>{chargedQuantity}</ram:BilledQuantity>
                </ram:SpecifiedLineTradeDelivery>
                <ram:SpecifiedLineTradeSettlement>
                    <ram:ApplicableTradeTax>
                        {
                        // TypeCode=VAT is determined in the EN 16931 - CII Mapping scheme
                        }
                        <ram:TypeCode>VAT</ram:TypeCode>
                        <ram:CategoryCode>{position.vatId.vatCode.code}</ram:CategoryCode>
                        <ram:RateApplicablePercent>{position.vatId.vatRate.rate}</ram:RateApplicablePercent>
                    </ram:ApplicableTradeTax>
                    <ram:SpecifiedTradeSettlementLineMonetarySummation>
                        <ram:LineTotalAmount>{totalAmount}</ram:LineTotalAmount>
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
                    <ram:TypeCode>{paymentInfo.paymentMeansCode.code}</ram:TypeCode>
                </ram:SpecifiedTradeSettlementPaymentMeans>
                {
                    {for (group <- paymentInfo.vatGroups)
                        yield CreateTaxSummaryXML(group)}
                }
                {
                    val value = paymentInfo.paymentTerms.terms
                    val xml =
                        <ram:SpecifiedTradePaymentTerms>
                            <ram:Description>{value}</ram:Description>
                        </ram:SpecifiedTradePaymentTerms>
                    insertOptionalInput(value, xml)
                }
                {CreateDocumentSummaryXML(storedPositions, paymentInfo.currencycode.toString)}
            </ram:ApplicableHeaderTradeSettlement>
        return xml
    }

    // This part need to be repeated for every VAT category code
    // TODO: replace hardcoded values
    private def CreateTaxSummaryXML(vatGroup: InvoiceVATGroup): scala.xml.Elem = {
        val id = vatGroup.id
        var totalAmount = 0.0
        var totalAmountWithVAT = 0.0
        var totalVATAmount = 0.0
        for (pos <- vatGroup.positions) {
            val vatValue = getVATvalue(pos.identifier.vatRate.rate)
            val amount = pos.netAmount.amount
            totalAmount = totalAmount + amount
            totalAmountWithVAT = totalAmountWithVAT + amount * vatValue
            totalVATAmount = totalVATAmount + (amount * vatValue - amount)
        }
        val xml = {
                <ram:ApplicableTradeTax>
                    <ram:CalculatedAmount>{roundAmount(totalVATAmount)}</ram:CalculatedAmount>
                    <ram:TypeCode>VAT</ram:TypeCode>
                    {
                        var value = vatGroup.vatExemptionReason.reason
                        if ("SZLM".contains(id.vatCode.code)){value = ""} // S/Z/L/M are the codes that can not contain an exemption reason. All other codes require one.
                        val xml= 
                            <ram:ExemptionReason>{value}</ram:ExemptionReason>
                        insertOptionalInput(value, xml)
                    }
                    <ram:BasisAmount>{roundAmount(totalAmount)}</ram:BasisAmount>
                    <ram:CategoryCode>{id.vatCode.code}</ram:CategoryCode>
                    <ram:RateApplicablePercent>{id.vatRate.rate}</ram:RateApplicablePercent>
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
            val taxpercentage = getVATvalue(i.vatId.vatRate.rate)
            totalNetAmount += i.netAmount
            totalVATAmount += i.netAmount * taxpercentage - i.netAmount
            totalAmountWithoutVAT += i.netAmount  // Whats the differnce to totalNetAmount?
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
