package utility.codelists

import utility.validation._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


case class PaymentMeansTypeCode(code: String) extends ValidateAble[String](code)
object PaymentMeansTypeCode {
    private def matchStr(str: String): Option[PaymentMeansTypeCode] = {
        PaymentMeansTypeCodes.values.find(_.toString == "Code_" ++ str) match 
            case Some(_) =>
                Some(PaymentMeansTypeCode(str))
            case None => 
                None
    }
    private def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(code: String): Validated[Seq[ErrorMessage], PaymentMeansTypeCode] = {
        Validated.cond(
            PaymentMeansTypeCode.strInList(code),
            PaymentMeansTypeCode.matchStr(code).get,
            Seq(ValueNotInCodelistError(code))
        )
    }
}

// Added Code_ to enable the Enum to work with codes that start with numbers, and preserve uniformity
enum PaymentMeansTypeCodes{
    case
        Code_1,
        Code_2,
        Code_3,
        Code_4,
        Code_5,
        Code_6,
        Code_7,
        Code_8,
        Code_9,
        Code_10,
        Code_11,
        Code_12,
        Code_13,
        Code_14,
        Code_15,
        Code_16,
        Code_17,
        Code_18,
        Code_19,
        Code_20,
        Code_21,
        Code_22,
        Code_23,
        Code_24,
        Code_25,
        Code_26,
        Code_27,
        Code_28,
        Code_29,
        Code_30,
        Code_31,
        Code_32,
        Code_33,
        Code_34,
        Code_35,
        Code_36,
        Code_37,
        Code_38,
        Code_39,
        Code_40,
        Code_41,
        Code_42,
        Code_43,
        Code_44,
        Code_45,
        Code_46,
        Code_47,
        Code_48,
        Code_49,
        Code_50,
        Code_51,
        Code_52,
        Code_53,
        Code_54,
        Code_55,
        Code_56,
        Code_57,
        Code_58,
        Code_59,
        Code_60,
        Code_61,
        Code_62,
        Code_63,
        Code_64,
        Code_65,
        Code_66,
        Code_67,
        Code_68,
        Code_70,
        Code_74,
        Code_75,
        Code_76,
        Code_77,
        Code_78,
        Code_91,
        Code_92,
        Code_93,
        Code_94,
        Code_95,
        Code_96,
        Code_97,
        Code_ZZZ
}
