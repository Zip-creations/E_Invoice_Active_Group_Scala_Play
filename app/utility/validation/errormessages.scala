package utility.validation

abstract class ErrorMessage(val str: Seq[String]) {
    def errorMessage: String
}

case class ArgumentError(override val str: String*) extends ErrorMessage(str) {
    def errorMessage: String = s"ArgumentError: ${str.map(_ + "\n").mkString}"
}

case class ValueNotInCodelist(override val str: String*) extends ErrorMessage(str) {
    def errorMessage: String = s"Given value ${str} was not found in codelist"
}
