import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html._
import org.scalajs.dom.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.Dynamic._
import scala.scalajs.js.JSConverters._

import sharedUtility.utility._
import codelists._

@main def main(): Unit =
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
        // Connect the input field from the document
        val invoiceDateInput = document.getElementsByName("InvoiceIssueDate").head.asInstanceOf[Input]
        val now = new Date()
        // Format the date so a html input can use it
        val today = now.getFullYear().toString() + "-" + fillWithZero((now.getMonth()+1).toString()) + "-" + fillWithZero(now.getDate().toString())
        invoiceDateInput.setAttribute("value", today)
        loadRestrictions()
    })
    def parseCode[T](code: T): String = {
        code.toString.replace("Code_", "")
    }
    def getCodelist(source: String): Array[String] = {
        var values = Array.empty[String]
        source match
            case "countrycode" =>
                CountryCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case "currencycode" =>
                CurrencyCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case "invoicetypecode" =>
                InvoiceTypeCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case "measurementcode" =>
                MeasurementCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case "paymentmeanstypecode" =>
                PaymentMeansTypeCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case "vatcategorycode" =>
                VATCategoryCodes.values.foreach(value =>
                    values = values :+ parseCode(value)
                )
            case _ =>
                values :+ "Error: No values found for codelist"
        // println(values.mkString(", "))
        values
    }
    def loadRestrictions(): Unit = {
        val allInputFields = document.querySelectorAll("input")
        for (i <- 0 until allInputFields.length) {
            val input = allInputFields(i).asInstanceOf[HTMLElement]
            if (input.className == "awesomplete"){
                val values = getCodelist(input.dataset("file"))
                input.asInstanceOf[js.Dynamic].data = values.toJSArray // input.data is used by awesomplete as source for values
            }
        }
    }
