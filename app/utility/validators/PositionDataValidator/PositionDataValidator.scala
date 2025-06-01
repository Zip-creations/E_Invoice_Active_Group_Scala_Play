package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait PositionDataValidator {
    def validatePositionData(positionData: Validated[Seq[ErrorMessage], InvoicePosition]): Validated[Seq[ErrorMessage], InvoicePositionData] = {
        positionData match {
            case Valid(a) => 
                a.data match {
                    case InvoicePositionData.Stundenposition(hours, hourlyrate) =>
                            StundenpositionValidator.validateStundenposition(hours, hourlyrate)
                    case InvoicePositionData.Leistungsposition(quantity, pricePerPart, measurementCode) =>
                            LeistungspositionValidator.validateLeistungsposition(quantity, pricePerPart, measurementCode)
                }
            case Invalid(e) =>
                Invalid(e)
        }
    }
}
object PositionDataValidator extends PositionDataValidator
