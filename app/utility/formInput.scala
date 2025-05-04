package utility
import play.twirl.api.Html

object FormInputHelper {
    def GenerateInputField(name: String, description: String, req: Boolean=false, readOnly: Boolean=false, dontShow: Boolean=false, inputType: String="text", classes: String="", data: String="", index: String=""): Html = {
        val inputID = "\"" + name.replace(" ", "") + s"$index\""
        var reqString = ""
        var readStr = ""
        var classStr = ""
        var dataStr = ""
        var showStr = ""
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
        Html(s"<div$showStr><label for=$inputID>$name:</label><input type=\"$inputType\" name=$inputID $classStr$dataStr$reqString$readStr><div class=\"tooltip\"><i class=\"fa-solid fa-circle-info\"></i><span>$description</span></div></div>")
    }
}
