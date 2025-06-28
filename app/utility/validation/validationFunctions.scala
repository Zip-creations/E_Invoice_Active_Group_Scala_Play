package utility.validation

import scala.util.{Try, Success, Failure}
import scala.util.matching.Regex

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

import sharedUtility.error._
import sharedUtility.validation._

// Each function shall return if the test result is negative (ergo: return true if the input is valid, and false if the input is invalid)


def basicTests(input: InputType, maxlength: Int, errorHead: String, allowedLiterals: String = " "): Seq[ErrorMessage] = {
    val value = input.value
    val regex = ("(?u)[\\p{L}\\d"+ allowedLiterals + "]*$").r
    var errorlist: Seq[ErrorMessage] = Seq.empty
    if (value.length > maxlength){
        errorlist = errorlist :+ ArgumentError(InputType(f"$errorHead: \"$value\" ist zu lang. Zeichenlimit für diesen Typen: $maxlength", input.source))
    }
    value.foreach(literal =>
        if (!regex.matches(literal.toString())){
            errorlist = errorlist :+ ArgumentError(InputType(f"$errorHead: \"$value\" enthält ein ungültiges Zeichen: $literal", input.source))
        })
    errorlist
}

def isValidDouble(str: String): Boolean = {
    Try(str.toDouble) match{
        case Success(v) =>
            true
        case Failure(e) =>
            false
    }
}

def notNegative(num: Double): Boolean = {
    num >= 0
}

// Only valid date format: YYYYMMDD
def isValidDateFormat(str: String): Boolean = {
    Try(str.toInt) match{
        case Success(v) =>
            str.length() == 8 && v >= 0
        case Failure(e) =>
            false
    }
}
