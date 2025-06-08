package codelists

// I want to create an abstract class for all case class that use enums to validate their codes

import utility._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

abstract class ValidateAbleCode[T, T2](val value: T) {
    // def matchStr(str: String): Option[T] = {
    // T2.values.find(_.toString == "Code_" ++ str) match 
    //     case Some(_) =>
    //         Some(T(str))
    //     case None => 
    //         None
    // }
    // def strInList(str: String): Boolean = {
    //     matchStr(str).isDefined
    // }
    // def validate(code: String): Validated[Seq[ErrorMessage], T] = {
    //     Validated.cond(
    //         strInList(code),
    //         matchStr(code).get,
    //         Seq(ArgumentError)
    //     )
    // }
}
