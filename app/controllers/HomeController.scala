package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api._
import play.api.mvc._
import scala.sys.process._
import scala.collection.mutable

import java.io.{File, PrintWriter}

import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import cats.syntax.group

import utility.validation._
import utility.xml._
import utility.codelists._

class HomeController @Inject() (val controllerComponents: ControllerComponents) (implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    //Ok(views.html.index(request))
    Ok(views.html.index(using request))
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.index(using request))
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

    val meta = InvoiceMetaData.validate(
      connectInput("InvoiceNumber"),
      connectInput("InvoiceIssueDate").replace("-", ""),
      "380" // TODO: replace with a value the user can set OR replace with fitting default
      )

    val seller = InvoiceSeller.validate(
      connectInput("SellerName"),
      connectInput("SellerAddressLine1"),
      Address.validate(
      connectInput("SellerPostCode"),
      connectInput("SellerCity"),
      connectInput("SellerCountryCode"))
      ,
      connectInput("TODO1"),
      connectInput("TODO3"),
      connectInput("SellerElectronicAddress"),
      connectInput("SellerVATIdentifier")
      )

    val sellerContact = InvoiceSellerContact.validate(
      connectInput("SellerContactPoint"),
      connectInput("SellerContactTelephoneNumber"),
      connectInput("SellerContactEmailAddress"),
      )

    val buyer = InvoiceBuyer.validate(
      connectInput("BuyerReference"),
      connectInput("BuyerName"),
      Address.validate(
      connectInput("BuyerPostCode"),
      connectInput("BuyerCity"),
      connectInput("BuyerCountryCode")
      ),
      connectInput("TODO4"),
      connectInput("BuyerElectronicAddress")
      )

    var allPositions: List[Validated[Seq[ErrorMessage], InvoicePosition]] = Nil
    var groupedPositions: mutable.Map[Validated[Seq[ErrorMessage], VATCategoryIdentifier], List[Validated[Seq[ErrorMessage], SimplePosition]]] = mutable.Map.empty
    for index <- allPositionIDs do
      val positionType = connectInput("positionTypecontainer" + index)
      val vatId = VATCategoryIdentifier.validate(
        connectInput("InvoicedItemVATCategoryCode" + index),
        connectInput("InvoicedItemVATRate" + index)
        )
      val position = InvoicePosition.validate(
        connectInput("InvoiceLineIdentifier" + index),
        connectInput("ItemName" + index),
        vatId,
        positionType match {
        case "time" =>
          Stundenposition.validate(
            connectInput("InvoicedQuantity" + index),
            connectInput("ItemNetPrice" + index)
          )
        case "item" =>
          Leistungsposition.validate(
            connectInput("InvoicedQuantity" + index),
            connectInput("ItemNetPrice" + index),
            connectInput("InvoicedQuantityUnitOfMeasureCode" + index)
          )
        }
      )
      allPositions = allPositions :+ position
      // Group positions with only the info that is relevant for the VAT groups
      val newPos = SimplePosition.validate(vatId, connectInput("InvoicedQuantity" + index), connectInput("ItemNetPrice" + index))
      val currentPos = groupedPositions.getOrElse(vatId, List.empty)
      groupedPositions.update(vatId, currentPos :+ newPos)

    var vatGroups: List[Validated[Seq[ErrorMessage], InvoiceVATGroup]] = Nil
    for (group <- groupedPositions.keys) {
      val vatGroup = InvoiceVATGroup.validate(
        group,
        groupedPositions(group)
      )
      vatGroups = vatGroups :+ vatGroup
    }

    val paymentInformation = InvoicePaymentInformation.validate(
      connectInput("InvoiceCurrencyCode"),
      connectInput("PaymentMeansTypeCode"),
      vatGroups,
      connectInput("PaymentTerms")
    )

    val involvedparties = InvoiceInvolvedParties.validate(seller, sellerContact, buyer)
    val invoice = Invoice.validate(meta, involvedparties, allPositions, paymentInformation)

    invoice match {
      case Valid(a) =>
        val xmlData = xmlUtil.CreateInvoiceXML(a)
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
      case Invalid(e) =>
        Ok(views.html.invalid_input(e))
      }
  }
}
