# E_Invoice_Active_Group_Scala_Play

Dieses Projekt verwendet den Validator den [OpenXRechnungToolbox](https://github.com/jcthiele/OpenXRechnungToolbox) "as is". Lizenz der Toolbox: [GPLv3](Toolbox/license.txt)

Benutzung des E-Rechung Tools:

`sbt run`

im Hauptverzeichnis ausführen,

Tool ist dann unter `localhost:9000` ansprechbar.

Ein Minumum Working Example, welches alle Tests des OpenToolBox-Validators besteht, kann mit diesen Eingaben erzeugt werden, wenn sie über die Konsole des Browsers eingefügt werden:

document.querySelector('[name="InvoiceNumber"]').value = "lalatest123";\
document.querySelector('[name="InvoiceIssueDate"]').value = "2000-01-01";\
document.querySelector('[name="InvoiceTypeCode"]').value = "380";\
document.querySelector('[name="InvoiceCurrencyCode"]').value = "EUR";\
document.querySelector('[name="BuyerReference"]').value = "0";\
document.querySelector('[name="BusinessProcessType"]').value = "0";\
document.querySelector('[name="SpecificationIdentifier"]').value = "0";\
document.querySelector('[name="SellerName"]').value = "0";\
document.querySelector('[name="SellerIdentifier"]').value = "0";\
document.querySelector('[name="SellerElectronicAddress"]').value = "0";\
document.querySelector('[name="SellerCity"]').value = "0";\
document.querySelector('[name="SellerPostCode"]').value = "0";\
document.querySelector('[name="SellerCountryCode"]').value = "DE";\
document.querySelector('[name="SellerContactPoint"]').value = "0";\
document.querySelector('[name="SellerContactTelephoneNumber"]').value = "000";\
document.querySelector('[name="SellerContactEmailAddress"]').value = "0.0@0.0";\
document.querySelector('[name="BuyerName"]').value = "0";\
document.querySelector('[name="BuyerElectronicAddress"]').value = "0";\
document.querySelector('[name="BuyerCity"]').value = "0";\
document.querySelector('[name="BuyerPostCode"]').value = "0";\
document.querySelector('[name="BuyerCountryCode"]').value = "DE";\
document.querySelector('[name="PaymentMeansTypeCode"]').value = "ZZZ";\
document.querySelector('[name="SumOfInvoiceLineNetAmount"]').value = "0";\
document.querySelector('[name="InvoiceTotalAmountWithoutVAT"]').value = "0";\
document.querySelector('[name="InvoiceTotalAmountWithVAT"]').value = "0";\
document.querySelector('[name="AmountDueForPayment"]').value = "0";\
document.querySelector('[name="VATCategoryTaxableAmount"]').value = "0";\
document.querySelector('[name="VATCategoryTaxAmount"]').value = "0";\
document.querySelector('[name="VATCategoryCode"]').value = "O";\
document.querySelector('[name="VATCategoryRate"]').value = "0";\
document.querySelector('[name="VATExemptionReasonText"]').value = "0";\
document.querySelector('[name="InvoiceLineIdentifier"]').value = "0";\
document.querySelector('[name="InvoicedQuantity"]').value = "0";\
document.querySelector('[name="InvoicedQuantityUnitOfMeasureCode"]').value = "H87";\
document.querySelector('[name="InvoiceLineNetAmount"]').value = "0";\
document.querySelector('[name="ItemNetPrice"]').value = "0";\
document.querySelector('[name="InvoicedItemVATCategoryCode"]').value = "O";\
document.querySelector('[name="ItemName"]').value = "0";
