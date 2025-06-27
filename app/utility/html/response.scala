package utility.html

import play.api.libs.json._
import sharedUtility.error._

sealed trait Response(val status: Status)
case class OkResponse(val link: String) extends Response(Status.Ok)
case class BadResponse(val errors: Seq[ErrorMessage]) extends Response(Status.Error)

enum Status{
    case
        Ok,
        Error
}
