package utility.validation

import scala.util.{Try, Success, Failure}
import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import sharedUtility.error._
import scala.scalajs.js.Dynamic.literal

// Each function shall return if the test result is negative (ergo: return true if the input is valid, and false if the input is invalid)


def basicTests[T](value: String, maxlength: Int, constructor: String => T, addDisallowed: Seq[String] = Seq.empty, removeDisallowed: Seq[String] = Seq.empty, newDisallowed: Seq[String] = Seq.empty): Validated[Seq[ErrorMessage], T] = {
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
        errorlist = errorlist :+ ArgumentError(f"$value enthält mehr Zeichen als das erlaubte Limit für diesen Input: $maxlength")
    }
    disallowedLiterals.foreach(literal =>
        if (value.contains(literal)){
            errorlist = errorlist :+ ArgumentError(f"$value enthält ein ungültiges Zeichen: $literal")
        })
    if (errorlist.isEmpty) {
        Valid(constructor(value))
    } else {
        Invalid(errorlist)
    }
}

// Tests for two or more consecutive whitespaces
def noDoubleWhitespace(str: String): Boolean = {
    !str.contains("  ")
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
