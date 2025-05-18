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
      "DE",
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
      "ref 123",
      "buyer name",
      "54321",
      "buyer city",
      "DE",
      "DE12 3456 7890",
      "ex3@mail.com"
      // connectInput(""),
      // connectInput(""),
      // connectInput(""),
      // connectInput(""),
      // connectInput(""),
      // connectInput("")
      )

    // TODO: replace hardcoded values
    val paymentInformation = InvoicePaymentInformation(
      "EUR",
      "ZZZ"
    )

    // only for testting purposes - postions need to be created dynamically and in an abritrary number later on!
    val testposition = InvoicePosition.Stundenposition(
      "123",
      19,
      12,
      42.42
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
