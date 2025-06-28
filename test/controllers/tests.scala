package test

import utility.validation._
import codelists._
import sharedUtility.validation._

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

def addDefaultSource(str: String): InputType = {
    InputType(str, "")
}

class MUnitTest extends munit.FunSuite {
    test("testing PostCode") {
        // Standart case, numbers only
        val test1 = PostCode.validate(addDefaultSource("12345"))
        assertEquals(test1.map(_.get), Valid("12345"))
        // mixture of numbers, letters and whitespace
        val test2 = PostCode.validate(addDefaultSource("DE 12345"))
        assertEquals(test2.map(_.get), Valid("DE 12345"))
        // test maximum length
        val test3 = PostCode.validate(addDefaultSource("1"*50))
        assertEquals(test3.map(_.get), Valid("1"*50))

        // test invalid case: more than 50 letters
        val test4 = PostCode.validate(addDefaultSource("1"*50+"1"))
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing City") {
        // test regular case, ASCII letters only
        val test1 = City.validate(addDefaultSource("Freiburg"))
        assertEquals(test1.map(_.get), Valid("Freiburg"))
        // test 'ö'
        val test2 = City.validate(addDefaultSource("Köln"))
        assertEquals(test2.map(_.get), Valid("Köln"))
        // test 'ø'
        val test3 = City.validate(addDefaultSource("Hillerød"))
        assertEquals(test3.map(_.get), Valid("Hillerød"))
        // test ciryllic letters (Kyjiw)
        val test4 = City.validate(addDefaultSource("Київ"))
        assertEquals(test4.map(_.get), Valid("Київ"))
        // test chinese letters (Peking)
        val test5 = City.validate(addDefaultSource("北京市"))
        assertEquals(test5.map(_.get), Valid("北京市"))
        // test several words with whitespace
        val test6 = City.validate(addDefaultSource("New York"))
        assertEquals(test6.map(_.get), Valid("New York"))
        // test maximum length
        val test7 = City.validate(addDefaultSource("1234567890"*10))
        assertEquals(test7.map(_.get), Valid("1234567890"*10))

        // test invalid case: more than 100 letters
        val test8 = City.validate(addDefaultSource("1234567890"*10+"1"))
        assert(assertInvalid(test8.map(_.get)))
    }

    test("testing Name") {
        // test regular case
        val test1 = Name.validate(addDefaultSource("vorname nachname"))
        assertEquals(test1.map(_.get), Valid("vorname nachname"))
        // test japanese literals
        val test4 = Name.validate(addDefaultSource("山田花子"))
        assertEquals(test4.map(_.get), Valid("山田花子"))
        // test -
        val test5 = Name.validate(addDefaultSource("Sören-Gandalf"))
        assertEquals(test5.map(_.get), Valid("Sören-Gandalf"))
        // test ' (e.g. Irish names)
        val test6 = Name.validate(addDefaultSource("Caoimhe O'Conner"))
        assertEquals(test6.map(_.get), Valid("Caoimhe O'Conner"))
        // test & (company names)
        val test7 = Name.validate(addDefaultSource("Zimmermann & Söhne"))
        assertEquals(test7.map(_.get), Valid("Zimmermann & Söhne"))
        // test maximum length
        val test2 = Name.validate(addDefaultSource("1234567890"*10))
        assertEquals(test2.map(_.get), Valid("1234567890"*10))

        // test invalid case: more than 100 letters
        val test3 = Name.validate(addDefaultSource("1234567890"*10+"1"))
        assert(assertInvalid(test3.map(_.get)))
    }

    test("testing Email") {
        // test regular case, simple mail
        val test1 = Email.validate(addDefaultSource("name@mail.com"))
        assertEquals(test1.map(_.get), Valid("name@mail.com"))
        // test minimun size
        val test16 = Email.validate(addDefaultSource("n@m"))
        assertEquals(test16.map(_.get), Valid("n@m"))
        // test regular case, numbers only
        val test17 = Email.validate(addDefaultSource("123@456"))
        assertEquals(test17.map(_.get), Valid("123@456"))
        // test . left of @
        val test2 = Email.validate(addDefaultSource("name.lastname@mail.com"))
        assertEquals(test2.map(_.get), Valid("name.lastname@mail.com"))
        // test several . left of @
        val test13 = Email.validate(addDefaultSource("name.lastname.some.more.names@mail.com"))
        assertEquals(test13.map(_.get), Valid("name.lastname.some.more.names@mail.com"))
        // test . right of @
        val test3 = Email.validate(addDefaultSource("name@mail.com.de"))
        assertEquals(test3.map(_.get), Valid("name@mail.com.de"))
        // test several . right of @
        val test14 = Email.validate(addDefaultSource("name@mail.com.de.bw.fr"))
        assertEquals(test14.map(_.get), Valid("name@mail.com.de.bw.fr"))
        // test . left and right of @
        val test4 = Email.validate(addDefaultSource("name.lastname@mail.com.de"))
        assertEquals(test4.map(_.get), Valid("name.lastname@mail.com.de"))
        // test several . left and right of @
        val test15 = Email.validate(addDefaultSource("name.lastname.some.more.names@mail.com.de.bw.fr"))
        assertEquals(test15.map(_.get), Valid("name.lastname.some.more.names@mail.com.de.bw.fr"))
        // test _
        val test5 = Email.validate(addDefaultSource("name_middlename@mail.com"))
        assertEquals(test5.map(_.get), Valid("name_middlename@mail.com"))
        // test +
        val test6 = Email.validate(addDefaultSource("name+jobtitle@mail.com"))
        assertEquals(test6.map(_.get), Valid("name+jobtitle@mail.com"))
        // test _ with . left of @
        val test7 = Email.validate(addDefaultSource("name_middlename.lastname@mail.com"))
        assertEquals(test7.map(_.get), Valid("name_middlename.lastname@mail.com"))
        // test + with . left of @
        val test8 = Email.validate(addDefaultSource("name+jobtitle.lastname@mail.com"))
        assertEquals(test8.map(_.get), Valid("name+jobtitle.lastname@mail.com"))
        // test _ with . right of @
        val test9 = Email.validate(addDefaultSource("name_middlename@mail.com.de"))
        assertEquals(test9.map(_.get), Valid("name_middlename@mail.com.de"))
        // test + with . right of @
        val test10 = Email.validate(addDefaultSource("name+jobtitle@mail.com.de"))
        assertEquals(test10.map(_.get), Valid("name+jobtitle@mail.com.de"))
        // test _ with . left and right of @
        val test11 = Email.validate(addDefaultSource("name_middlename.lastname@mail.com.de"))
        assertEquals(test11.map(_.get), Valid("name_middlename.lastname@mail.com.de"))
        // test + with . left and right of @
        val test12 = Email.validate(addDefaultSource("name+jobtitle.lastname@mail.com.de"))
        assertEquals(test12.map(_.get), Valid("name+jobtitle.lastname@mail.com.de"))

        // test invalid case: no @
        val test20 = Email.validate(addDefaultSource("namemail"))
        assert(assertInvalid(test20.map(_.get)))
        // test invalid case: nothing left of @
        val test21 = Email.validate(addDefaultSource("name@"))
        assert(assertInvalid(test21.map(_.get)))
        // test invalid case: nothing right of @
        val test22 = Email.validate(addDefaultSource("@mail"))
        assert(assertInvalid(test22.map(_.get)))
        // test invalid case: .. left of @ 
        val test23 = Email.validate(addDefaultSource("name..lastname@mail"))
        assert(assertInvalid(test23.map(_.get)))
        // test invalid case: .. right of @
        val test26 = Email.validate(addDefaultSource("name@mail..de"))
        assert(assertInvalid(test26.map(_.get)))
        // test invalid case: . directly after @
        val test24 = Email.validate(addDefaultSource("name@.mail"))
        assert(assertInvalid(test24.map(_.get)))
        // test invalid case: . directly before @
        val test27 = Email.validate(addDefaultSource("name.@mail"))
        assert(assertInvalid(test27.map(_.get)))
        // test invalid case: . at the beginning
        val test25 = Email.validate(addDefaultSource(".name@mail"))
        assert(assertInvalid(test25.map(_.get)))
        // test invalid case: . at the end
        val test28 = Email.validate(addDefaultSource("name@mail."))
        assert(assertInvalid(test28.map(_.get)))
        // test invalid case: , left of @
        val test29 = Email.validate(addDefaultSource("name,lastname@mail"))
        assert(assertInvalid(test29.map(_.get)))
        // test invalid case: , right of @
        val test30 = Email.validate(addDefaultSource("name@mail,de"))
        assert(assertInvalid(test30.map(_.get)))
        // test invalid case: whitespace left of @
        val test31 = Email.validate(addDefaultSource("name lastname@mail"))
        assert(assertInvalid(test31.map(_.get)))
        // test invalid case: whitespace right of @
        val test32 = Email.validate(addDefaultSource("name@mail de"))
        assert(assertInvalid(test32.map(_.get)))
    }

    test("testing PaymentTerms") {
        // test regular case
        val test1 = PaymentTerms.validate(addDefaultSource("Zahlung innerhalb von 2 Wochen"))
        assertEquals(test1.map(_.get), Valid("Zahlung innerhalb von 2 Wochen"))
        // test maximum length
        val test2 = PaymentTerms.validate(addDefaultSource("1234567890"*100))
        assertEquals(test2.map(_.get), Valid("1234567890"*100))

        // test invalid case: more than 1000 letters
        val test3 = PaymentTerms.validate(addDefaultSource("1234567890"*100+"1"))
        assert(assertInvalid(test3.map(_.get)))
    }

    test("testing Street") {
        // test regular case
        val test1 = Street.validate(addDefaultSource("Gartenweg"))
        assertEquals(test1.map(_.get), Valid("Gartenweg"))
        // test whitespace
        val test2 = Street.validate(addDefaultSource("Charlottenburger Straße"))
        assertEquals(test2.map(_.get), Valid("Charlottenburger Straße"))
        // test whitespace with number
        val test3 = Street.validate(addDefaultSource("Charlottenburger Straße 2"))
        assertEquals(test3.map(_.get), Valid("Charlottenburger Straße 2"))
        // test maximum length
        val test4 = Street.validate(addDefaultSource("1234567890"*10))
        assertEquals(test4.map(_.get), Valid("1234567890"*10))

        // test invalid case: more than 100 letters
        val test5 = Street.validate(addDefaultSource("1234567890"*10+"1"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing Date") {
        // test regular case
        val test1 = Date.validate(addDefaultSource("20000101"))
        assertEquals(test1.map(Date.get(_)), Valid("20000101"))

        // test invalid case: not a number
        val test2 = Date.validate(addDefaultSource("a0000101"))
        assert(assertInvalid(test2.map(Date.get(_))))
        // test invalid case: whitespace, correct lenght of number
        val test11 = Date.validate(addDefaultSource("1111 2222"))
        assert(assertInvalid(test11.map(Date.get(_))))
        // test invalid case: whitespace, correct length of string
        val test12 = Date.validate(addDefaultSource("1111 222"))
        assert(assertInvalid(test12.map(Date.get(_))))
        // test invalid case: negative number
        val test3 = Date.validate(addDefaultSource("-9990101"))
        assert(assertInvalid(test3.map(Date.get(_))))
        // test invalid case: date format as it is communicated by a html-input with input type="date"
        val test4 = Date.validate(addDefaultSource("2000-01-01"))
        assert(assertInvalid(test4.map(Date.get(_))))

        // test invalid case: too short
        val test5 = Date.validate(addDefaultSource("1999130"))
        assert(assertInvalid(test5.map(Date.get(_))))
        // test invalid case: too long
        val test6 = Date.validate(addDefaultSource("199913010"))
        assert(assertInvalid(test6.map(Date.get(_))))

        // test invalid case: month > 12
        val test7 = Date.validate(addDefaultSource("19991301"))
        assert(assertInvalid(test7.map(Date.get(_))))
        // test invalid case: month < 1
        val test8 = Date.validate(addDefaultSource("19990001"))
        assert(assertInvalid(test8.map(Date.get(_))))
        // test invalid case: day > 31
        val test9 = Date.validate(addDefaultSource("19990132"))
        assert(assertInvalid(test9.map(Date.get(_))))
        // test invalid case: day < 1
        val test10 = Date.validate(addDefaultSource("19990100"))
        assert(assertInvalid(test10.map(Date.get(_))))
        // TODO: combination of wrong day and month
    }

    test("testing VATRate") {
        // test regular case
        val test1 = VATRate.validate(addDefaultSource("19.0"))
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = VATRate.validate(addDefaultSource("0.0"))
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = VATRate.validate(addDefaultSource("19"))
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = VATRate.validate(addDefaultSource("a.bc"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = VATRate.validate(addDefaultSource("-19.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing Quantity") {
        // test regular case
        val test1 = Quantity.validate(addDefaultSource("19.0"))
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = Quantity.validate(addDefaultSource("0.0"))
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = Quantity.validate(addDefaultSource("19"))
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = Quantity.validate(addDefaultSource("a.bc"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = Quantity.validate(addDefaultSource("-19.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing NetPrice") {
        // test regular case
        val test1 = NetPrice.validate(addDefaultSource("19.0"))
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = NetPrice.validate(addDefaultSource("0.0"))
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = NetPrice.validate(addDefaultSource("19"))
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = NetPrice.validate(addDefaultSource("a.bc"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = NetPrice.validate(addDefaultSource("-19.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing NetAmount") {
        // test regular case
        val test1 = NetAmount.validate(addDefaultSource("10.0"), addDefaultSource("10.0"))
        assertEquals(test1.map(_.get), Valid(100.0))
        // test without decimal point
        val test2 = NetAmount.validate(addDefaultSource("10"), addDefaultSource("10"))
        assertEquals(test2.map(_.get), Valid(100.0))

        // test invalid case: quantity is invalid
        val test3 = NetAmount.validate(addDefaultSource("-10.0"), addDefaultSource("10.0"))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: netPrice is invalid
        val test4 = NetAmount.validate(addDefaultSource("10.0"), addDefaultSource("-10.0"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: quantity and netPrice are invalid
        val test5 = NetAmount.validate(addDefaultSource("-10.0"), addDefaultSource("-10.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing PositionName") {
        // test regular case
        val test1 = PositionName.validate(addDefaultSource("Schraube"))
        assertEquals(test1.map(_.get), Valid("Schraube"))
        // test _
        val test2 = PositionName.validate(addDefaultSource("Reifen_rot"))
        assertEquals(test2.map(_.get), Valid("Reifen_rot"))
        // test "
        val test3 = PositionName.validate(addDefaultSource("Kleber \"Klebemeister\""))
        assertEquals(test3.map(_.get), Valid("Kleber \"Klebemeister\""))
        // test ,
        val test4 = PositionName.validate(addDefaultSource("Schraubenzieherset, neu"))
        assertEquals(test4.map(_.get), Valid("Schraubenzieherset, neu"))
        // test .
        val test5 = PositionName.validate(addDefaultSource("Miete Beratungsraum Nr.2"))
        assertEquals(test5.map(_.get), Valid("Miete Beratungsraum Nr.2"))
        // test #
        val test6 = PositionName.validate(addDefaultSource("Miete Beratungsraum #2"))
        assertEquals(test6.map(_.get), Valid("Miete Beratungsraum #2"))
    }

    test("testing Hours") {
        // test regular case
        val test1 = Hours.validate(addDefaultSource("19.0"))
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = Hours.validate(addDefaultSource("0.0"))
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = Hours.validate(addDefaultSource("19"))
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = Hours.validate(addDefaultSource("a.bc"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = Hours.validate(addDefaultSource("-19.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing HourlyRate") {
        // test regular case
        val test1 = HourlyRate.validate(addDefaultSource("19.0"))
        assertEquals(test1.map(_.get), Valid(19.0))
        // test 0.0
        val test2 = HourlyRate.validate(addDefaultSource("0.0"))
        assertEquals(test2.map(_.get), Valid(0.0))
        // test without decimal point
        val test3 = HourlyRate.validate(addDefaultSource("19"))
        assertEquals(test3.map(_.get), Valid(19.0))

        // test invalid case: not a number
        val test4 = HourlyRate.validate(addDefaultSource("a.bc"))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: negative number
        val test5 = HourlyRate.validate(addDefaultSource("-19.0"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing CountryCode") {
        // test regular case (member of Enum)
        val test1 = CountryCode.validate(addDefaultSource("DE"))
        assertEquals(test1.map(_.get), Valid("DE"))

        // test invalid case (not a member of Enum)
        val test2 = CountryCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = CountryCode.validate(addDefaultSource(""))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: lower case letters
        val test4 = CountryCode.validate(addDefaultSource("de"))
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing CurrencyCode") {
        // test regular case (member of Enum)
        val test1 = CurrencyCode.validate(addDefaultSource("EUR"))
        assertEquals(test1.map(_.get), Valid("EUR"))

        // test invalid case (not a member of Enum)
        val test2 = CurrencyCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = CurrencyCode.validate(addDefaultSource(""))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: lower case letters
        val test4 = CurrencyCode.validate(addDefaultSource("eur"))
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing MeasurementCode") {
        // test regular case (member of Enum)
        val test1 = MeasurementCode.validate(addDefaultSource("H87"))
        assertEquals(test1.map(_.get), Valid("H87"))
        // test regular case (member of Enum, original code starts with a number)
        val test2 = MeasurementCode.validate(addDefaultSource("35"))
        assertEquals(test2.map(_.get), Valid("35"))

        // test invalid case (not a member of Enum)
        val test3 = MeasurementCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: empty String
        val test4 = MeasurementCode.validate(addDefaultSource(""))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: lower case letters
        val test5 = MeasurementCode.validate(addDefaultSource("h87"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing PaymentMeansTypeCode") {
        // test regular case (member of Enum)
        val test1 = PaymentMeansTypeCode.validate(addDefaultSource("ZZZ"))
        assertEquals(test1.map(_.get), Valid("ZZZ"))
        // test regular case (member of Enum, original code starts with a number)
        val test2 = PaymentMeansTypeCode.validate(addDefaultSource("1"))
        assertEquals(test2.map(_.get), Valid("1"))

        // test invalid case (not a member of Enum)
        val test3 = PaymentMeansTypeCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: empty String
        val test4 = PaymentMeansTypeCode.validate(addDefaultSource(""))
        assert(assertInvalid(test4.map(_.get)))
        // test invalid case: lower case letters
        val test5 = PaymentMeansTypeCode.validate(addDefaultSource("zzz"))
        assert(assertInvalid(test5.map(_.get)))
    }

    test("testing VATCategoryCode") {
        // test regular case (member of Enum)
        val test1 = VATCategoryCode.validate(addDefaultSource("S"))
        assertEquals(test1.map(_.get), Valid("S"))

        // test invalid case (not a member of Enum)
        val test2 = VATCategoryCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = VATCategoryCode.validate(addDefaultSource(""))
        assert(assertInvalid(test3.map(_.get)))
        // test invalid case: lower case letters
        val test4 = VATCategoryCode.validate(addDefaultSource("s"))
        assert(assertInvalid(test4.map(_.get)))
    }

    test("testing InvoiceTypeCode") {
        // test regular case (member of Enum)
        val test1 = InvoiceTypeCode.validate(addDefaultSource("380"))
        assertEquals(test1.map(_.get), Valid("380"))

        // test invalid case (not a member of Enum)
        val test2 = InvoiceTypeCode.validate(addDefaultSource("XY"))
        assert(assertInvalid(test2.map(_.get)))
        // test invalid case: empty String
        val test3 = InvoiceTypeCode.validate(addDefaultSource(""))
        assert(assertInvalid(test3.map(_.get)))
    }
}
