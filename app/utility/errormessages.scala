package utility

sealed trait ErrorMessage {
    def errorMessage: String
}

case object ArgumentError extends ErrorMessage {
    def errorMessage: String = "Given value is invalid"
}

case object ValueNotInCodelist extends ErrorMessage {
    def errorMessage: String = "Given value was not found in codelist"
}
