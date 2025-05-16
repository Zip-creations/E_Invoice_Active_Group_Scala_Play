package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api._
import play.api.mvc._
import scala.sys.process._

import java.io.{File, PrintWriter}
import scala.xml.XML

import utility._

class HomeController @Inject() (val controllerComponents: ControllerComponents) (implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    //Ok(views.html.index(request))
    Ok(views.html.index2())
  }

  def generateInvoiceItem() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.invoice_item())
  }

  def generateInvoiceTime() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.invoice_time())
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoiceLine(positionID))
  }

  def generateEInvoice(counter: Int = 0) = Action { implicit request: Request[AnyContent] =>
    val formData = request.body.asFormUrlEncoded
    val allPositionIDs: Seq[String] = formData
      .flatMap(_.get("positionIDcontainer"))
      .getOrElse(Seq.empty)

    def connectInput = (inputIdentifier: String) =>
      formData.flatMap(_.get(inputIdentifier).flatMap(_.headOption)).getOrElse("")

    def connectOptionalInput(value: String, xml: scala.xml.Elem): scala.xml.NodeSeq = {
      if (value != "") {
        return xml
      } else {
        return scala.xml.NodeSeq.Empty
      }
    }

    def ProccessBuyerToCII(buyer: Buyer): scala.xml.Elem = {
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
    // Connect all Inputs from the html form, except group_INVOICE-LINE, group_PRICE-DETAILS,
    // group_LINE-VAT-INFORMATION and group_ITEM-INFORMATION; those are connected in CreateXMLDataInvoicePostion()
    // group_INVOICE
    val inputInvoiceNumber = connectInput("InvoiceNumber")
    val inputVATAccountingCurrencyCode = connectInput("VATAccountingCurrencyCode")
    val inputValueAddedTaxPointDate = connectInput("ValueAddedTaxPointDate")
    val inputValueAddedTaxPointDateCode = connectInput("ValueAddedTaxPointDateCode")
    val inputPaymentDueDate = connectInput("PaymentDueDate")
    val inputProjectReference = connectInput("ProjectReference")
    val inputContractReference = connectInput("ContractReference")
    val inputPurchaseOrderReference = connectInput("PurchaseOrderReference")
    val inputSalesOrderReference = connectInput("SalesOrderReference")
    val inputReceivingAdviceReference = connectInput("ReceivingAdviceReference")
    val inputDespatchAdviceReference = connectInput("DespatchAdviceReference")
    val inputTenderOrLotReference = connectInput("TenderOrLotReference")
    val inputInvoicedObjectIdentifier = connectInput("InvoicedObjectIdentifier")
    val inputBuyerAccountingReference = connectInput("BuyerAccountingReference")
    val inputPaymentTerms = connectInput("PaymentTerms")
    // group_PROCESS-CONTROL
    val inputBusinessProcessType = connectInput("BusinessProcessType")
    // group_SELLER
    val inputSellerTradingName = connectInput("SellerTradingName")
    val inputSellerIdentifier = connectInput("SellerIdentifier")
    val inputSellerLegalRegistrationIdentifier = connectInput("SellerLegalRegistrationIdentifier")
    val inputSellerVATIdentifier = connectInput("SellerVATIdentifier")
    val inputSellerTaxRegistrationIdentifier = connectInput("SellerTaxRegistrationIdentifier")
    val inputSellerAdditionalLegalInformation = connectInput("SellerAdditionalLegalInformation")
    // group_SELLER-POSTAL-ADDRESS
    val inputSellerAddressLine1 = connectInput("SellerAddressLine1")
    val inputSellerAddressLine2 = connectInput("SellerAddressLine2")
    val inputSellerAddressLine3 = connectInput("SellerAddressLine3")
    val inputSellerCountrySubdivision = connectInput("SellerCountrySubdivision")
    // group_BUYER
    val inputBuyerTradingName = connectInput("BuyerTradingName")
    val inputBuyerIdentifier = connectInput("BuyerIdentifier")
    val inputBuyerLegalRegistrationIdentifier = connectInput("BuyerLegalRegistrationIdentifier")
    val inputBuyerVATIdentifier = connectInput("BuyerVATIdentifier")
    // group_BUYER-POSTAL-ADDRESS
    val inputBuyerAddressLine1 = connectInput("BuyerAddressLine1")
    val inputBuyerAddressLine2 = connectInput("BuyerAddressLine2")
    val inputBuyerAddressLine3 = connectInput("BuyerAddressLine3")
    val inputBuyerCountrySubdivision = connectInput("BuyerCountrySubdivision")
    // group_PAYMENT-INSTRUCTIONS
    val inputPaymentMeansText = connectInput("PaymentMeansText")
    val inputRemittanceInformation = connectInput("RemittanceInformation")
    // group_DOCUMENT-TOTALS
    val inputSumOfAllowancesOnDocumentLevel = connectInput("SumOfAllowancesOnDocumentLevel")
    val inputSumOfChargesOnDocumentLevel = connectInput("SumOfChargesOnDocumentLevel")
    val inputInvoiceTotalVATAmount = connectInput("InvoiceTotalVATAmount")
    val inputInvoiceTotalVATAmountInAccountingCurrency = connectInput("InvoiceTotalVATAmountInAccountingCurrency")
    val inputPaidAmount = connectInput("PaidAmount")
    val inputRoundingAmount = connectInput("RoundingAmount")

    // Gets repeated for every invoice position
    def CreateXMLDataInvoicePostion(i: String) = {
      <ram:IncludedSupplyChainTradeLineItem>
        <ram:AssociatedDocumentLineDocument>
          <ram:LineID>{connectInput("InvoiceLineIdentifier"++i)}</ram:LineID>
          {val value = connectInput("InvoiceLineNote"++i)
            val xml = 
              <ram:IncludedNote>
                <ram:Content>{value}</ram:Content>
              </ram:IncludedNote>
            connectOptionalInput(value, xml)}
        </ram:AssociatedDocumentLineDocument>
        <ram:SpecifiedTradeProduct>
          {
            val value = connectInput("ItemStandardIdentifier"++i)
            val xml =
              <ram:GlobalID schemeID="0088">{value}</ram:GlobalID>
            connectOptionalInput(value, xml)
          }
          {
            val value = connectInput("ItemSellersIdentifier"++i)
            val xml =
              <ram:SellerAssignedID>{value}</ram:SellerAssignedID>
            connectOptionalInput(value, xml)
          }
          {
            val value = connectInput("ItemBuyersIdentifier"++i)
            val xml =
              <ram:BuyerAssignedID>{value}</ram:BuyerAssignedID>
            connectOptionalInput(value, xml)
          }
          <ram:Name>{connectInput("ItemName" ++ i)}</ram:Name>
          {
            val value = connectInput("ItemDescription"++i)
            val xml =
              <ram:Description>{value}</ram:Description>
            connectOptionalInput(value, xml)
          }
          {
            val value = connectInput("ItemClassificationIdentifier"++i)
            val xml =
              <ram:DesignatedProductClassification>
                <ram:ClassCode listID={connectInput("itemClassificationIdentifierScheme"++i)}>{value}</ram:ClassCode>
              </ram:DesignatedProductClassification>
            connectOptionalInput(value, xml)
          }
        </ram:SpecifiedTradeProduct>
        <ram:SpecifiedLineTradeAgreement>
          {
            val value = connectInput("ReferencedPurchaseOrderLineReference"++i)
              val xml= 
              <ram:BuyerOrderReferenceDocument>
                <ram:LineID>{value}</ram:LineID>
              </ram:BuyerOrderReferenceDocument>
            connectOptionalInput(value, xml)}
          {
            val inputItemPriceDiscount = connectInput("ItemPriceDiscount"++i)
            val inputItemGrossPrice = connectInput("ItemGrossPrice"++i)
            val inputItemPriceBaseQuantity = connectInput("ItemPriceBaseQuantity"++i)
            val inputItemPriceBaseQuantityUnitOfMeasureCode = connectInput("ItemPriceBaseQuantityUnitOfMeasureCode"++i)
            // inputItemPriceBaseQuantityUnitOfMeasureCode is dependant on inputItemPriceBaseQuantity
            if (inputItemPriceDiscount != "" || inputItemGrossPrice != "" || inputItemPriceBaseQuantity != "") {
              {
                val xml =
                <ram:GrossPriceProductTradePrice>
                  {
                    val xmlGrossPrice =
                      <ram:ChargeAmount>{inputItemGrossPrice}</ram:ChargeAmount>
                    connectOptionalInput(inputItemGrossPrice, xmlGrossPrice)
                  }
                  {
                    val xmlQuantity =
                      <ram:BasisQuantity unitCode={inputItemPriceBaseQuantityUnitOfMeasureCode}>{inputItemPriceBaseQuantity}</ram:BasisQuantity>
                    connectOptionalInput(inputItemPriceBaseQuantity, xmlQuantity)
                  }
                  {
                    val xmlDiscount = 
                    <ram:AppliedTradeAllowanceCharge>
                      <ram:ChargeIndicator>
                        <udt:Indicator>false</udt:Indicator>
                      </ram:ChargeIndicator>
                      <ram:ActualAmount>{inputItemPriceDiscount}</ram:ActualAmount>
                    </ram:AppliedTradeAllowanceCharge>
                    connectOptionalInput(inputItemPriceDiscount, xmlDiscount)
                  }
                </ram:GrossPriceProductTradePrice>
                xml
              }
            } else {
              scala.xml.NodeSeq.Empty
            }
          }
          <ram:NetPriceProductTradePrice>
            <ram:ChargeAmount>{connectInput("ItemNetPrice" ++ i)}</ram:ChargeAmount>
          </ram:NetPriceProductTradePrice>
        </ram:SpecifiedLineTradeAgreement>
        <ram:SpecifiedLineTradeDelivery>
          <ram:BilledQuantity unitCode={connectInput("InvoicedQuantityUnitOfMeasureCode" ++ i)}>{connectInput("InvoicedQuantity" ++ i)}</ram:BilledQuantity>
        </ram:SpecifiedLineTradeDelivery>
        <ram:SpecifiedLineTradeSettlement>
          <ram:ApplicableTradeTax>
            {
              // TypeCode=VAT is determined in the EN 16931 - CII Mapping scheme
            }
            <ram:TypeCode>VAT</ram:TypeCode>
            <ram:CategoryCode>{connectInput("InvoicedItemVATCategoryCode" ++ i)}</ram:CategoryCode>
            {
              val value = connectInput("InvoicedItemVATRate"++i)
              val xml = 
                <ram:RateApplicablePercent>{value}</ram:RateApplicablePercent>
              connectOptionalInput(value, xml)
            }
          </ram:ApplicableTradeTax>
          <ram:SpecifiedTradeSettlementLineMonetarySummation>
            <ram:LineTotalAmount>{connectInput("InvoiceLineNetAmount" ++ i)}</ram:LineTotalAmount>
          </ram:SpecifiedTradeSettlementLineMonetarySummation>
          {
            val value = connectInput("InvoiceLineObjectIdentifier"++i)
            // TypeCode=130 is determined in the EN 16931 - CII Mapping scheme
            val xml = 
              <ram:AdditionalReferencedDocument>
                <ram:IssuerAssignedID>{value}</ram:IssuerAssignedID>
                <ram:TypeCode>130</ram:TypeCode>
              </ram:AdditionalReferencedDocument>
            connectOptionalInput(value, xml)
          }
        </ram:SpecifiedLineTradeSettlement>
      </ram:IncludedSupplyChainTradeLineItem>
    }

    // This part need to be repeated for every VAT category code
    def CreateXMLDataPositionTax(i: String): scala.xml.Elem = { 
      val xmlData = 
        <ram:ApplicableTradeTax>
          <ram:CalculatedAmount>{connectInput("VATCategoryTaxAmount" ++ i)}</ram:CalculatedAmount>
          <ram:TypeCode>VAT</ram:TypeCode>
          <ram:ExemptionReason>{connectInput("VATExemptionReasonText" ++ i)}</ram:ExemptionReason>
          <ram:BasisAmount>{connectInput("VATCategoryTaxableAmount" ++ i)}</ram:BasisAmount>
          <ram:CategoryCode>{connectInput("VATCategoryCode" ++ i)}</ram:CategoryCode>
          <ram:RateApplicablePercent>{connectInput("VATCategoryRate" ++ i)}</ram:RateApplicablePercent>
        </ram:ApplicableTradeTax>
      return xmlData
    }

    // {for (i <- List("")) yield CreateXMLDataPositionTax(i)} is a placeholder, until the full set of VAT category codes is supported
    // TODO: inputSellerIdentifier: Find out how to set schemeID
    val xmlData =
      <rsm:CrossIndustryInvoice xmlns:rsm="urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:qdt="urn:un:unece:uncefact:data:standard:QualifiedDataType:100" xmlns:ram="urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:100" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:100">
        <rsm:ExchangedDocumentContext>
          <ram:BusinessProcessSpecifiedDocumentContextParameter>
            <ram:ID>urn:fdc:peppol.eu:2017:poacc:billing:01:1.0</ram:ID>
          </ram:BusinessProcessSpecifiedDocumentContextParameter>
          <ram:GuidelineSpecifiedDocumentContextParameter>
            <ram:ID>urn:cen.eu:en16931:2017#compliant#urn:xeinkauf.de:kosit:xrechnung_3.0</ram:ID>
          </ram:GuidelineSpecifiedDocumentContextParameter>
        </rsm:ExchangedDocumentContext>
        <rsm:ExchangedDocument>
          <ram:ID>
            {inputInvoiceNumber}
          </ram:ID>
          <ram:TypeCode>{connectInput("InvoiceTypeCode")}</ram:TypeCode>
          <ram:IssueDateTime>
            {
              // format="102" is determined in the EN 16931 - CII Mapping scheme
            }
            <udt:DateTimeString format="102">
              {connectInput("")}
            </udt:DateTimeString>
          </ram:IssueDateTime>
        </rsm:ExchangedDocument>
        <rsm:SupplyChainTradeTransaction>
          {for (i <- allPositionIDs)
            yield CreateXMLDataInvoicePostion(i)}
          <ram:ApplicableHeaderTradeAgreement>
            <ram:BuyerReference>{connectInput("BuyerReference")}</ram:BuyerReference>
            <ram:SellerTradeParty>
              {
                val value = connectInput("SellerIdentifier")
                val xml = <ram:GlobalID schemeID="0088">{value}</ram:GlobalID>
                connectOptionalInput(value, xml)
              }
              <ram:Name>{connectInput("SellerName")}</ram:Name>
              <ram:DefinedTradeContact>
                <ram:PersonName>{connectInput("SellerContactPoint")}</ram:PersonName>
                <ram:TelephoneUniversalCommunication>
                  <ram:CompleteNumber>{connectInput("SellerContactTelephoneNumber")}</ram:CompleteNumber>
                </ram:TelephoneUniversalCommunication>
                <ram:EmailURIUniversalCommunication>
                  <ram:URIID>{connectInput("SellerContactEmailAddress")}</ram:URIID>
                </ram:EmailURIUniversalCommunication>
              </ram:DefinedTradeContact>
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{connectInput("SellerPostCode")}</ram:PostcodeCode>
                <ram:CityName>{connectInput("SellerCity")}</ram:CityName>
                <ram:CountryID>{connectInput("SellerCountryCode")}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{connectInput("SellerElectronicAddress")}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:SellerTradeParty>
            <ram:BuyerTradeParty>
              <ram:Name>{connectInput("BuyerName")}</ram:Name>
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{connectInput("BuyerPostCode")}</ram:PostcodeCode>
                <ram:CityName>{connectInput("BuyerCity")}</ram:CityName>
                <ram:CountryID>{connectInput("BuyerCountryCode")}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{connectInput("BuyerElectronicAddress")}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:BuyerTradeParty>
          </ram:ApplicableHeaderTradeAgreement>
          <ram:ApplicableHeaderTradeDelivery>
          </ram:ApplicableHeaderTradeDelivery>
          <ram:ApplicableHeaderTradeSettlement>
            <ram:InvoiceCurrencyCode>{connectInput("InvoiceCurrencyCode")}</ram:InvoiceCurrencyCode>
            <ram:SpecifiedTradeSettlementPaymentMeans>
              <ram:TypeCode>{connectInput("PaymentMeansTypeCode")}</ram:TypeCode>
            </ram:SpecifiedTradeSettlementPaymentMeans>
              {for (i <- List(""))
                yield CreateXMLDataPositionTax(i)}
            <ram:SpecifiedTradeSettlementHeaderMonetarySummation>
              <ram:LineTotalAmount>{connectInput("SumOfInvoiceLineNetAmount")}</ram:LineTotalAmount>
              <ram:TaxBasisTotalAmount>{connectInput("InvoiceTotalAmountWithoutVAT")}</ram:TaxBasisTotalAmount>
              <ram:GrandTotalAmount>{connectInput("InvoiceTotalAmountWithVAT")}</ram:GrandTotalAmount>
              <ram:DuePayableAmount>{connectInput("AmountDueForPayment")}</ram:DuePayableAmount>
            </ram:SpecifiedTradeSettlementHeaderMonetarySummation>
          </ram:ApplicableHeaderTradeSettlement>
        </rsm:SupplyChainTradeTransaction>
      </rsm:CrossIndustryInvoice>


    // Paths
    val invoiceName = new File(s"eInvoice_$inputInvoiceNumber").getPath 
    val invoicePathXML = new File(s"./output/xml/$invoiceName.xml").getPath
    val invoicePathPDF = new File(s"./output/pdf/$invoiceName.pdf").getPath
    val reportPath = new File(s"app/views/validation_reports/${invoiceName}_validation.html").getPath
    
    // Store invoice as .xml
    val writer = new PrintWriter(new File(invoicePathXML))
    writer.write("<?xml version='1.0' encoding='UTF-8'?>\n" ++ xmlData.toString() ++ "\n")
    writer.close()

    // Location of execution
    val directory = new File("Toolbox")

    // Call the .jar from the Toolbox, create .pdf from the .xml and store the .pdf
    val createPDF = s"java -Dlog4j2.configurationFile=./resources/log4j2.xml -jar OpenXRechnungToolbox.jar -viz -i .$invoicePathXML -o ../$invoicePathPDF -p"
    val executeCreatePDF = createPDF.!
    Process(createPDF, directory).!

    // Call the .jar from the Toolbox, create report and store it as .html
    val createReport = s"java -Dlog4j2.configurationFile=./resources/log4j2.xml -jar OpenXRechnungToolbox.jar -val -i .$invoicePathXML -o ../$reportPath -v 3.0.2"
    val executeCreateReport = createReport.!
    Process(createReport, directory).!

    // Open the .html report
    Ok.sendFile(new java.io.File(reportPath))
  }
}
