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

def IsValidDate(str: String): Boolean = {
    Try(str.toInt) match{
        case Success(v) =>
            str.length() == 8
        case Failure(e) =>
            false
    }
}
