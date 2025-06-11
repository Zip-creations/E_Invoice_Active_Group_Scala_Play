package test

import utility.validation._
import utility.codelists._

import munit.FunSuite
import cats.data._
import cats.data.Validated._
import cats.syntax.all._

def assertInvalid(value: Any): Boolean = {
    value match {
        case Invalid(_) =>
            true
        case _ =>
            false
    }
}

class MUnitTest extends munit.FunSuite {
    test("testing PostCode") {
        // Standart case, numbers only
        val test1 = PostCode.validate("12345")
        assertEquals(test1.map(_.get), Valid("12345"))
        // mixture of numbers, letters and whitespace
        val test2 = PostCode.validate("DE 12345")
        assertEquals(test2.map(_.get), Valid("DE 12345"))
    }

    test("testing City") {
        // test regular case, ASCII letters only
        val test1 = City.validate("Freiburg")
        assertEquals(test1.map(_.get), Valid("Freiburg"))
        // test 'ö'
        val test2 = City.validate("Köln")
        assertEquals(test2.map(_.get), Valid("Köln"))
        // test 'ø'
        val test3 = City.validate("Hillerød")
        assertEquals(test3.map(_.get), Valid("Hillerød"))
        // test ciryllic letters (Kyjiw)
        val test4 = City.validate("Київ")
        assertEquals(test4.map(_.get), Valid("Київ"))
        // test chinese letters (Peking)
        val test5 = City.validate("北京市")
        assertEquals(test5.map(_.get), Valid("北京市"))
        // test several words with whitespace
        val test6 = City.validate("New York")
        assertEquals(test6.map(_.get), Valid("New York"))

        // test invalid case: Double whitespace
        val test7 = City.validate("New  York")
        assert(assertInvalid(test7.map(_.get)))
    }

    test("testing date") {
        // test regular case
        val test1 = Date.validate("20000101")
        assertEquals(test1.map(_.get), Valid("20000101"))

        // test invalid case: not a number
        val test2 = Date.validate("a0000101")
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: whitespace, correct lenght of number
        val test11 = Date.validate("1111 2222")
        assert(assertInvalid(test11.map(_.get)))
        // test invalid case: whitespace, correct length of string
        val test12 = Date.validate("1111 222")
        assert(assertInvalid(test12.map(_.get)))
        // test invalid case: negative number
        val test3 = Date.validate("-9990101")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: date format as it is communicated by a html-input with input type="date"
        val test4 = Date.validate("2000-01-01")
        assert(assertInvalid(test4.map(_.get)))

        // test invalid case: too short
        val test5 = Date.validate("1999130")
        assert(assertInvalid(test5.map(_.get)))
        // test invalid case: too long
        val test6 = Date.validate("199913010")
        assert(assertInvalid(test6.map(_.get)))

        // test invalid case: month > 12
        val test7 = Date.validate("19991301")
        assert(assertInvalid(test7.map(_.get)))
        // test invalid case: month < 1
        val test8 = Date.validate("19990001")
        assert(assertInvalid(test8.map(_.get)))
        // test invalid case: day > 31
        val test9 = Date.validate("19990132")
        assert(assertInvalid(test9.map(_.get)))
        // test invalid case: day < 1
        val test10 = Date.validate("19990100")
        assert(assertInvalid(test10.map(_.get)))
        // TODO: combination of wrong day and month
    }

    test("testing VATRate") {
        // test regular case
        val test1 = VATRate.validate("19.0")
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = VATRate.validate("0.0")
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = VATRate.validate("19")
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = VATRate.validate("a.bc")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = VATRate.validate("-19.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing Quantity") {
        // test regular case
        val test1 = Quantity.validate("19.0")
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = Quantity.validate("0.0")
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = Quantity.validate("19")
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = Quantity.validate("a.bc")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = Quantity.validate("-19.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing NetPrice") {
        // test regular case
        val test1 = NetPrice.validate("19.0")
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = NetPrice.validate("0.0")
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = NetPrice.validate("19")
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = NetPrice.validate("a.bc")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = NetPrice.validate("-19.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing Hours") {
        // test regular case
        val test1 = Hours.validate("19.0")
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = Hours.validate("0.0")
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = Hours.validate("19")
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = Hours.validate("a.bc")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = Hours.validate("-19.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing HourlyRate") {
        // test regular case
        val test1 = HourlyRate.validate("19.0")
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = HourlyRate.validate("0.0")
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = HourlyRate.validate("19")
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = HourlyRate.validate("a.bc")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = HourlyRate.validate("-19.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing NetAmount") {
        // test regular case
        val test1 = NetAmount.validate("10.0", "10.0")
        assertEquals(test1.map(_.get), Valid(100.0))
        // test without decimal point
        val test2 = NetAmount.validate("10", "10")
        assertEquals(test2.map(_.get), Valid(100.0))

        // test invalid case: quantity is invalid
        val test3 = NetAmount.validate("-10.0", "10.0")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: netPrice is invalid
        val test4 = NetAmount.validate("10.0", "-10.0")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: quantity and netPrice are invalid
        val test5 = NetAmount.validate("-10.0", "-10.0")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing CountryCode") {
        // test regular case (member of Enum)
        val test1 = CountryCode.validate("DE")
        assertEquals(test1.map(_.get), Valid("DE"))

        // test invalid case (not a member of Enum)
        val test2 = CountryCode.validate("XY")
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = CountryCode.validate("")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: lower case
        val test4 = CountryCode.validate("de")
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing CurrencyCode") {
        // test regular case (member of Enum)
        val test1 = CurrencyCode.validate("EUR")
        assertEquals(test1.map(_.get), Valid("EUR"))

        // test invalid case (not a member of Enum)
        val test2 = CurrencyCode.validate("XY")
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = CurrencyCode.validate("")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: lower case
        val test4 = CurrencyCode.validate("eur")
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing MeasurementCode") {
        // test regular case (member of Enum)
        val test1 = MeasurementCode.validate("H87")
        assertEquals(test1.map(_.get), Valid("H87"))
        // test regular case (member of Enum, original code starts with a number)
        val test2 = MeasurementCode.validate("35")
        assertEquals(test2.map(_.get), Valid("35"))

        // test invalid case (not a member of Enum)
        val test3 = MeasurementCode.validate("XY")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: empty String
        val test4 = MeasurementCode.validate("")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: lower case
        val test5 = MeasurementCode.validate("h87")
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing PaymentMeansTypeCode") {
        // test regular case (member of Enum)
        val test1 = PaymentMeansTypeCode.validate("ZZZ")
        assertEquals(test1.map(_.get), Valid("ZZZ"))
        // test regular case (member of Enum, original code starts with a number)
        val test2 = PaymentMeansTypeCode.validate("1")
        assertEquals(test2.map(_.get), Valid("1"))

        // test invalid case (not a member of Enum)
        val test3 = PaymentMeansTypeCode.validate("XY")
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: empty String
        val test4 = PaymentMeansTypeCode.validate("")
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: lower case
        val test5 = PaymentMeansTypeCode.validate("zzz")
        assert(assertInvalid(test5.map(_.get)))
    }
}
