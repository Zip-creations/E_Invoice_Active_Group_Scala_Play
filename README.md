# E_Invoice_Active_Group_Scala_Play

Dieses Projekt entsteht im Rahmen eines Bachelorprojektes in Kooperation mit [Active Group](https://www.active-group.de/)

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

Eine Demonstration des Tools kann mit diesen Eingaben erzeugt werden, wenn sie über die Konsole des Browsers eingefügt werden:

document.querySelector('[name="InvoiceNumber"]').value = "lalatest123";\
document.querySelector('[name="SellerName"]').value = "seller name";\
document.querySelector('[name="SellerAddressLine1"]').value = "beispielstraße 1";\
document.querySelector('[name="SellerPostCode"]').value = "11223";\
document.querySelector('[name="SellerCity"]').value = "Example city";\
document.querySelector('[name="SellerCountryCode"]').value = "DE";\
document.querySelector('[name="TODO1"]').value = "plh1";\
document.querySelector('[name="TODO3"]').value = "plh3";\
document.querySelector('[name="BuyerReference"]').value = "ref 123";\
document.querySelector('[name="BuyerName"]').value = "buyer name";\
document.querySelector('[name="BuyerPostCode"]').value = "54321";\
document.querySelector('[name="BuyerCity"]').value = "buyer city";\
document.querySelector('[name="BuyerCountryCode"]').value = "DE";\
document.querySelector('[name="BuyerElectronicAddress"]').value = "ex3@mail.com";\
document.querySelector('[name="TODO4"]').value = "DE12 3456 7890";\
document.querySelector('[name="InvoiceCurrencyCode"]').value = "EUR";\
document.querySelector('[name="PaymentMeansTypeCode"]').value = "ZZZ";\
document.querySelector('[name="SellerElectronicAddress"]').value = "ex@mail.com";\
document.querySelector('[name="SellerContactPoint"]').value = "vorname nachname";\
document.querySelector('[name="SellerContactTelephoneNumber"]').value = "012 345";\
document.querySelector('[name="SellerContactEmailAddress"]').value = "ex2@mail.com";
