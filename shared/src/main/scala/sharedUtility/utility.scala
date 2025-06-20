package sharedUtility.utility

def fillWithZero(str: String): String = {
    if (str.length == 2) str else "0" + str
}
