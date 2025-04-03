package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.io.{File, PrintWriter}
import scala.xml.XML


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    // val inputInvoiceNumber = dom.document.getElementById("InvoiceNumber").asInstanceOf[html.Input]
    Ok(views.html.index(request))
  }

  def generateEInvoice() = Action { implicit request: Request[AnyContent] =>
    val formData = request.body.asFormUrlEncoded
    def connectInput = (InputIdentifier: String) =>
      formData.flatMap(_.get(InputIdentifier).flatMap(_.headOption)).getOrElse("")

    // group_INVOICE
    val inputInvoiceNumber = connectInput("InvoiceNumber")
    val inputInvoiceIssueDate = connectInput("InvoiceIssueDate")
    val inputInvoiceTypeCode = connectInput("InvoiceTypeCode")
    val inputInvoiceCurrencyCode = connectInput("InvoiceCurrencyCode")
    val inputBuyerReference = connectInput("BuyerReference")
    // group_PROCESS-CONTROL
    val inputBusinessProcessType = connectInput("BusinessProcessType")
    val inputSpecificationIdentifier = connectInput("SpecificationIdentifier")
    // group_SELLER
    val inputSellerName = connectInput("SellerName")
    val inputSellerElectronicAddress = connectInput("SellerElectronicAddress")
    // group_SELLER-POSTAL-ADDRESS
    val inputSellerCity = connectInput("SellerCity")
    val inputSellerPostCode = connectInput("SellerPostCode")
    val inputSellerCountryCode = connectInput("SellerCountryCode")
    // group_SELLER-CONTACT
    val inputSellerContactPoint = connectInput("SellerContactPoint")
    val inputSellerContactTelephoneNumber = connectInput("SellerContactTelephoneNumber")
    val inputSellerContactEmailAddress = connectInput("SellerContactEmailAddress")
    // group_BUYER
    val inputBuyerName = connectInput("BuyerName")
    val inputBuyerElectronicAddress = connectInput("BuyerElectronicAddress")
    // group_BUYER-POSTAL-ADDRESS
    val inputBuyerCity = connectInput("BuyerCity")
    val inputBuyerPostCode = connectInput("BuyerPostCode")
    val inputBuyerCountryCode = connectInput("BuyerCountryCode")
    // group_PAYMENT-INSTRUCTIONS
    val inputPaymentMeansTypeCode = connectInput("PaymentMeansTypeCode")
    // group_DOCUMENT-TOTALS
    val inputSumOfInvoiceLineNetAmount = connectInput("SumOfInvoiceLineNetAmount")
    val inputInvoiceTotalAmountWithoutVAT = connectInput("InvoiceTotalAmountWithoutVAT")
    val inputInvoiceTotalAmountWithVAT = connectInput("InvoiceTotalAmountWithVAT")
    val inputAmountDueForPayment = connectInput("AmountDueForPayment")
    // group_VAT-BREAKDOWN
    val inputVATCategoryTaxableAmount = connectInput("VATCategoryTaxableAmount")
    val inputVATCategoryTaxAmount = connectInput("VATCategoryTaxAmount")
    val inputVATCategoryCode = connectInput("VATCategoryCode")
    val inputVATCategoryRate = connectInput("VATCategoryRate")
    // group_INVOICE-LINE
    val inputInvoiceLineIdentifier = connectInput("InvoiceLineIdentifier")
    val inputInvoicedQuantity = connectInput("InvoicedQuantity")
    val inputInvoicedQuantityUnitOfMeasureCode = connectInput("InvoicedQuantityUnitOfMeasureCode")
    val inputInvoiceLineNetAmount = connectInput("InvoiceLineNetAmount")
    // group_PRICE-DETAILS
    val inputItemNetPrice = connectInput("ItemNetPrice")
    // group_LINE-VAT-INFORMATION
    val inputInvoicedItemVATCategoryCode = connectInput("InvoicedItemVATCategoryCode")
    // group_ITEM-INFORMATION
    val inputItemName = connectInput("ItemName")

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
          <ram:ID>{inputInvoiceNumber}</ram:ID>
          <ram:TypeCode>{inputInvoiceTypeCode}</ram:TypeCode>
          <ram:IssueDateTime>
            <udt:DateTimeString format="102">{inputInvoiceIssueDate}</udt:DateTimeString>
          </ram:IssueDateTime>
        </rsm:ExchangedDocument>
      </rsm:CrossIndustryInvoice>

    scala.xml.XML.save("./output/outputScalaXMl.xml", xmlData)
    val file = new File("./output/outputPrintWriter.xml")
    val writer = new PrintWriter(file)
    writer.write("<?xml version='1.0' encoding='UTF-8'?>\n" ++ xmlData.toString() ++ "\n")
    writer.close()
    Ok(views.html.index(request))
  }
}
