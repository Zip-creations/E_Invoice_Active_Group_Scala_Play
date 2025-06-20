import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html

import scala.scalajs.js.Date
import sharedUtility.utility._

@main def main(): Unit =
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
        // Connect the input field from the document
        val invoiceDateInput = document.getElementsByName("InvoiceIssueDate").head.asInstanceOf[html.Input]
        val now = new Date()
        // Format the date so a html input can use it
        val today = now.getFullYear().toString() + "-" + fillWithZero((now.getMonth()+1).toString()) + "-" + fillWithZero(now.getDate().toString())
        invoiceDateInput.setAttribute("value", today)
        val positionID = 1
        println(today)
    })
