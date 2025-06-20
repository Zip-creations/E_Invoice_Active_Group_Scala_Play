package codelists

import sharedUtility.validation._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class VATCategoryCode(code: String) extends ValidateAble[String](code)
object VATCategoryCode {
    private def matchStr (str: String): Option[VATCategoryCode] = {
        VATCategoryCodes.values.find(_.toString == "Code_" ++ str) match 
            case Some(_) =>
                Some(VATCategoryCode(str))
            case None => 
                None
    }
    private def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(code: String): Validated[Seq[ErrorMessage], VATCategoryCode] = {
        Validated.cond(
            VATCategoryCode.strInList(code),
            VATCategoryCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(code))
        )
    }
}

enum VATCategoryCodes {
    case
        Code_S,
        Code_Z,
        Code_E,
        Code_AE,
        Code_K,
        Code_G,
        Code_O,
        Code_L,
        Code_M,
}
