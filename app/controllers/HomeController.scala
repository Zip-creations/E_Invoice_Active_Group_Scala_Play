package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api._
import play.api.mvc._
import scala.sys.process._
import scala.collection.mutable

import java.io.{File, PrintWriter}
import scala.xml.XML

import utility._
import cats.syntax.group

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
      new Address(
      connectInput("SellerPostCode"),
      connectInput("SellerCity"),
      connectInput("SellerCountryCode")
      ),
      connectInput("TODO1"),
      connectInput("TODO3"),
      connectInput("SellerElectronicAddress"),
      connectInput("SellerVATIdentifier")
      )

    val sellerContact = InvoiceSellerContact(
      connectInput("SellerContactPoint"),
      connectInput("SellerContactTelephoneNumber"),
      connectInput("SellerContactEmailAddress"),
      )

    val buyer = InvoiceBuyer(
      connectInput("BuyerReference"),
      connectInput("BuyerName"),
      new Address(
      connectInput("BuyerPostCode"),
      connectInput("BuyerCity"),
      connectInput("BuyerCountryCode")
      ),
      connectInput("TODO4"),
      connectInput("BuyerElectronicAddress")
      )

    var allPositions: List[InvoicePosition] = Nil
    var simplifiedPositions: List[SimplePosition] = Nil
    var groupedPositions: mutable.Map[VATCategoryIdentifier, List[SimplePosition]] = mutable.Map.empty
    for index <- allPositionIDs do
      val positionType = connectInput("positionTypecontainer" + index)
      var innerPosition: InvoicePositionData = null
      positionType match {
        case "time" =>
          innerPosition = InvoicePositionData.Stundenposition(
            connectInput("InvoicedQuantity" + index).toDouble,
            connectInput("ItemNetPrice" + index).toDouble
          )
        case "item" =>
          innerPosition = InvoicePositionData.Leistungsposition(
            connectInput("InvoicedQuantity" + index).toDouble,
            connectInput("ItemNetPrice" + index).toDouble,
            connectInput("InvoicedQuantityUnitOfMeasureCode" + index)
          )
      }
      val position = InvoicePosition(
        connectInput("InvoiceLineIdentifier" + index),
        connectInput("ItemName" + index),
        connectInput("InvoicedItemVATCategoryCode" + index),
        connectInput("InvoicedItemVATRate" + index).toDouble,
        innerPosition
      )
      allPositions = allPositions :+ position
      simplifiedPositions = simplifiedPositions :+ SimplePosition(VATCategoryIdentifier(position.vatCode, position.vatRate), connectInput("InvoicedQuantity" + index).toDouble * connectInput("ItemNetPrice" + index).toDouble)
    for (pos <- simplifiedPositions) {
      val identifier = pos.identifier
      val currentPos = groupedPositions.getOrElse(identifier, List.empty)
      val newPos = SimplePosition(identifier, pos.netAmount)
      groupedPositions.update(identifier, currentPos :+ newPos)
    }
    var vatGroups: List[InvoiceVATGroup] = Nil
    for (group <- groupedPositions.keys) {
      val vatGroup = InvoiceVATGroup(
        group,
        groupedPositions(group)
      )
      vatGroups = vatGroups :+ vatGroup
    }

    val paymentInformation = InvoicePaymentInformation(
      connectInput("InvoiceCurrencyCode"),
      connectInput("PaymentMeansTypeCode"),
      vatGroups,
      connectInput("PaymentTerms")
    )
    // Testing validation prototypes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // var ex6 = InputValidator.ValidateFormData(1, "A")
    // print(ex6)
    // print("\n")
    // var ex7 = InputValidator.ValidateFormData(0, "A")
    // print(ex7)
    // print("\n")
    // var ex8 = InputValidator.ValidateFormData(1, "D")
    // print(ex8)
    // print("\n")
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    val involvedparties = InvoiceInvolvedParties(seller, sellerContact, buyer)
    val invoice = Invoice(meta, involvedparties, allPositions, paymentInformation)

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
