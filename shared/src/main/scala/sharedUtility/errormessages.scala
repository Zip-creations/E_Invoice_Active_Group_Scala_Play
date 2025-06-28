package sharedUtility.error
import sharedUtility.validation._

def makeError(message: String, input: InputType): InputType = {
    InputType(s"${message} Fehlerquelle: \'${input.value}\'", input.source)
}
def makeError(message: String, value: ValidateAble[?]): String = {
    s"${message} Fehlerquelle: \'${value.getStr}\'"
}

abstract class ErrorMessage(input: InputType) {
    def errorMessage: String
    val value = input
}

case class ArgumentError(input: InputType) extends ErrorMessage(input) {
    def errorMessage: String = s"ArgumentError: ${input.value.map(_ + "\n").mkString}"
}

case class ValueNotInCodelistError(input: InputType) extends ErrorMessage(input) {
    def errorMessage: String = s"ValueNotInCodelistError: ${input.value.map(_ + "\n").mkString}"
}
