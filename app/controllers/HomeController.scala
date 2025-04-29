package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api._
import play.api.mvc._
import scala.sys.process._

import java.io.{File, PrintWriter}
import scala.xml.XML


class HomeController @Inject() (val controllerComponents: ControllerComponents) (implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(request))
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoiceLine(positionID))
  }

  def generateEInvoice(counter: Int = 0) = Action { implicit request: Request[AnyContent] =>
    val formData = request.body.asFormUrlEncoded
    val allPositionIDs: Seq[String] = formData
      .flatMap(_.get("positionIDcontainer"))
      .getOrElse(Seq.empty)
    
    def connectInput = (InputIdentifier: String) =>
      formData.flatMap(_.get(InputIdentifier).flatMap(_.headOption)).getOrElse("")

    // Connect all Inputs from the html form, except group_INVOICE-LINE, group_PRICE-DETAILS,
    // group_LINE-VAT-INFORMATION and group_ITEM-INFORMATION; those are connected in CreateXMLDataInvoicePostion()
    // group_INVOICE
    val inputInvoiceNumber = connectInput("InvoiceNumber")
    val inputInvoiceIssueDate = connectInput("InvoiceIssueDate").replace("-", "")
    val inputInvoiceTypeCode = connectInput("InvoiceTypeCode")
    val inputInvoiceCurrencyCode = connectInput("InvoiceCurrencyCode")
    val inputBuyerReference = connectInput("BuyerReference")
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
    val inputSpecificationIdentifier = connectInput("SpecificationIdentifier")
    // group_SELLER
    val inputSellerName = connectInput("SellerName")
    val inputSellerElectronicAddress = connectInput("SellerElectronicAddress")
    val inputSellerTradingName = connectInput("SellerTradingName")
    val inputSellerIdentifier = connectInput("SellerIdentifier")
    val inputSellerLegalRegistrationIdentifier = connectInput("SellerLegalRegistrationIdentifier")
    val inputSellerVATIdentifier = connectInput("SellerVATIdentifier")
    val inputSellerTaxRegistrationIdentifier = connectInput("SellerTaxRegistrationIdentifier")
    val inputSellerAdditionalLegalInformation = connectInput("SellerAdditionalLegalInformation")
    // group_SELLER-POSTAL-ADDRESS
    val inputSellerCity = connectInput("SellerCity")
    val inputSellerPostCode = connectInput("SellerPostCode")
    val inputSellerCountryCode = connectInput("SellerCountryCode")
    val inputSellerAddressLine1 = connectInput("SellerAddressLine1")
    val inputSellerAddressLine2 = connectInput("SellerAddressLine2")
    val inputSellerAddressLine3 = connectInput("SellerAddressLine3")
    val inputSellerCountrySubdivision = connectInput("SellerCountrySubdivision")
    // group_SELLER-CONTACT
    val inputSellerContactPoint = connectInput("SellerContactPoint")
    val inputSellerContactTelephoneNumber = connectInput("SellerContactTelephoneNumber")
    val inputSellerContactEmailAddress = connectInput("SellerContactEmailAddress")
    // group_BUYER
    val inputBuyerName = connectInput("BuyerName")
    val inputBuyerElectronicAddress = connectInput("BuyerElectronicAddress")
    val inputBuyerTradingName = connectInput("BuyerTradingName")
    val inputBuyerIdentifier = connectInput("BuyerIdentifier")
    val inputBuyerLegalRegistrationIdentifier = connectInput("BuyerLegalRegistrationIdentifier")
    val inputBuyerVATIdentifier = connectInput("BuyerVATIdentifier")
    // group_BUYER-POSTAL-ADDRESS
    val inputBuyerCity = connectInput("BuyerCity")
    val inputBuyerPostCode = connectInput("BuyerPostCode")
    val inputBuyerCountryCode = connectInput("BuyerCountryCode")
    val inputBuyerAddressLine1 = connectInput("BuyerAddressLine1")
    val inputBuyerAddressLine2 = connectInput("BuyerAddressLine2")
    val inputBuyerAddressLine3 = connectInput("BuyerAddressLine3")
    val inputBuyerCountrySubdivision = connectInput("BuyerCountrySubdivision")
    // group_PAYMENT-INSTRUCTIONS
    val inputPaymentMeansTypeCode = connectInput("PaymentMeansTypeCode")
    val inputPaymentMeansText = connectInput("PaymentMeansText")
    val inputRemittanceInformation = connectInput("RemittanceInformation")
    // group_DOCUMENT-TOTALS
    val inputSumOfInvoiceLineNetAmount = connectInput("SumOfInvoiceLineNetAmount")
    val inputInvoiceTotalAmountWithoutVAT = connectInput("InvoiceTotalAmountWithoutVAT")
    val inputInvoiceTotalAmountWithVAT = connectInput("InvoiceTotalAmountWithVAT")
    val inputAmountDueForPayment = connectInput("AmountDueForPayment")
    val inputSumOfAllowancesOnDocumentLevel = connectInput("SumOfAllowancesOnDocumentLevel")
    val inputSumOfChargesOnDocumentLevel = connectInput("SumOfChargesOnDocumentLevel")
    val inputInvoiceTotalVATAmount = connectInput("InvoiceTotalVATAmount")
    val inputInvoiceTotalVATAmountInAccountingCurrency = connectInput("InvoiceTotalVATAmountInAccountingCurrency")
    val inputPaidAmount = connectInput("PaidAmount")
    val inputRoundingAmount = connectInput("RoundingAmount")

    // Check how to set <ram:SpecifiedLineTradeSettlement> <ram:ApplicableTradeTax> Typecode
    // Gets repeated for every invoice position
    def CreateXMLDataInvoicePostion(i: String) = {
      <ram:IncludedSupplyChainTradeLineItem>
        <ram:AssociatedDocumentLineDocument>
          <ram:LineID>{connectInput("InvoiceLineIdentifier" ++ i)}</ram:LineID>
          {val value = connectInput("InvoiceLineNote"++i)
            if (value != "") {
            <ram:IncludedNote>
              <ram:Content>{value}</ram:Content>
            </ram:IncludedNote>
          } else {
            scala.xml.NodeSeq.Empty
          }
        }
        </ram:AssociatedDocumentLineDocument>
        <ram:SpecifiedTradeProduct>
          <ram:Name>{connectInput("ItemName" ++ i)}</ram:Name>
        </ram:SpecifiedTradeProduct>
        <ram:SpecifiedLineTradeAgreement>
          <ram:NetPriceProductTradePrice>
            <ram:ChargeAmount>{connectInput("ItemNetPrice" ++ i)}</ram:ChargeAmount>
          </ram:NetPriceProductTradePrice>
        </ram:SpecifiedLineTradeAgreement>
        <ram:SpecifiedLineTradeDelivery>
          <ram:BilledQuantity unitCode={connectInput("InvoicedQuantityUnitOfMeasureCode" ++ i)}>{connectInput("InvoicedQuantity" ++ i)}</ram:BilledQuantity>
        </ram:SpecifiedLineTradeDelivery>
        <ram:SpecifiedLineTradeSettlement>
          <ram:ApplicableTradeTax>
            <ram:TypeCode>VAT</ram:TypeCode>
            <ram:CategoryCode>{connectInput("InvoicedItemVATCategoryCode" ++ i)}</ram:CategoryCode>
          </ram:ApplicableTradeTax>
          <ram:SpecifiedTradeSettlementLineMonetarySummation>
            <ram:LineTotalAmount>{connectInput("InvoiceLineNetAmount" ++ i)}</ram:LineTotalAmount>
          </ram:SpecifiedTradeSettlementLineMonetarySummation>
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
            <ram:ID>{inputSpecificationIdentifier}</ram:ID>
          </ram:GuidelineSpecifiedDocumentContextParameter>
        </rsm:ExchangedDocumentContext>
        <rsm:ExchangedDocument>
          <ram:ID>{inputInvoiceNumber}</ram:ID>
          <ram:TypeCode>{inputInvoiceTypeCode}</ram:TypeCode>
          <ram:IssueDateTime>
            <udt:DateTimeString format="102">{inputInvoiceIssueDate}</udt:DateTimeString>
          </ram:IssueDateTime>
        </rsm:ExchangedDocument>
        <rsm:SupplyChainTradeTransaction>
          {for (i <- allPositionIDs)
            yield CreateXMLDataInvoicePostion(i)}
          <ram:ApplicableHeaderTradeAgreement>
            <ram:BuyerReference>{inputBuyerReference}</ram:BuyerReference>
            <ram:SellerTradeParty>
              <ram:GlobalID schemeID="0088">{inputSellerIdentifier}</ram:GlobalID>
              <ram:Name>{inputSellerName}</ram:Name>
              <ram:DefinedTradeContact>
                <ram:PersonName>{inputSellerContactPoint}</ram:PersonName>
                <ram:TelephoneUniversalCommunication>
                  <ram:CompleteNumber>{inputSellerContactTelephoneNumber}</ram:CompleteNumber>
                </ram:TelephoneUniversalCommunication>
                <ram:EmailURIUniversalCommunication>
                  <ram:URIID>{inputSellerContactEmailAddress}</ram:URIID>
                </ram:EmailURIUniversalCommunication>
              </ram:DefinedTradeContact>
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{inputSellerPostCode}</ram:PostcodeCode>
                <ram:CityName>{inputSellerCity}</ram:CityName>
                <ram:CountryID>{inputSellerCountryCode}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{inputSellerElectronicAddress}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:SellerTradeParty>
            <ram:BuyerTradeParty>
              <ram:Name>{inputBuyerName}</ram:Name>
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{inputBuyerPostCode}</ram:PostcodeCode>
                <ram:CityName>{inputBuyerCity}</ram:CityName>
                <ram:CountryID>{inputBuyerCountryCode}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{inputBuyerElectronicAddress}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:BuyerTradeParty>
          </ram:ApplicableHeaderTradeAgreement>
          <ram:ApplicableHeaderTradeDelivery>
          </ram:ApplicableHeaderTradeDelivery>
          <ram:ApplicableHeaderTradeSettlement>
            <ram:InvoiceCurrencyCode>{inputInvoiceCurrencyCode}</ram:InvoiceCurrencyCode>
            <ram:SpecifiedTradeSettlementPaymentMeans>
              <ram:TypeCode>{inputPaymentMeansTypeCode}</ram:TypeCode>
            </ram:SpecifiedTradeSettlementPaymentMeans>
              {for (i <- List(""))
                yield CreateXMLDataPositionTax(i)}
            <ram:SpecifiedTradeSettlementHeaderMonetarySummation>
              <ram:LineTotalAmount>{inputSumOfInvoiceLineNetAmount}</ram:LineTotalAmount>
              <ram:TaxBasisTotalAmount>{inputInvoiceTotalAmountWithoutVAT}</ram:TaxBasisTotalAmount>
              <ram:GrandTotalAmount>{inputInvoiceTotalAmountWithVAT}</ram:GrandTotalAmount>
              <ram:DuePayableAmount>{inputAmountDueForPayment}</ram:DuePayableAmount>
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
