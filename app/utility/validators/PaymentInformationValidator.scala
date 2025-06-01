package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


sealed trait PaymentInformationValidator {
    def valideteCurrencyCode(currencycode: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            currencycode,
            Seq(ArgumentError)
        )
    }
    def validatePaymentMeansCode(code: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            code,
            Seq(ArgumentError)
        )
    }
    def validateVATGroup(vatGroups: List[InvoiceVATGroup]): Validated[Seq[ErrorMessage], List[InvoiceVATGroup]] = {
        vatGroups.traverse(VATGroupValidator.validateVATGroup)
    }
    def validatePaymentTerms(terms: String): Validated[Seq[ErrorMessage], String] = {
        Validated.cond(
            true,
            terms,
            Seq(ArgumentError)
        )
    }
    def validatePaymentInformation(currencycode: String, paymentMeansCode: String, vatGroups: List[InvoiceVATGroup], paymentTerms: String = ""): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        (
            valideteCurrencyCode(currencycode),
            validatePaymentMeansCode(paymentMeansCode),
            validateVATGroup(vatGroups),
            validatePaymentTerms(paymentTerms)
        ).mapN(InvoicePaymentInformation.apply)
    }
    def validatePaymentInformation(paymentInformation: InvoicePaymentInformation): Validated[Seq[ErrorMessage], InvoicePaymentInformation] = {
        validatePaymentInformation(
            paymentInformation.currencycode,
            paymentInformation.paymentMeansCode,
            paymentInformation.vatGroups,
            paymentInformation.paymentTerms
        )
    }
}
object PaymentInformationValidator extends PaymentInformationValidator
