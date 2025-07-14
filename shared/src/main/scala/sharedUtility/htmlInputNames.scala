package sharedUtility.inputNames

enum InputName {
    // def indexed(index: Int = 1) = {
    //     this match {
    //         case ItemName => f"$this$index"
    //         case _ => this
    //     }
    // }
    case InvoiceNumber
    case InvoiceIssueDate
    case ItemName(index: Int)

    // Both can be called, but ItemName should only be callable with an index:
    // println(InputName.ItemName)
    // println(InputName.ItemName(5))
}

sealed trait InputName2
object InputName2 {
    case object InvoiceNumber extends InputName2
    case class ItemName(index: Int) extends InputName2
}

object InputName3 {
    def InvoiceNumber = InputName4.NoIndex("InvoiceNumber")
    def ItemName(index: Int) = InputName4.Indexed("ItemName", index)
}

enum InputName4 {
    case Indexed(name: String, index: Int)
    case NoIndex(name: String)
}
