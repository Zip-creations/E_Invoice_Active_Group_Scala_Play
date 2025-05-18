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
document.querySelector('[name="placeholder1"]').value = "plh1";\
document.querySelector('[name="placeholder2"]').value = "plh2";\
document.querySelector('[name="placeholder3"]').value = "plh3";\
document.querySelector('[name="SellerElectronicAddress"]').value = "ex@mail.com";\
document.querySelector('[name="SellerContactPoint"]').value = "vorname nachname";\
document.querySelector('[name="SellerContactTelephoneNumber"]').value = "012 345";\
document.querySelector('[name="SellerContactEmailAddress"]').value = "ex2@mail.com";
