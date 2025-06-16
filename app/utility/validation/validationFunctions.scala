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

def ValidYear(str: String): Option[String] = {
    val year = str.slice(0, 4).toInt
    if (year >= 0) {
        Some(year.toString)
    } else {
        None
    }
}

def ValidMonth(str: String): Option[String] = {
    val month = str.slice(4, 6).toInt
    if (month > 0 && month <= 12){
        Some(month.toString)
    } else {
        None
    }
}

def ValidDay(str: String): Option[String] = {
    val day = str.slice(6, 8).toInt
    if (day > 0 && day <= 31) {
        Some(day.toString)
    } else {
        None
    }
}
