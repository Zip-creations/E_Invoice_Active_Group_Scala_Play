package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

sealed trait StundenpositionValidator {
    def valideteHours(hours: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            hours,
            Seq(ArgumentError)
        )
    }
    def valideteHourlyrate(hourlyrate: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            hourlyrate,
            Seq(ArgumentError)
        )
    }
    def validateStundenposition(hours: Double, hourlyrate: Double): Validated[Seq[ErrorMessage], InvoicePositionData.Stundenposition] = {
        (
            valideteHours(hours),
            valideteHourlyrate(hourlyrate)
        ).mapN(InvoicePositionData.Stundenposition.apply)
    }
    def validateStundenposition(stundenposition: InvoicePositionData.Stundenposition): Validated[Seq[ErrorMessage], InvoicePositionData.Stundenposition] = {
        validateStundenposition(
            stundenposition.hours,
            stundenposition.hourlyrate
        )
    }
}
object StundenpositionValidator extends StundenpositionValidator
