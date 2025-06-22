package sharedUtility.ValidateAble

abstract class ValidateAble[T](val value: T) {
    def get: T = {
        value
    }
    def getStr: String = {
        value.toString
    }
}
