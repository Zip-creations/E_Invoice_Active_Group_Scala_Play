package utility
import play.twirl.api.Html

object FormInputHelper {
    def GenerateInputField(name: String, description: String, inputType: String = "text", req: Boolean = false): Html = {
        val inputID = name.replace(" ", "")
        var reqString = ""
        if (req) {
            reqString = "required"
        }

        Html(s"<div><label for=\"$inputID\">$name</label><input type=\"$inputType\" name=\"$inputID\" $reqString></input><div class=\"tooltip\"><i class=\"fa-solid fa-circle-info\"></i><span>$description</span></div></div>")
    }
}
