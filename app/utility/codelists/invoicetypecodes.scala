package utility.codelists

import utility.validation._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class InvoiceTypeCode(code: String) extends ValidateAble[String](code)
object InvoiceTypeCode {
    private def matchStr (str: String): Option[InvoiceTypeCode] = {
        InvoiceTypeCodes.values.find(_.toString == "Code_" ++ str) match 
            case Some(_) =>
                Some(InvoiceTypeCode(str))
            case None => 
                None
    }
    private def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(code: String): Validated[Seq[ErrorMessage], InvoiceTypeCode] = {
        Validated.cond(
            InvoiceTypeCode.strInList(code),
            InvoiceTypeCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(code))
        )
    }
}

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
