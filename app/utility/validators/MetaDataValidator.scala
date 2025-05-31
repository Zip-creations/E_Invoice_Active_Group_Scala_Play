package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

sealed trait MetaDataValidator {
    def validateNumber(number: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            number,
            Seq(ArgumentError)
        )
    }
    def validateDate(date: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            date,
            Seq(ArgumentError)
        )
    }
    def validateTyp(typ: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            typ,
            Seq(ArgumentError)
        )
    }
    def validateMetaData(number: String, date: String, typ: String): Validated[Seq[ErrorMessage], InvoiceMetaData] = {
        (
            validateNumber(number),
            validateDate(date),
            validateTyp(typ)
        ).mapN(InvoiceMetaData.apply)
    }
}
object MetaDataValidator extends MetaDataValidator
