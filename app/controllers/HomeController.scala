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
    val inputInvoiceNumber = formData.flatMap(_.get("InvoiceNumber").flatMap(_.headOption)).getOrElse("")
    val inputInvoiceTypeCode = formData.flatMap(_.get("InvoiceTypeCode").flatMap(_.headOption)).getOrElse("")
    val inputInvoiceIssueDate = formData.flatMap(_.get("InvoiceIssueDate").flatMap(_.headOption)).getOrElse("")
    
    val xmlData =
      <CrossIndustryInvoice xmlns:rsm="urn:un:unece:uncefact:data:standard:CrossIndustryInvoice:100" xmlns:qdt="urn:un:unece:uncefact:data:standard:QualifiedDataType:100" xmlns:ram="urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:100" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:100">
        <ExchangedDocumentContext>
          <ram:BusinessProcessSpecifiedDocumentContextParameter>
            <ram:ID>urn:fdc:peppol.eu:2017:poacc:billing:01:1.0</ram:ID>
          </ram:BusinessProcessSpecifiedDocumentContextParameter>
          <ram:GuidelineSpecifiedDocumentContextParameter>
            <ram:ID>urn:cen.eu:en16931:2017#compliant#urn:xeinkauf.de:kosit:xrechnung_3.0</ram:ID>
          </ram:GuidelineSpecifiedDocumentContextParameter>
        </ExchangedDocumentContext>
        <ExchangedDocument>
          <ram:ID>{inputInvoiceNumber}</ram:ID>
          <ram:TypeCode>{inputInvoiceTypeCode}</ram:TypeCode>
          <ram:IssueDateTime>
            <udt:DateTimeString format="102">{inputInvoiceIssueDate}</udt:DateTimeString>
          </ram:IssueDateTime>
        </ExchangedDocument>
      </CrossIndustryInvoice>

    scala.xml.XML.save("./output/outputScalaXMl.xml", xmlData)
    val file = new File("./output/outputPrintWriter.xml")
    val writer = new PrintWriter(file)
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" ++ xmlData.toString() ++ "\n")
    writer.close()
    Ok(views.html.index(request))
  }
}
