document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    // Set InvoiceIssueDate to "today"
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    
    var positionID = 1

    // Add LeistungsPosition
    document.getElementById("addLeistungsabrechnungButton").addEventListener("click", function () {
        CreateLeistungsabrechnungsPosition(positionID).then(newPosID=> {
            positionID = newPosID
            LoadRestrictions()
        })
    });

    // Add StundenPosition
    document.getElementById("addStundenabrechnungButton").addEventListener("click", function () {
        CreateStundenabrechnungsPosition(positionID).then(newPosID=> {
            positionID = newPosID
            LoadRestrictions()
        })
    });

    // Remove Position
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("removePositionButton")) {
          const container = event.target.closest(".inputContainer");
            container.remove();
        }
    });

    document.getElementById("invoicecontainer").addEventListener("submit", async function (e) {
        e.preventDefault();
        toggleUserInteraction(true)
        clearErrorDisplays();
        const data = new FormData(e.target);
        await fetch("/generateEInvoice", {
            method: "POST",
            body: data
        })
        .then(response => response.json())
        .then(json => {
            if (json.status === "ok") {
                window.open(`/validationReport?path=${json.data}`, "_blank")
                toggleUserInteraction(false)
            } else {
                Object.entries(json.data).forEach(([key, values]) => {  // key is "source", values are the errormessage(s)
                    const erroneousInput = document.getElementsByName(key)[0]  // Reminder, IDs can't be used when sending a form from the frontend to the backend (and back)
                    var jumpOnce = true
                    values.forEach(errorMessage => {
                        const targetDiv = erroneousInput.parentElement.parentElement.querySelector(".errorDisplay");
                        targetDiv.insertAdjacentHTML("beforeend", errorMessage);
                        // Jump to the header of first div that contains errors
                        if (jumpOnce) {
                            erroneousInput.parentElement.parentElement.parentElement.parentElement.scrollIntoView();
                            jumpOnce = false
                        }  
                    });
                });
                toggleUserInteraction(false)
            }
        })
    });
    LoadRestrictions()
});

function clearErrorDisplays() {
    const allErrorDisplays = Array.from(document.getElementsByClassName("errorDisplay"))
    allErrorDisplays.forEach(display => {
        display.innerHTML = ""  // nuke everything within the div, without the div itself
    });
}

function toggleUserInteraction(disable) {
    var allElements = Array.from(document.getElementById("invoicecontainer").elements)
    var allButtons = allElements.filter(elem => elem.type === "submit" || elem.tagName === "BUTTON")
    if (disable) {
        document.body.style.cursor = "wait"
        allElements.forEach(elem => elem.style.pointerEvents = "none")
    } else {
        document.body.style.cursor = ""
        allElements.forEach(elem => elem.style.pointerEvents = "auto")
    }
    allButtons.forEach(elem => elem.disabled = disable)
}

/**
 *Fetches invoice_item.scala.html, tranfers the current PostionID, attaches the created content
 *to the positionContainer div (which contains one invoice position) and increments the positionID
 * @param {*} positionID consistent, increasing ID for invoice Positions
 * @returns 
 */
async function CreateLeistungsabrechnungsPosition(positionID) {
    await fetch(`/addLeistungsposition?positionID=${positionID.toString()}`)
        .then(response => response.text())
        .then(html => {
            const targetDiv = document.getElementById("positionContainer");
            targetDiv.insertAdjacentHTML("beforeend", html);
        })
    positionID++
    return positionID
}

/**
 *Fetches invoice_time.scala.html, tranfers the current PostionID, attaches the created content
 *to the positionContainer div (which contains one invoice position) and increments the positionID
 * @param {*} positionID consistent, increasing ID for invoice Positions
 * @returns 
 */
async function CreateStundenabrechnungsPosition(positionID) {
    await fetch(`/addStundenposition?positionID=${positionID.toString()}`)
        .then(response => response.text())
        .then(html => {
            const targetDiv = document.getElementById("positionContainer");
            targetDiv.insertAdjacentHTML("beforeend", html);
        })
    positionID++
    return positionID
}

function GetAllDescendants(node){
    var allDescendants = []
    Array.from(node.childNodes).forEach(child => {
        allDescendants.push(child)
        allDescendants = allDescendants.concat(GetAllDescendants(child))
    });
    return allDescendants
}

// window.onbeforeunload = function(){
//     return "";
// };

/**
 * Sets all Listeners and Codelists. Must be called after the document loaded, and each time a new 
 * input is connected to the document!
 */
// .awesomeplete, .dataTypeAmount, .datatypeQuantity and .datatypePercentage must be used mutually exclusive!
function LoadRestrictions() {
    const numericClasses = ["datatypeAmount", "datatypeQuantity", "datatypePercentage"]
    var allRelevantChilds = document.querySelectorAll("input")
    allRelevantChilds.forEach(input => {
        // Prevents that a Listener gets assigned to the same input multiple times
        if (input.dataset.beforeinputBound) {return}
        input.dataset.beforeinputBound = "true"
        if (numericClasses.includes(input.className)) {
            AddNumericRestriction(input)
        } else if (input.className === "awesomplete") {
            AddAwesompleteRestriction(input);
        }
        // compute the net amount again if any of these 3 inputs is changed
        if (/(InvoicedQuantity|ItemNetPrice|InvoicedItemVATRate)\d+/.test(input.name)){
            addLineNetAmountComputation(input)
        }
    })
}

function addLineNetAmountComputation(input) {
    input.addEventListener("input", function(e) {
        function getValue(inputName) {
            return parseFloat(document.getElementsByName(inputName)[0].value)
        }
        var index = input.name.match(/\d+$/)[0]
        var invoiceLineNetAmount = document.getElementsByName("InvoiceLineNetAmount" + index)[0]
        var invoicedQuantity = getValue("InvoicedQuantity" + index) || 1
        var itemNetPrice = getValue("ItemNetPrice" + index) || 1
        var invoicedItemVATRate = getValue("InvoicedItemVATRate" + index) || 0
        invoiceLineNetAmount.value = parseInt((invoicedQuantity * itemNetPrice * (1+ invoicedItemVATRate / 100)) * 100) / 100
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
        if (isNaN(proposed) || proposed.charAt(0) === "." || proposed.includes(" ")) {
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
    // Set awesomplete properties
    input.awesomplete = new Awesomplete(input, {
        minChars: 0,
        maxItems: Infinity,
        autoFirst: true,
        tabSelect: true,
        list: input.data,
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
