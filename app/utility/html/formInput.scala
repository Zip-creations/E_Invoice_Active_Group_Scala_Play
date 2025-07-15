package utility.html

import play.twirl.api.Html
import utility.inputNames._

object FormInputHelper {
    def GenerateInputField(name: InputName, displayname: String, description: String, req: Boolean=false, readOnly: Boolean=false, dontShow: Boolean=false, inputType: String="text", classes: String="", data: String="", defaultValue: String=""): Html = {
        val inputID = "\"" + name.toString.replace(" ", "") + "\""
        var reqString = ""
        var readStr = ""
        var classStr = ""
        var dataStr = ""
        var showStr = ""
        var valueStr = ""
        if (req) {
            reqString = "required "
        }
        if (readOnly) {
            readStr = "readonly"
        }
        if (dontShow) {
            showStr = " style=\"display: none\""
        }
        if (classes != "") {
            classStr = "class=\"" + classes + "\" "
        }
        if (data != "") {
            dataStr = "data-file=\"" + data + "\" "
        }
        if (defaultValue != "") {
            valueStr = "value=\"" + defaultValue + "\" "
        }
        Html(s"<div class=\"inputfield\"><div$showStr class=\"inputDataField\"><label for=$inputID>$displayname:</label><input type=\"$inputType\" name=$inputID $valueStr $classStr$dataStr$reqString$readStr><div class=\"tooltip\"><i class=\"fa-solid fa-circle-info\"></i><span>$description</span></div></div><div class=errorDisplay></div></div>")
    }
}
