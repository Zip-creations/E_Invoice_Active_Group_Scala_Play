package codelists

import sharedUtility.validation._
import sharedUtility.error._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class VATCategoryCode private(code: String) extends ValidateAble[String](code)
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
    def validate(input: InputType): Validated[Seq[ErrorMessage], VATCategoryCode] = {
        val code = input.value
        Validated.cond(
            VATCategoryCode.strInList(code),
            VATCategoryCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(makeError("Für eine Steuerkategorie wurde ein inkorrekter Code angegeben.", input)))
        )
    }
}

// Added Code_ to enable the Enum to work with codes that start with numbers, and preserve uniformity
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
