package codelists

import sharedUtility.validation._
import sharedUtility.error._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class InvoiceTypeCode private(val code: String) extends ValidateAble[String](code) // extends ValidateableCode2[InvoiceTypeCode](code, InvoiceTypeCodes.values)
object InvoiceTypeCode {
    private def matchStr(str: String): Option[InvoiceTypeCode] = {
        InvoiceTypeCodes.values.find(_.toString == "Code_" ++ str) match 
            case Some(_) =>
                Some(InvoiceTypeCode(str))
            case None => 
                None
    }
    private def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(input: InputType): Validated[Seq[ErrorMessage], InvoiceTypeCode] = {
        val code = input.value
        Validated.cond(
            InvoiceTypeCode.strInList(code),
            InvoiceTypeCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(input))
        )
    }
}

// abstract class ValidateableCode2[T](val code: String, val enumeration: Array[?]) extends ValidateAble[String](code) {
//     private def matchStr[T](str: String, enumeration: scala.Enumeration)(constructor: String => T): Option[T] = {
//     enumeration.values.find(_.toString == "Code_" ++ str) match 
//         case Some(_) =>
//             Some(constructor(str))
//         case None => 
//             None
//     }
//     pricte def strInList[T](str: String, enumeration: Enumeration)(constructor: String => T): Boolean = {
//         matchStr(str, enumeration)(constructor).isDefined
//     }
//     def validate[T](code: String)(constructor: String => T): Validated[Seq[ErrorMessage], T] = {
//         Validated.cond(
//             ValidateableCode2.strInList(code, enumeration)(constructor),
//             ValidateableCode2.matchStr(code, enumeration)(constructor).get,
//             Seq(ValueNotInCodelistError(code))
//         )
//     }
// }

enum InvoiceTypeCodes {
    case
        Code_326,
        Code_380,
        Code_384,
        Code_389,
        Code_381,
        Code_875,
        Code_876,
        Code_877,
}
