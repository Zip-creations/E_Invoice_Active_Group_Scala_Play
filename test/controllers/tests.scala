package test

import utility._
import codelists._

import munit.FunSuite
import cats.data._
import cats.data.Validated._
import cats.syntax.all._


class MUnitTest extends munit.FunSuite {
    test("PostCode") {
        val test1 = PostCode.validate("12345")
        assertEquals(test1.map(_.get), Valid("12345"))
        val test2 = PostCode.validate("DE 12345")
        assertEquals(test2.map(_.get), Valid("DE 12345"))
    }
    test("City") {
        val test1 = City.validate("Freiburg")
        assertEquals(test1.map(_.get), Valid("Freiburg"))
        val test2 = City.validate("Köln")
        assertEquals(test2.map(_.get), Valid("Köln"))
        val test3 = City.validate("Hillerød")
        assertEquals(test3.map(_.get), Valid("Hillerød"))
        val test4 = City.validate("Київ")
        assertEquals(test4.map(_.get), Valid("Київ"))
        val test5 = City.validate("北京市")
        assertEquals(test5.map(_.get), Valid("北京市"))
        val test6 = City.validate("New York")
        assertEquals(test6.map(_.get), Valid("New York"))
    }
}
