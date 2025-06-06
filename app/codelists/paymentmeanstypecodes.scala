package codelists

import utility._
import cats.data._
import cats.data.Validated._
import cats.syntax.all._


object PaymentMeansTypeCode {
    def matchStr(str: String): Option[PaymentMeansTypeCode] = {
        PaymentMeansTypeCode.values.find(_.toString == str)
    }
    def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(code: String): Validated[Seq[ErrorMessage], PaymentMeansTypeCode] = {
        Validated.cond(
            PaymentMeansTypeCode.strInList(code),
            PaymentMeansTypeCode.matchStr(code).get,
            Seq(ArgumentError)
        )
    }
}

enum PaymentMeansTypeCode{
    case
        // TODO: How to include codes that start with a number?
        // 1,
        // 2,
        // 3,
        // 4,
        // 5,
        // 6,
        // 7,
        // 8,
        // 9,
        // 10,
        // 11,
        // 12,
        // 13,
        // 14,
        // 15,
        // 16,
        // 17,
        // 18,
        // 19,
        // 20,
        // 21,
        // 22,
        // 23,
        // 24,
        // 25,
        // 26,
        // 27,
        // 28,
        // 29,
        // 30,
        // 31,
        // 32,
        // 33,
        // 34,
        // 35,
        // 36,
        // 37,
        // 38,
        // 39,
        // 40,
        // 41,
        // 42,
        // 43,
        // 44,
        // 45,
        // 46,
        // 47,
        // 48,
        // 49,
        // 50,
        // 51,
        // 52,
        // 53,
        // 54,
        // 55,
        // 56,
        // 57,
        // 58,
        // 59,
        // 60,
        // 61,
        // 62,
        // 63,
        // 64,
        // 65,
        // 66,
        // 67,
        // 68,
        // 70,
        // 74,
        // 75,
        // 76,
        // 77,
        // 78,
        // 91,
        // 92,
        // 93,
        // 94,
        // 95,
        // 96,
        // 97,
        ZZZ
}
