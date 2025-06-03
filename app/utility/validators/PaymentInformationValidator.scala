package utility

import codelists._
import cats.data._
import cats.data.Validated._
import cats.syntax.all._
import java.util.Currency


sealed trait PaymentInformationValidator {

    def validatePaymentMeansCode(code: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            code,
            Seq(ArgumentError)
        )
    }
    def validatePaymentTerms(terms: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            terms,
            Seq(ArgumentError)
        )
    }
    def validatePaymentInformation(currencycode: String, paymentMeansCode: String, vatGroups: List[Validated[Seq[ErrorMessage], InvoiceVATGroup]], paymentTerms: String = ""): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        (
            CurrencyCode.validate(currencycode),
            validatePaymentMeansCode(paymentMeansCode),
            vatGroups.sequence,
            validatePaymentTerms(paymentTerms)
        ).mapN(InvoicePaymentInformation.apply)
    }
    // def validatePaymentInformation(paymentInformation: InvoicePaymentInformation): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
    //     validatePaymentInformation(
    //         paymentInformation.currencycode,
    //         paymentInformation.paymentMeansCode,
    //         paymentInformation.vatGroups,
    //         paymentInformation.paymentTerms
    //     )
    // }
}
object PaymentInformationValidator extends PaymentInformationValidator
