package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

sealed trait LeistungspositionValidator {
    def valideteQuantity(quantity: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            quantity,
            Seq(ArgumentError)
        )
    }
    def validetePricePerPart(pricePerPart: Double): Validated[Seq[ErrorMessage], Double] = {
        Validated.cond(
            true,
            pricePerPart,
            Seq(ArgumentError)
        )
    }
    def valideteMeasurementCode(measurementCode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            measurementCode,
            Seq(ArgumentError)
        )
    }
    def validateLeistungsposition(quantity: Double, pricePerPart: Double, measurementCode: String): Validated[Seq[ErrorMessage], InvoicePositionData.Leistungsposition] = {
        (
            valideteQuantity(quantity),
            validetePricePerPart(pricePerPart),
            valideteMeasurementCode(measurementCode)
        ).mapN(InvoicePositionData.Leistungsposition.apply)
    }
    def validateLeistungsposition(leistungsposition: InvoicePositionData.Leistungsposition): Validated[Seq[ErrorMessage], InvoicePositionData.Leistungsposition] = {
        validateLeistungsposition(
            leistungsposition.quantity,
            leistungsposition.pricePerPart,
            leistungsposition.measurementCode,
        )
    }
}
object LeistungspositionValidator extends LeistungspositionValidator
