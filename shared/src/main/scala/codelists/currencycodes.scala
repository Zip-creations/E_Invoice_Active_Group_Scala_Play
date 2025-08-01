package codelists

import sharedUtility.validation._
import sharedUtility.error._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class CurrencyCode private(code: String) extends ValidateAble[String](code)
object CurrencyCode {
    private def matchStr(str: String): Option[CurrencyCode] = {
        CurrencyCodes.values.find(_.toString == "Code_" ++ str) match 
            case Some(_) =>
                Some(CurrencyCode(str))
            case None => 
                None
    }
    private def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(input: InputType): Validated[Seq[ErrorMessage], CurrencyCode] = {
        val code = input.value
        Validated.cond(
            CurrencyCode.strInList(code),
            CurrencyCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(makeError("Für einen Währungscode wurde ein inkorrekter Code angegeben.", input)))
        )
    }
}

// Added Code_ to enable the Enum to work with codes that start with numbers, and preserve uniformity
enum CurrencyCodes {
    case
        Code_AED,
        Code_AFN,
        Code_ALL,
        Code_AMD,
        Code_ANG,
        Code_AOA,
        Code_ARS,
        Code_AUD,
        Code_AWG,
        Code_AZN,
        Code_BAM,
        Code_BBD,
        Code_BDT,
        Code_BGN,
        Code_BHD,
        Code_BIF,
        Code_BMD,
        Code_BND,
        Code_BOB,
        Code_BOV,
        Code_BRL,
        Code_BSD,
        Code_BTN,
        Code_BWP,
        Code_BYN,
        Code_BZD,
        Code_CAD,
        Code_CDF,
        Code_CHE,
        Code_CHF,
        Code_CHW,
        Code_CLF,
        Code_CLP,
        Code_CNY,
        Code_COP,
        Code_COU,
        Code_CRC,
        Code_CUC,
        Code_CUP,
        Code_CVE,
        Code_CZK,
        Code_DJF,
        Code_DKK,
        Code_DOP,
        Code_DZD,
        Code_EGP,
        Code_ERN,
        Code_ETB,
        Code_EUR,
        Code_FJD,
        Code_FKP,
        Code_GBP,
        Code_GEL,
        Code_GHS,
        Code_GIP,
        Code_GMD,
        Code_GNF,
        Code_GTQ,
        Code_GYD,
        Code_HKD,
        Code_HNL,
        Code_HRK,
        Code_HTG,
        Code_HUF,
        Code_IDR,
        Code_ILS,
        Code_INR,
        Code_IQD,
        Code_IRR,
        Code_ISK,
        Code_JMD,
        Code_JOD,
        Code_JPY,
        Code_KES,
        Code_KGS,
        Code_KHR,
        Code_KMF,
        Code_KPW,
        Code_KRW,
        Code_KWD,
        Code_KYD,
        Code_KZT,
        Code_LAK,
        Code_LBP,
        Code_LKR,
        Code_LRD,
        Code_LSL,
        Code_LYD,
        Code_MAD,
        Code_MDL,
        Code_MGA,
        Code_MKD,
        Code_MMK,
        Code_MNT,
        Code_MOP,
        Code_MRO,
        Code_MUR,
        Code_MVR,
        Code_MWK,
        Code_MXN,
        Code_MXV,
        Code_MYR,
        Code_MZN,
        Code_NAD,
        Code_NGN,
        Code_NIO,
        Code_NOK,
        Code_NPR,
        Code_NZD,
        Code_OMR,
        Code_PAB,
        Code_PEN,
        Code_PGK,
        Code_PHP,
        Code_PKR,
        Code_PLN,
        Code_PYG,
        Code_QAR,
        Code_RON,
        Code_RSD,
        Code_RUB,
        Code_RWF,
        Code_SAR,
        Code_SBD,
        Code_SCR,
        Code_SDG,
        Code_SEK,
        Code_SGD,
        Code_SHP,
        Code_SLL,
        Code_SOS,
        Code_SRD,
        Code_SSP,
        Code_STD,
        Code_SVC,
        Code_SYP,
        Code_SZL,
        Code_THB,
        Code_TJS,
        Code_TMT,
        Code_TND,
        Code_TOP,
        Code_TRY,
        Code_TTD,
        Code_TWD,
        Code_TZS,
        Code_UAH,
        Code_UGX,
        Code_USD,
        Code_USN,
        Code_UYI,
        Code_UYU,
        Code_UZS,
        Code_VEF,
        Code_VND,
        Code_VUV,
        Code_WST,
        Code_XAF,
        Code_XAG,
        Code_XAU,
        Code_XBA,
        Code_XBB,
        Code_XBC,
        Code_XBD,
        Code_XCD,
        Code_XDR,
        Code_XOF,
        Code_XPD,
        Code_XPF,
        Code_XPT,
        Code_XSU,
        Code_XTS,
        Code_XUA,
        Code_XXX,
        Code_YER,
        Code_ZAR,
        Code_ZMW,
        Code_ZWL,
}