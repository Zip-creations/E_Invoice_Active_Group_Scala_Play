document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    let positionID = 1
    positionID = CreatePosition(positionID)

    // Add Position
    document.getElementById("addPositionButton").addEventListener("click", function () {
        positionID = CreatePosition(positionID)
    });

    // Remove Position
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("removePositionButton")) {
          const container = event.target.closest(".inputContainer");
            container.remove();
        }
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
    LoadRestrictions()
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

/**
 * Some values can be hardcoded, to reduce the workload for the user and prevent errors.
 * A short explanation is provided as comment for each value.
 */
function SetHardCodedInputs() {
    // See documentation for BT-24. This value is independant from user input and only needs to change
    // if this tool complies to another Standart than CIUS XRechnung in the future
   var SpecificationIdentifier = document.getElementsByName("SpecificationIdentifier")[0]
   SpecificationIdentifier.value = "urn:cen.eu:en16931:2017#compliant#urn:xeinkauf.de:kosit:xrechnung_3.0"
}

// window.onbeforeunload = function(){
//     return "";
// };

/**
 * Sets all Listeners and Codelists. Must be called at least once after the document loaded, and each time a new 
 * input is connected to the document!
 */
function LoadRestrictions() {
    var allInputs = document.querySelectorAll("input")
    allInputs.forEach(input => {
        switch(input.className) {
            case "awesomplete":
                AddAwesompleteRestriction(input);
                break;
            case "datatypeAmount":
                AddAmountRestriction(input);
                break;
            case "datatypeQuantity":
                AddQuantityRestriction(input);
                break;
        }
    })
}

function GetProposedNumber(input, e) {
    const {selectionStart, selectionEnd, value} = input;
    const proposed = value.slice(0, selectionStart) + e.data.replace(",", ".") + value.slice(selectionEnd);
    return proposed
}

function SendProposedInput(input, e, proposed) {
    e.preventDefault();
    input.value = proposed
    const event = new Event("input", { bubbles: true });
    input.dispatchEvent(event);
}

function AddAwesompleteRestriction(input) {
    ConnectData(input.dataset.file).then(values => {
        // Set the data the completionchecker uses
        input.data = values
        // Set awesomplete properties
        const autocompleteElement = new Awesomplete(input, {
            minChars: 0,
            maxItems: Infinity,
            autoFirst: true,
            tabSelect: true,
            list: values,
            filter: Awesomplete.FILTER_STARTSWITH,
        });
        // Allows the autocomplete-list to appear when the user clicks into the input field,
        // even if no input has been given yet
        input.addEventListener("focus", function () {
            autocompleteElement.evaluate();
        });
    })
    input.addEventListener("beforeinput", (e) => {
        const {selectionStart, selectionEnd, value} = input;
        // e.data is null if DELETE or BACKSPACE are pressed
        // (which must be allowed, but only from the end of the text to prevent false inputs)
        // BACKSPACE is non-functional due to this restriction
        if (e.data === null && selectionEnd != value.length) {
            e.preventDefault()
            return
        } else if (e.data === null) {
            return
        }
        // Create the input the user wants to type in and Replace the userinput with the uppercase letter.
        // Slicing is necessary to test in-between inserts, and in case several letters are marked at once with the cursor.
        const proposed = value.slice(0, selectionStart) + e.data.toUpperCase() + value.slice(selectionEnd);
        const matches = input.data.some(entry => entry.startsWith(proposed));
        if (!matches) {
            e.preventDefault();
        }
        else {
            SendProposedInput(input, e, proposed)
        }
    });
}

function AddAmountRestriction(input) {
    input.addEventListener("beforeinput", (e) => {
        if (e.data === null) {return}
        const proposed = GetProposedNumber(input, e)
        if (!CheckIfValidNumber(input, e) || (proposed.includes(".") && proposed.slice(proposed.indexOf(".")+1, proposed.length).length > 2)) {
            e.preventDefault();
        }
        else {
            SendProposedInput(input, e, proposed)
        }
    })
}

function AddQuantityRestriction(input) {
    input.addEventListener("beforeinput", (e) => {
        if (e.data === null) {return}
        const proposed = GetProposedNumber(input, e)
        if (!CheckIfValidNumber(input, e)) {
            e.preventDefault()
        }
        else {
            SendProposedInput(input, e, proposed)
        }
    })
}

// TODO: prevent . at end of number (Replace with .0 ?)
function CheckIfValidNumber(input, e) {
    // Prevent error message, don't change any behavior
    if (e.data === null) {return}
    // Similar to document.querySelectorAll("input.completionchecker")
    const proposed = GetProposedNumber(input, e)
    var flag = false
    var index = 0
    for (const literal of proposed) {
        // A maximum of one . is allowed
        if (literal === ".") {
            // . is not allowed at the beginning of the number
            if (flag || index === 0) { 
                e.preventDefault(); 
                return false
            }
            else {flag = true}
        }
        else if (isNaN(literal) && literal != ".") {
            e.preventDefault();
            return false
        }
        index++
    }
    return true
}
