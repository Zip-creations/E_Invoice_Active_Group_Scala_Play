package utility.validation

import scala.util.{Try, Success, Failure}

// Each function shall return if the test result is negative (ergo: return true if the input is valid, and false if the input is invalid)
// so the result of each validation function can be used in a disjuction

// Tests for two or more consecutive whitespaces
def NoDoubleWhitespace(str: String): Boolean = {
    !str.contains("  ")
}

def IsValidDouble(str: String): Boolean = {
    Try(str.toDouble) match{
        case Success(v) =>
            true
        case Failure(e) =>
            false
    }
}

def NotNegative(num: Double): Boolean = {
    num >= 0
}

def IsValidDateFormat(str: String): Boolean = {
    Try(str.toInt) match{
        case Success(v) =>
            str.length() == 8 && str.toInt > 0
        case Failure(e) =>
            false
    }
}

def ValidMonth(str: String): Boolean = {
    val month = str.slice(4, 6).toInt
    month > 0 && month <= 12
}

def ValidDay(str: String): Boolean = {
    val day = str.slice(6, 8).toInt
    day > 0 && day <= 31
}
