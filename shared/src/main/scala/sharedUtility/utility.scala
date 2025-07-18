package sharedUtility.utility

def fillWithZero(str: String, len: Int): String = {
    val strLen = str.length
    if (strLen == len) str else "0" * (len - strLen) + str
}

def getVatCategory(vatID: String) = {
    vatID.split(",")(0)
}

def getVatRate(vatID: String) = {
    vatID.split(",")(1)
}
