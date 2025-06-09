package utility.validation

// Each function shall return if the test result is negative (so false if a invalid case has been found, and true if the input is valid)
// so the result of each validation function can be used in a disjuction

// Tests for two or more consecutive whitespaces
def NoDoubleWhitespace(str: String): Boolean = {
    !str.contains("  ")
}
