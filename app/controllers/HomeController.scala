package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._
import play.twirl.api.Html

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.sys.process._
import scala.collection.mutable
import java.io.{File, PrintWriter}

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

import sharedUtility.error._
import sharedUtility.utility._
import sharedUtility.validation._

import utility.xml._
import utility.inputNames._
import utility.validation._

class HomeController @Inject() (val controllerComponents: ControllerComponents) (implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(using request))
  }

  def addPosition(positionID: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.index(using request))
  }

  def generateLeistungsabrechnungPosition(positionID: Int) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoicePosition_item(positionID))
  }

  def generateStundenabrechnungPosition(positionID: Int) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.invoicePosition_time(positionID))
  }

  def generateVatIDPositionContainer(vatID: String, posIDs: String) = Action { (request: Request[AnyContent]) =>
    Ok(views.html.vatIDPositionContainer(vatID, posIDs))
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
    val allVATGroups: Seq[String] = formData.dataParts
      .getOrElse("vatIDContainer", Seq.empty)
    def getInput(inputIdentifier: String) = {
      formData.dataParts.get(inputIdentifier).flatMap(_.headOption).getOrElse("")
    }
    def createInputType(source: InputName): InputType = {
      val name = source.toString
      InputType(getInput(name), name)
    }

    val meta = InvoiceMetaData.validate(
      createInputType(InputName.InvoiceNumber),
      InputType(getInput("InvoiceIssueDate").replace("-", ""), "InvoiceIssueDate"),  // Xrechnung requires the format YYYYMMDD without '-'
      InputType("380", "") // hardcoded default value, from InvoiceTypeCodes
      )

    val seller = InvoiceSeller.validate(
      createInputType(InputName.SellerName),
      createInputType(InputName.SellerAddressLine1),
      Address.validate(
      createInputType(InputName.SellerPostCode),
      createInputType(InputName.SellerCity),
      createInputType(InputName.SellerCountryCode)),
      createInputType(InputName.Placeholder1),
      createInputType(InputName.Placeholder2),
      createInputType(InputName.SellerElectronicAddress),
      createInputType(InputName.SellerVATIdentifier)
      )

    val sellerContact = InvoiceSellerContact.validate(
      createInputType(InputName.SellerContactPoint),
      createInputType(InputName.SellerContactTelephoneNumber),
      createInputType(InputName.SellerContactEmailAddress),
      )

    val buyer = InvoiceBuyer.validate(
      createInputType(InputName.BuyerReference),
      createInputType(InputName.BuyerName),
      Address.validate(
      createInputType(InputName.BuyerPostCode),
      createInputType(InputName.BuyerCity),
      createInputType(InputName.BuyerCountryCode)),
      createInputType(InputName.Placeholder3),
      createInputType(InputName.BuyerElectronicAddress)
      )

    var validatedVATGroups: List[Validated[Seq[ErrorMessage], VATGroup]] = Nil
    // allPositions is for invoice.positions
    var allPositions: List[Validated[Seq[ErrorMessage], InvoicePosition]] = Nil
    allVATGroups.foreach(group =>
      val validatedVATID = VATCategoryIdentifier.validate(
        // Refers to the inputs of the vatIDPositionContainer
        createInputType(InputName.VATGroupCategory(group)),
        createInputType(InputName.VARGroupRate(group))
      )
      val posIDs = getInput("vatGroupPositionIDsContainer" + group)
      // vatGroupPositions is for the tax summary
      var vatGroupPositions: List[Validated[Seq[ErrorMessage], InvoicePosition]] = Nil
      // create a validated InvoicePostion for every positionID
      posIDs.split(",").foreach(posID =>
        val number = posID.toInt
        val positionType = getInput("positionTypecontainer" + posID)
        val vatId = VATCategoryIdentifier.validate(
          createInputType(InputName.InvoicedItemVATCategoryCode(number)),
          createInputType(InputName.InvoicedItemVATRate(number))
          )
        val quantity = createInputType(InputName.InvoicedQuantity(number))
        val netPrice = createInputType(InputName.ItemNetPrice(number))
        val position = InvoicePosition.validate(
          createInputType(InputName.InvoiceLineIdentifier(number)),
          createInputType(InputName.ItemName(number)),
          vatId,
          positionType match {
          case "time" =>
            Stundenposition.validate(
              quantity,
              netPrice
            )
          case "item" =>
            Leistungsposition.validate(
              quantity,
              netPrice,
              createInputType(InputName.InvoicedQuantityUnitOfMeasureCode(number))
            )
          }
        )
        vatGroupPositions = vatGroupPositions :+ position
        allPositions = allPositions :+ position
        )
      val validatedGroup = VATGroup.validate(
        validatedVATID,
        vatGroupPositions,
        createInputType(InputName.VATGroupExemptionReasonText(group))
      )
      validatedVATGroups = validatedVATGroups :+ validatedGroup
    )
    val paymentInformation = InvoicePaymentInformation.validate(
      createInputType(InputName.InvoiceCurrencyCode),
      createInputType(InputName.PaymentMeansTypeCode),
      validatedVATGroups,
      createInputType(InputName.PaymentTerms)
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
          errors.update(error.value.source, currentPos :+ "<div><p>"+error.value.value+"</p></div>")
          )
        Ok(Json.obj("status" -> "error", "data" -> Json.toJson(errors)))
      }
  }
}
