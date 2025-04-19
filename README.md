# E_Invoice_Active_Group_Scala_Play

Dieses Projekt verwendet den Validator den [OpenXRechnungToolbox](https://github.com/jcthiele/OpenXRechnungToolbox) "as is". Lizenz der Toolbox: [GPLv3](Toolbox/license.txt)

Benutzung des E-Rechung Tools:

`sbt run`

im Hauptverzeichnis ausführen,

Tool ist dann unter `localhost:9000` ansprechbar.

Die E-Rechnung im XML-Format sowie der dazugehörige Validierungs-report im HTML werden mit der "InvoiceNumber" (also der ID der Rechung) im Namen abgespeichert.\
Speicherorte:\
XML (Rechnung): `output/xml/eInvoice_"InvoiceNumber".xml` [Directory](output/xml/)\
PDF (Rechnung): `output/pdf/eInvoice_"InvoiceNumber".pdf`[Directory](output/pdf/)\
HTML (Report): `app/views/validation_reports/eInvoice_"InvoiceNumber"_validation.html` [Directory](app/views/validation_reports/)

Ein Minumum Working Example, welches alle Tests des OpenToolBox-Validators besteht, kann mit diesen Eingaben erzeugt werden, wenn sie über die Konsole des Browsers eingefügt werden:

document.querySelector('[name="InvoiceNumber"]').value = "lalatest123";\
document.querySelector('[name="InvoiceTypeCode"]').value = "380";\
document.querySelector('[name="InvoiceCurrencyCode"]').value = "EUR";\
document.querySelector('[name="BuyerReference"]').value = "0";\
document.querySelector('[name="BusinessProcessType"]').value = "0";\
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
document.querySelector('[name="InvoiceLineIdentifier1"]').value = "0";\
document.querySelector('[name="InvoicedQuantity1"]').value = "0";\
document.querySelector('[name="InvoicedQuantityUnitOfMeasureCode1"]').value = "H87";\
document.querySelector('[name="InvoiceLineNetAmount1"]').value = "0";\
document.querySelector('[name="ItemNetPrice1"]').value = "0";\
document.querySelector('[name="InvoicedItemVATCategoryCode1"]').value = "O";\
document.querySelector('[name="ItemName1"]').value = "0";
