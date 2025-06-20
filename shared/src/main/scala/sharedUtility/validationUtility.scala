package sharedUtility.validation

def makeError(message: String, value: String): String = {
    s"${message} Fehlerquelle: \'${value}\'"
}
def makeError(message: String, value: ValidateAble[?]): String = {
    s"${message} Fehlerquelle: \'${value.getStr.slice(4,5)}\'"
}

abstract class ValidateAble[T](val value: T) {
    def get: T = {
        value
    }
    def getStr: String = {
        value.toString
    }
}
