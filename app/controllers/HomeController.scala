package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.sys.process._
import scala.collection.mutable
import java.io.{File, PrintWriter}

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

import sharedUtility.error._
import sharedUtility.validation._

import utility.xml._
import utility.validation._

class HomeController @Inject() (val controllerComponents: ControllerComponents) (implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
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

  // JS can't access app/views/validation_reports/ directly, that's why this function exists
  def getReport(path: String) = Action { (request: Request[AnyContent]) =>
    Ok.sendFile(new java.io.File(path))
  }

  def generateEInvoice = Action(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] =>
    var xmlUtil = XMLUtility()
    val formData = request.body
    val allPositionIDs: Seq[String] = formData.dataParts
      .getOrElse("positionIDcontainer", Seq.empty)

    def getInput(inputIdentifier: String) = {
      formData.dataParts.get(inputIdentifier).flatMap(_.headOption).getOrElse("")
    }
    def createInputType(source: String): InputType = {
      InputType(getInput(source), source)
    }

    val meta = InvoiceMetaData.validate(
      createInputType("InvoiceNumber"),
      InputType(getInput("InvoiceIssueDate").replace("-", ""), "InvoiceIssueDate"),  // Xrechnung requieres the format YYYYMMDD without '-'
      InputType("380", "") // hardcoded default value, from InvoiceTypeCodes
      )

    val seller = InvoiceSeller.validate(
      createInputType("SellerName"),
      createInputType("SellerAddressLine1"),
      Address.validate(
      createInputType("SellerPostCode"),
      createInputType("SellerCity"),
      createInputType("SellerCountryCode")),
      createInputType("TODO1"),
      createInputType("TODO3"),
      createInputType("SellerElectronicAddress"),
      createInputType("SellerVATIdentifier")
      )

    val sellerContact = InvoiceSellerContact.validate(
      createInputType("SellerContactPoint"),
      createInputType("SellerContactTelephoneNumber"),
      createInputType("SellerContactEmailAddress"),
      )

    val buyer = InvoiceBuyer.validate(
      createInputType("BuyerReference"),
      createInputType("BuyerName"),
      Address.validate(
      createInputType("BuyerPostCode"),
      createInputType("BuyerCity"),
      createInputType("BuyerCountryCode")),
      createInputType("TODO4"),
      createInputType("BuyerElectronicAddress")
      )

    var allPositions: List[Validated[Seq[ErrorMessage], InvoicePosition]] = Nil
    var groupedPositions: mutable.Map[Validated[Seq[ErrorMessage], VATCategoryIdentifier], List[Validated[Seq[ErrorMessage], SimplePosition]]] = mutable.Map.empty
    for index <- allPositionIDs do
      val positionType = getInput("positionTypecontainer" + index)
      val vatId = VATCategoryIdentifier.validate(
        createInputType("InvoicedItemVATCategoryCode" + index),
        createInputType("InvoicedItemVATRate" + index)
        )
      val position = InvoicePosition.validate(
        createInputType("InvoiceLineIdentifier" + index),
        createInputType("ItemName" + index),
        vatId,
        positionType match {
        case "time" =>
          Stundenposition.validate(
            createInputType("InvoicedQuantity" + index),
            createInputType("ItemNetPrice" + index)
          )
        case "item" =>
          Leistungsposition.validate(
            createInputType("InvoicedQuantity" + index),
            createInputType("ItemNetPrice" + index),
            createInputType("InvoicedQuantityUnitOfMeasureCode" + index)
          )
        }
      )
      allPositions = allPositions :+ position
      // Group positions with only the info that is relevant for the VAT groups
      val newPos = SimplePosition.validate(vatId, createInputType("InvoicedQuantity" + index), createInputType("ItemNetPrice" + index))
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
      createInputType("InvoiceCurrencyCode"),
      createInputType("PaymentMeansTypeCode"),
      vatGroups,
      createInputType("PaymentTerms")
    )

    val involvedparties = InvoiceInvolvedParties.validate(seller, sellerContact, buyer)
    val invoice = Invoice.validate(meta, involvedparties, allPositions, paymentInformation)

    invoice match {
      case Valid(a) =>
        val xmlData = xmlUtil.CreateInvoiceXML(a)
        // Paths
        val inputInvoiceNumber = getInput("InvoiceNumber")
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
        Ok(Json.obj("status"-> "ok", "data" -> reportPath))
      case Invalid(e) =>
        val errors: mutable.Map[String, Seq[String]] = mutable.Map.empty
        e.foreach(error =>
          val currentPos = errors.getOrElse(error.value.source, List.empty)
          errors.update(error.value.source, currentPos :+ "<p>"+error.value.value+"</p>")
          )
        Ok(Json.obj("status" -> "error", "data" -> Json.toJson(errors)))
      }
  }
}
