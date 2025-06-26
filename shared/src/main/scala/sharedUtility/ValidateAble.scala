package sharedUtility.validation

abstract class ValidateAble[T](val value: T) {
    def get: T = {
        value
    }
    def getStr: String = {
        value.toString
    }
}

case class InputType(
    value: String,
    source: String
)
object InputType {
    def empty: InputType = {
        InputType("", "")
    }
}