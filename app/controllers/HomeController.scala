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
    Ok(views.html.index(request))
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.index(request))
  }

  def generateLeistungsabrechnungPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoice_item(positionID))
  }

  def generateStundenabrechnungPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoice_time(positionID))
  }

  def generateEInvoice(counter: Int = 0) = Action { implicit request: Request[AnyContent] =>
    var xmlUtil = XMLUtility()
    val formData = request.body.asFormUrlEncoded
    val allPositionIDs: Seq[String] = formData
      .flatMap(_.get("positionIDcontainer"))
      .getOrElse(Seq.empty)

    def connectInput = (inputIdentifier: String) =>
      formData.flatMap(_.get(inputIdentifier).flatMap(_.headOption)).getOrElse("")

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
      connectInput("SellerCountryCode"),
      connectInput("TODO1"),
      connectInput("TODO3"),
      connectInput("SellerElectronicAddress"),
      )

    val sellerContact = InvoiceSellerContact(
      connectInput("SellerContactPoint"),
      connectInput("SellerContactTelephoneNumber"),
      connectInput("SellerContactEmailAddress"),
      )

    val buyer = InvoiceBuyer(
      connectInput("BuyerReference"),
      connectInput("BuyerName"),
      connectInput("BuyerPostCode"),
      connectInput("BuyerCity"),
      connectInput("BuyerCountryCode"),
      connectInput("TODO4"),
      connectInput("BuyerElectronicAddress")
      )

    // TODO: replace hardcoded values
    val paymentInformation = InvoicePaymentInformation(
      connectInput("InvoiceCurrencyCode"),
      connectInput("PaymentMeansTypeCode")
    )

    // only for testting purposes - postions need to be created dynamically and in an abritrary number later on!
    val testposition = InvoicePosition(
      "123",
      19,
      InvoicePositionData.Stundenposition(12, 42)
    )

    val invoice = Invoice(meta, seller, sellerContact, buyer, Array(testposition).toList, paymentInformation)

    val xmlData = xmlUtil.CreateInvoiceXML(invoice)

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
