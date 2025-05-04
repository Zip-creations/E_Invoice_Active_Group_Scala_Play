document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    // Set InvoiceIssueDate to "today"
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    var positionID = 1
    CreatePosition(positionID).then( a =>
        positionID = a
    )

    // Add Position
    document.getElementById("addPositionButton").addEventListener("click", function () {
        CreatePosition(positionID).then(a=> {
            positionID = a
        })
    });

    // Remove Position
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("removePositionButton")) {
          const container = event.target.closest(".inputContainer");
            container.remove();
        }
    });

    function GetAllDescendants(node){
        var allDescendants = []
        Array.from(node.childNodes).forEach(child => {
            allDescendants.push(child)
            allDescendants = allDescendants.concat(GetAllDescendants(child))
        });
        return allDescendants
    }

    function ToggleOptionalGroup(button) {
        // get the div the button is supposed to control
        var adjacentDiv = button.nextSibling
        while (adjacentDiv.className != "groupContainer") {
            adjacentDiv = adjacentDiv.nextSibling
        }
        // console.log(adjacentDiv)
        // Toggle visibility and (de)activate the inputs (so they can be ignored by the form element accordingly)
        var allInputs = Array.from(adjacentDiv.childNodes).filter(elem => elem.tagName === "DIV" || elem.tagName === "BUTTON")
        var test = []
        allInputs.forEach(elem => {
            test = test.concat(Array.from(GetAllDescendants(elem)).filter(desc => desc.tagName === "INPUT"))
        })
        test.forEach(elem => {
            if (adjacentDiv.style.display === "none") {
                elem.disabled = false
            } else {
                elem.disabled = true
            }
        })
        if (adjacentDiv.style.display === "none") {
            adjacentDiv.style.display = "flex";
            button.innerHTML = button.innerHTML.replace("einblenden", "ausblenden")
        } else {
            adjacentDiv.style.display = "none";
            button.innerHTML = button.innerHTML.replace("ausblenden", "einblenden")
        }
    }
    var allOptButtons = Array.from(document.getElementsByClassName("buttonForOptionalGroups"))
    allOptButtons.forEach(button => {
        button.addEventListener("click", function(){ToggleOptionalGroup(button)})
        // Collapse all optional groups when the document is initially loaded
        ToggleOptionalGroup(button)
    });
    SetHardCodedInputs()
});


/**
 *Fetches invoiceLine.scala.html, tranfers the current PostionID, attaches the created inputContainer
 * (which contains one invoice position) and increments the positionID
 * @param {*} positionID consistent, increasing ID for invoice Positions
 * @returns 
 */
async function CreatePosition(positionID) {
    await fetch(`/invoiceLine?positionID=${positionID.toString()}`)
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
// .awesomeplete, .dataTypeAmount, .datatypeQuantity and .datatypePercentage must be used mutually exclusive!
function LoadRestrictions() {
    const numericClasses = ["datatypeAmount", "datatypeQuantity", "datatypePercentage"]
    var allInputs = document.querySelectorAll("input")
    allInputs.forEach(input => {
        if (numericClasses.includes(input.className)) {
            AddNumericRestriction(input)
        } else if (input.className === "awesomplete")
            AddAwesompleteRestriction(input);
    })
}

function GetProposedNumber(input, e) {
    const {selectionStart, selectionEnd, value} = input;
    // BACKSPACE on one digit
    if (e.inputType === "deleteContentBackward" && (selectionStart === selectionEnd)) {
        return [value.slice(0, Math.max(0, selectionStart -1)) + value.slice(selectionEnd), Math.max(0, selectionStart-1), Math.max(0, selectionEnd-1)]
    }
    // DELETE on one digit
    else if (e.inputType === "deleteContentForward" && (selectionStart === selectionEnd)) {
        return [value.slice(0, selectionStart) + value.slice(selectionStart +1, value.length), selectionStart, selectionEnd]
    }
    // delete selection (crtl + x, DELETE or BACKSPACE on selection)
    else if (e.data === null) {
        return [value.slice(0, selectionStart) + value.slice(selectionEnd, value.length), selectionStart, selectionStart]
    }
    else {
        var inputLen = e.data.length
        return [value.slice(0, selectionStart) + e.data.replace(",", ".") + value.slice(selectionEnd), selectionStart+inputLen, selectionEnd+inputLen]
    }
}

function SendProposedInput(input, e, proposed, selectionStart, selectionEnd) {
    e.preventDefault();
    input.value = proposed
    input.setSelectionRange(selectionStart, selectionEnd)
    const event = new Event("input", { bubbles: true });
    input.dispatchEvent(event)
}

function AddNumericRestriction(input){
    input.addEventListener("beforeinput", (e) => {
        const temp = GetProposedNumber(input, e)
        const proposed = temp[0]
        const mouseStart = temp[1]
        const mouseEnd = temp[2]
        if (isNaN(proposed) || proposed.charAt(0) === ".") {
            e.preventDefault()
            return
        }
        var [hasDot, leftOfDot, rightOfDot] = DeconstruktNumber(proposed)
        switch (input.className) {
            case "datatypeAmount":
                // A valid Amount must be a number and can only have a max of 2 digits at the right of the dot
                if (hasDot && rightOfDot.length > 2) {
                    e.preventDefault();
                } else {
                    SendProposedInput(input, e, proposed, mouseStart, mouseEnd)
                }
                break;
            case "datatypeQuantity":
                // Any valid number is accepted
                SendProposedInput(input, e, proposed, mouseStart, mouseEnd)
                break;
            case "datatypePercentage":
                // // Valid range for a percenatage: 100.00 to 0
                // // A Valid Percentage must be: A valid number & has a max of two digits after the dot & has no more than 3 digits left of the dot &
                // // if there are 3 digits left of the dot, it must be 100
                if (hasDot && rightOfDot.length > 2) {
                    e.preventDefault();
                    return
                } else if (parseInt(proposed) > 100){
                    e.preventDefault();
                    return
                } else {
                    SendProposedInput(input, e, proposed, mouseStart, mouseEnd)
                }
                break;
        }
    })
    // This Listener assumes that the current input is valid according to the "beforeinput"-Listener
    input.addEventListener("focusout", (e) => {
        const proposed = e.target.value
        if (proposed.charAt(proposed.length -1) === ".") {
            SendProposedInput(e.target, e, proposed + "0")
        }
    })
}

function AddAwesompleteRestriction(input) {
    ConnectData(input.dataset.file).then(values => {
        // Set the data the completionchecker uses
        input.data = values
        // Set awesomplete properties
        input.awesomplete = new Awesomplete(input, {
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
            input.awesomplete.evaluate();
        });

        input.addEventListener("focusout", (e) => {
            const proposed = e.target.value
            if (!input.data.includes(proposed)) {
                SendProposedInput(input, e, "")
            }
            input.awesomplete.close()
            // else: Input is valid, do nothing
        })
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
        } else {
            var inputLen = e.data.length
            SendProposedInput(input, e, proposed, selectionStart+inputLen, selectionEnd+inputLen)
        }
    });
}

function DeconstruktNumber(num){
    var hasDot = num.includes(".")
    var leftOfDot
    var rightOfDot
    if (hasDot) {
        leftOfDot = num.slice(0, num.indexOf("."))
        rightOfDot = num.slice(num.indexOf(".")+1, num.length)
    } else {
        leftOfDot = ""
        rightOfDot = ""
    }
    return [hasDot, leftOfDot, rightOfDot]
}
