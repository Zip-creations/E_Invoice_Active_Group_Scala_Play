package utility.validation

import scala.util.{Try, Success, Failure}


// Each function shall return if the test result is negative (ergo: return true if the input is valid, and false if the input is invalid)

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

// Only valid date format: YYYYMMDD
def IsValidDateFormat(str: String): Boolean = {
    Try(str.toInt) match{
        case Success(v) =>
            str.length() == 8 && v >= 0
        case Failure(e) =>
            false
    }
}
