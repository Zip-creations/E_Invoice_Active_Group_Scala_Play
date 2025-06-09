package utility.validation

// Each function shall return if the test result is positive (so true if a invalid case has been found, false if the input is valid)
// so the result of each validation function can be used in a disjuction

def proof(bools: Boolean*): Boolean = {
    !bools.contains(true)
}

// Tests for two or more consecutive whitespaces
def ContainsDoubleWhitespace(str: String): Boolean = {
    str.contains("  ")
}
