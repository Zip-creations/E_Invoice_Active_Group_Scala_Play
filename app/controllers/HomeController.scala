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
    Ok(views.html.index())
  }

  def generateInvoiceItem() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.invoice_item())
  }

  def generateInvoiceTime() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.invoice_time())
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.index())
  }

  def generateEInvoice(counter: Int = 0) = Action { implicit request: Request[AnyContent] =>
    var xmlUtil = XMLUtility()
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

    val meta = InvoiceMetaData(
      connectInput("InvoiceNumber"),
      connectInput("InvoiceIssueDate").replace("-", ""),
      "380" // TODO: replace with a value the user can set OR replace with fitting default
      )

    val seller = InvoiceSeller(
      connectInput("SellerName"),
      connectInput("SellerAddressLine1"),
      connectInput("SellerPostCode"),
      connectInput("SellerCity"),
      connectInput("placeholder1"),
      connectInput("placeholder2"),
      connectInput("placeholder3"),
      connectInput("SellerElectronicAddress"),
      )

    val sellerContact = InvoiceSellerContact(
      connectInput("SellerContactPoint"),
      connectInput("SellerContactTelephoneNumber"),
      connectInput("SellerContactEmailAddress"),
      )

    val buyer = InvoiceBuyer(
      connectInput(""),
      connectInput(""),
      connectInput(""),
      connectInput(""),
      connectInput(""),
      connectInput("")
      )

    // only for testting purposes - postions need to be created dynamically and in an abritrary number later on!
    val testposition = InvoicePosition.Stundenposition(
      "123",
      19,
      12,
      42.42
    )

    val invoice = Invoice(meta, seller, sellerContact, buyer, Array(testposition).toList)

    // Connect all Inputs from the html form, except group_INVOICE-LINE, group_PRICE-DETAILS,
    // group_LINE-VAT-INFORMATION and group_ITEM-INFORMATION; those are connected in CreateXMLDataInvoicePostion()
    // group_INVOICE

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
          {xmlUtil.CreateMetaDataXML(invoice.metadata)}
        <rsm:SupplyChainTradeTransaction>
          {for (i <- invoice.positions)
            yield xmlUtil.CreatePositionXML(i)}
          <ram:ApplicableHeaderTradeAgreement>
            <ram:BuyerReference>{connectInput("BuyerReference")}</ram:BuyerReference>
            <ram:SellerTradeParty>
              {
                val value = connectInput("SellerIdentifier")
                val xml = <ram:GlobalID schemeID="0088">{value}</ram:GlobalID>
                connectOptionalInput(value, xml)
              }
              <ram:Name>{connectInput("SellerName")}</ram:Name>
                {xmlUtil.CreateSellerContactXML(invoice.sellerContact)}
              <ram:PostalTradeAddress>
                <ram:PostcodeCode>{connectInput("SellerPostCode")}</ram:PostcodeCode>
                <ram:CityName>{connectInput("SellerCity")}</ram:CityName>
                <ram:CountryID>{connectInput("SellerCountryCode")}</ram:CountryID>
              </ram:PostalTradeAddress>
              <ram:URIUniversalCommunication>
                <ram:URIID schemeID="EM">{connectInput("SellerElectronicAddress")}</ram:URIID>
              </ram:URIUniversalCommunication>
            </ram:SellerTradeParty>
            {xmlUtil.CreateBuyerXML(invoice.buyer)}
          </ram:ApplicableHeaderTradeAgreement>
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
    val inputInvoiceNumber = connectInput("InvoiceNumber")
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
