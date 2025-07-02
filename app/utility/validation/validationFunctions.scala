package utility.validation

import scala.util.{Try, Success, Failure}
import scala.util.matching.Regex

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

import sharedUtility.error._
import sharedUtility.validation._
import scala.collection.mutable


def basicTests(input: InputType, maxlength: Int, errorHead: String, allowedChars: String = " "): Seq[ErrorMessage] = {
    val value = input.value
    val regex = ("(?u)[\\p{L}\\d"+ allowedChars + "]*$").r
    var errorlist: Seq[ErrorMessage] = Seq.empty
    if (value.length > maxlength){
        errorlist = errorlist :+ ArgumentError(InputType(f"$errorHead: \"$value\" ist zu lang. Zeichenlimit für diesen Typen: $maxlength", input.source))
    }
    var invalidChars: mutable.Map[Char, Int] = mutable.Map.empty
    value.foreach(char =>
        if (!regex.matches(char.toString())){
            var count = invalidChars.getOrElse(char, 0)
            invalidChars.update(char, count+1)
        })
    for (char <- invalidChars.keys) {
        val count = invalidChars(char)
        errorlist = errorlist :+ ArgumentError(InputType(f"$errorHead: \"$value\" enthält ${count}x das ungültige Zeichen: $char", input.source))
    }
    errorlist
}

def isValidDouble(str: String): Option[Double] = {
    Try(str.toDouble) match{
        case Success(num) =>
            Some(num)
        case Failure(e) =>
            None
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
