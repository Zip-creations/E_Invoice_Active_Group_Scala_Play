package utility.validation

import scala.util.{Try, Success, Failure}
import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import sharedUtility.error._

// Each function shall return if the test result is negative (ergo: return true if the input is valid, and false if the input is invalid)


def basicTests(value: String, maxlength: Int, errorHead: String, addDisallowed: Seq[String] = Seq.empty, removeDisallowed: Seq[String] = Seq.empty, newDisallowed: Seq[String] = Seq.empty): Seq[ErrorMessage] = {
    var disallowedLiterals: Seq[String] = Seq.empty
    if (newDisallowed.isEmpty){
        disallowedLiterals = Seq("#", ";", ":", "=", "*") ++ addDisallowed  // default disallowed literals
    } else {
        disallowedLiterals = newDisallowed
    }
    var errorlist: Seq[ErrorMessage] = Seq.empty
    // allow all literals from removeDisallowed
    removeDisallowed.foreach(literal =>
        disallowedLiterals.filterNot(elem => elem == literal))
    if (value.length > maxlength){
        errorlist = errorlist :+ ArgumentError(f"$errorHead: \"$value\" enthält mehr Zeichen als das erlaubte Limit für diesen Typen: $maxlength")
    }
    disallowedLiterals.foreach(literal =>
        if (value.contains(literal)){
            errorlist = errorlist :+ ArgumentError(f"$errorHead: \"$value\" enthält ein ungültiges Zeichen: $literal")
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
