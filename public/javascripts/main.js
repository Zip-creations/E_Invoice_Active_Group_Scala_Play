document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    let positionID = 1
    positionID = CreatePosition(positionID)

    document.getElementById("addPositionButton").addEventListener("click", function () {
        positionID = CreatePosition(positionID)
    });

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("removePositionButton")) {
          const container = event.target.closest(".inputContainer");
            container.remove();
        }
    });

    document.querySelectorAll("input.awesomplete").forEach(input => {
        ConnectData(input.dataset.file).then(values => {
            // Set the data the completionchecker uses
            input.data = values
            // Set awesomplete properties
            new Awesomplete(input, {
              minChars: 1,
              maxItems: 20,
              autoFirst: true,
              tabSelect: true,
              list: values,
              filter: Awesomplete.FILTER_STARTSWITH,
            });
        })
    });

    document.querySelectorAll("input.completionchecker").forEach(input => {
        input.addEventListener("beforeinput", (e) => {
            const { selectionStart, selectionEnd, value } = input;
            // Create the input the user wants to type in. Slicing is necessary to test in-between inserts,
            // and in case several letters are marked at once with the cursor
            const proposed = value.slice(0, selectionStart) + e.data.toUpperCase() + value.slice(selectionEnd);
            const matches = input.data.some(entry => entry.startsWith(proposed));
            // e.data is null if DELETE or BACKSPACE are pressed
            // (which must be allowed, but only from the end of the text to prevent false inputs)
            // BACKSPACE is non-functional due to this restriction
            if (!matches && !(e.data === null && selectionEnd === value.length)) {
                e.preventDefault();
            }
            // replace the userinput with the uppercase letter. Use data-* in html if some codes allow 
            // lowercase letters, to create two groups that can be handled individually
            else {
                e.preventDefault();
                input.value = proposed
                const event = new Event("input", { bubbles: true });
                input.dispatchEvent(event);
            }
        });
    });
    SetHardCodedInputs()
});


/**
 *Fetches invoiceLine.scala.html, tranfers the current PostionID, attaches the created inputContainer
 * (which contains one invoice position) and increments the positionID
 * @param {*} positionID consistent, increasing ID for invoice Positions
 * @returns 
 */
function CreatePosition(positionID) {
    fetch(`/invoiceLine?positionID=${positionID.toString()}`)
        .then(response => response.text())
        .then(html => {
            const targetDiv = document.getElementById("positionContainer");
            targetDiv.insertAdjacentHTML("beforeend", html);
        })
    positionID++
    return positionID
}


/**
 *  Reads values from a .xsd file and returns them as Array. Assumes the format: <xsd:enumeration value="foo"/>
 *  @param {*} positionID filename for .xsd
 */
// Needs to be async because values is set in the fetch .then part
async function ConnectData(filename) {
    var values = []
    // Will be translated to public/codelists/filename
    filepath = "assets/codelists/" + filename
    await fetch(filepath)
        .then(response => response.text())
        .then(xsdtext => {
            const matches = Array.from(xsdtext.matchAll(/<xsd:enumeration value="(.*?)"/g));
            values = matches.map(match => match[1]);
        })
    return values
}

function SetHardCodedInputs() {
    // See documentation for BT-24
   var SpecificationIdentifier = document.getElementsByName("SpecificationIdentifier")[0]
   SpecificationIdentifier.value = "urn:cen.eu:en16931:2017#compliant#urn:xeinkauf.de:kosit:xrechnung_3.0"
}
