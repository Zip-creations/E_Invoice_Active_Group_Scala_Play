# Tool zur Erstellung von E-Rechnungen (X-Rechung)

Dieses Projekt entstand im Rahmen eines Bachelorprojektes an der [Universität Freiburg](https://uni-freiburg.de/) in Kooperation mit [Active Group](https://www.active-group.de/)

Anforderungen: 
- Java ver. 21.0.7
- Scala ver. 3.7.1
- sbt ver. 1.10.9
- Play Framework ver. 3.0.7

Genutzte Bibliotheken:
- Scala.js ver. 1.19.0
- Cats ver. 2.13.0
- MUnit ver. 1.1.1

Dieses Projekt ist unter [BSD 3-Clause](LICENSE.txt) lizensiert.

Dieses Projekt verwendet den Validator den [OpenXRechnungToolbox](https://github.com/jcthiele/OpenXRechnungToolbox) "as is". Lizenz der Toolbox: [GPLv3](Toolbox/license.txt)

Das Tool wurde unter Windows 11 und Unbuntu 24.04.2 getestet.

Benutzung des E-Rechung Tools:

`sbt run`

im Hauptverzeichnis ausführen,

Tool ist dann unter `localhost:9000` ansprechbar.

Die Tets für die Datentypen können mit `sbt test` ausgeführt werden.

Die E-Rechnung im XML-Format sowie der dazugehörige Validierungs-report im HTML werden mit der "InvoiceNumber" (also der ID der Rechung) im Namen abgespeichert.\
Speicherorte:\
XML (Rechnung): `output/xml/eInvoice_"InvoiceNumber".xml` [Directory](output/xml/)\
PDF (Rechnung): `output/pdf/eInvoice_"InvoiceNumber".pdf`[Directory](output/pdf/)\
HTML (Report): `app/views/validation_reports/eInvoice_"InvoiceNumber"_validation.html` [Directory](app/views/validation_reports/)

Eine Demonstration des Tools kann mit diesen Eingaben erzeugt werden, wenn sie über die Konsole des Browsers eingefügt werden, nachdem eine Stundenabrechnung hinzugefügt wurde:\
(Hinweis: Aktuell muss noch einmal in den Umsatzsteuersatz oder die Steuerkategorie einer Position hinein- und wieder hinausgeklickt werden ("unfocus" ist entscheidend), um die Gruppierung der Positionen zu aktualisieren)

document.querySelector('[name="InvoiceNumber"]').value = "lalatest123";\
document.querySelector('[name="SellerName"]').value = "seller name";\
document.querySelector('[name="SellerAddressLine1"]').value = "beispielstraße 1";\
document.querySelector('[name="SellerPostCode"]').value = "11223";\
document.querySelector('[name="SellerCity"]').value = "Example city";\
document.querySelector('[name="SellerCountryCode"]').value = "DE";\
document.querySelector('[name="Placeholder1"]').value = "plh1";\
document.querySelector('[name="Placeholder2"]').value = "https://github.com/";
document.querySelector('[name="SellerElectronicAddress"]').value = "ex@mail.com";\
document.querySelector('[name="SellerVATIdentifier"]').value = "DE123456789";\
document.querySelector('[name="SellerContactPoint"]').value = "vorname nachname";\
document.querySelector('[name="SellerContactTelephoneNumber"]').value = "012 345";\
document.querySelector('[name="SellerContactEmailAddress"]').value = "ex2@mail.com";\
document.querySelector('[name="BuyerReference"]').value = "ref 123";\
document.querySelector('[name="BuyerName"]').value = "buyer name";\
document.querySelector('[name="BuyerElectronicAddress"]').value = "ex3@mail.com";\
document.querySelector('[name="BuyerCity"]').value = "buyer city";\
document.querySelector('[name="BuyerPostCode"]').value = "54321";\
document.querySelector('[name="BuyerCountryCode"]').value = "DE";\
document.querySelector('[name="Placeholder3"]').value = "DE12 3456 7890";\
document.querySelector('[name="InvoiceCurrencyCode"]').value = "EUR";\
document.querySelector('[name="PaymentMeansTypeCode"]').value = "ZZZ";\
document.querySelector('[name="PaymentTerms"]').value = "yes";

document.querySelector('[name="ItemName(1)"]').value = "item name 1";\
document.querySelector('[name="InvoicedQuantity(1)"]').value = "11";\
document.querySelector('[name="ItemNetPrice(1)"]').value = "10";\
document.querySelector('[name="InvoicedItemVATCategoryCode(1)"]').value = "S";\
document.querySelector('[name="InvoicedItemVATRate(1)"]').value = "19.0";
