package utility

import cats.data._
import cats.data.Validated._
import cats.syntax.all._

case class PostCode private(postcode: String)
object PostCode {
    def validate(postcode: String): Validated[Seq[ErrorMessage], PostCode] = {
        Validated.cond(
            postcode != "FEHLERTEST",
            PostCode(postcode),
            Seq(ArgumentError)
        )
    }
}

case class City private(city: String)
object City {
    def validate(city: String): Validated[Seq[ErrorMessage], City] = {
        Validated.cond(
            true,
            City(city),
            Seq(ArgumentError)
        )
    }
}
