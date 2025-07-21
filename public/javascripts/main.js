document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    // Set InvoiceIssueDate to "today"
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    
    var positionID = 1

    // Add LeistungsPosition
    document.getElementById("addLeistungsabrechnungButton").addEventListener("click", function () {
        createLeistungsabrechnungsPosition(positionID).then(newPosID=> {
            positionID = newPosID
            loadRestrictions()
        })
    });

    // Add StundenPosition
    document.getElementById("addStundenabrechnungButton").addEventListener("click", function () {
        createStundenabrechnungsPosition(positionID).then(newPosID=> {
            positionID = newPosID
            loadRestrictions()
        })
    });

    // Remove Position
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("removePositionButton")) {
            const container = event.target.closest(".invoicePosition");
            if (confirm("Soll diese Position wirklich entfernt werden? Eingaben werden veroren gehen.")) {
                container.remove();
            }
            loadRestrictions()
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
                var jumpOnce = true
                Object.entries(json.data).forEach(([key, values]) => {  // key is "source", values are the errormessage(s)
                    const erroneousInput = document.getElementsByName(key)[0]  // Reminder, IDs can't be used when sending a form from the frontend to the backend (and back)
                    values.forEach(errorMessage => {
                        const parent  = erroneousInput.closest(".inputfield")
                        parent.querySelector(".errorDisplay").insertAdjacentHTML("beforeend", errorMessage);
                        // Jump to the header of first div that contains errors (not directly to the false input, better visual effect with this configuration)
                        if (jumpOnce) {
                            parent.parentElement.parentElement.scrollIntoView();
                            jumpOnce = false
                        }  
                    });
                });
                toggleUserInteraction(false)
            }
        })
    });
    loadRestrictions()
});

function clearErrorDisplays() {
    const allErrorDisplays = Array.from(document.getElementsByClassName("errorDisplay"))
    allErrorDisplays.forEach(errorDisplayDiv => {
        errorDisplayDiv.innerHTML = ""  // nuke everything within the div, without the div itself
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
    // functionality of buttons is controlled by elem.style.pointerEvents like every other element,
    // but diabeling/enabling them adds an additioanal visual effect
    allButtons.forEach(elem => elem.disabled = disable)
}

/**
 *Fetches invoice_item.scala.html, tranfers the current PostionID, attaches the created content
 *to the positionContainer div (which contains one invoice position) and increments the positionID
 * @param {*} positionID consistent, increasing ID for invoice Positions
 * @returns 
 */
async function createLeistungsabrechnungsPosition(positionID) {
    await fetch(`/addLeistungsposition?positionID=${positionID}`)
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
async function createStundenabrechnungsPosition(positionID) {
    await fetch(`/addStundenposition?positionID=${positionID}`)
        .then(response => response.text())
        .then(html => {
            const targetDiv = document.getElementById("positionContainer");
            targetDiv.insertAdjacentHTML("beforeend", html);
        })
    positionID++
    return positionID
}

async function createVatIDPositionContainer(vatID, positions) {
    var allPositionIDs = positions.map(pos => {
        return pos.querySelector("input[name='positionIDcontainer']").value
    })
    // must be a simple type, routes dont allow Arrays. Note: PositionIds are never allowed to contain a comma!
    const allPosIDsString = allPositionIDs.join(",")
    await fetch(`/addVatIDPositionContainer?vatID=${vatID}&posIDs=${allPosIDsString}`)
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser()
            const parsedHtml = parser.parseFromString(html, "text/html").body.firstElementChild
            const targetDiv = document.getElementById("positionContainer");
            positions.forEach(position => {
                parsedHtml.appendChild(position);
            })
            targetDiv.appendChild(parsedHtml);
        })
    return
}

function removeVatIDContainers() {
    var positionContainer = document.getElementById("positionContainer")
    var allVATIDconatiners = positionContainer.querySelectorAll("div.vatIDPositionContainer")
    allVATIDconatiners.forEach(container => {
        container.remove()
    })
}

function reloadPositionContainers() {
    var allPositions = Array.from(document.getElementsByClassName("invoicePosition"))
    var groupedPositions = new Map()
    allPositions.forEach(position => {
        var posID = position.querySelector("input[name='positionIDcontainer']")
        var vatCategory = position.querySelector("select[name='InvoicedItemVATCategoryCode(" + posID.value + ")']").value
        var vatRate = position.querySelector("input[name='InvoicedItemVATRate(" + posID.value + ")']").value
        // positions where the user has not set a vatRate or vatCategory will not be assigned to a vatIDPositonContainer.
        // since both inputs are required before the form can be submitted, we can assume the user will set them at some point
        if (vatCategory != "" && vatRate != "") {
            var vatID = vatCategory + "," + vatRate
            if (!vatID.includes(".")){vatID = vatID + ".0"}
            if (!groupedPositions.has(vatID)) {
                groupedPositions.set(vatID, [])
            }
            groupedPositions.get(vatID).push(position)
        }
    })
    // TODO: it would be better if containers could be moved around instead of being removed and created anew
    // This would prevent losing the input of the exemption reason
    removeVatIDContainers()
    groupedPositions.forEach((positions, group) => {
        // Sort the positions by their identifier in ascending order
        positions.sort((a, b) => {
        const idA = a.querySelector("input[name='positionIDcontainer']").value;
        const idB = b.querySelector("input[name='positionIDcontainer']").value;
        return idA.localeCompare(idB);
        });
        var vatID = group.split(",")
        createVatIDPositionContainer(vatID, positions)
    })
}

function getAllDescendants(node){
    var allDescendants = []
    Array.from(node.childNodes).forEach(child => {
        allDescendants.push(child)
        allDescendants = allDescendants.concat(getAllDescendants(child))
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
function loadRestrictions() {
    const numericClasses = ["datatypeAmount", "datatypeQuantity", "datatypePercentage"]
    var allRelevantChilds = document.querySelectorAll("input, select")
    allRelevantChilds.forEach(input => {
        // Prevents that a Listener gets assigned to the same input multiple times
        if (input.dataset.beforeinputBound) {return}
        input.dataset.beforeinputBound = "true"
        
        if (numericClasses.includes(input.className)) {
            addNumericRestriction(input)
        } else if (input.className === "awesomplete") {
            addAwesompleteRestriction(input);
        }
        // compute the net amount again if any of these 3 inputs is changed
        if (/(InvoicedQuantity|ItemNetPrice|InvoicedItemVATRate)\(\d\)+/.test(input.name)){
            addLineNetAmountComputation(input)
        }
        if (/(InvoicedItemVATCategoryCode|InvoicedItemVATRate)\(\d+\)/.test(input.name)){
            input.addEventListener("blur", function(e) {
                reloadPositionContainers()
            })
        }
    })
    reloadPositionContainers()
}

function addLineNetAmountComputation(input) {
    input.addEventListener("input", function(e) {
        function getValue(inputName) {
            return parseFloat(document.getElementsByName(inputName)[0].value)
        }
        var index = input.name.match(/\(\d+\)$/)[0]
        var invoiceLineNetAmount = document.getElementsByName("InvoiceLineNetAmount" + index)[0]
        var invoicedQuantity = getValue("InvoicedQuantity" + index) || 1
        var itemNetPrice = getValue("ItemNetPrice" + index) || 1
        var invoicedItemVATRate = getValue("InvoicedItemVATRate" + index) || 0
        invoiceLineNetAmount.value = parseInt((invoicedQuantity * itemNetPrice * (1+ invoicedItemVATRate / 100)) * 100) / 100
    })
}

function getProposedNumber(input, e) {
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

function sendProposedInput(input, e, proposed, selectionStart, selectionEnd) {
    e.preventDefault();
    input.value = proposed
    input.setSelectionRange(selectionStart, selectionEnd)
    const event = new Event("input", { bubbles: true });
    input.dispatchEvent(event)
}

function addNumericRestriction(input){
    input.addEventListener("beforeinput", (e) => {
        const temp = getProposedNumber(input, e)
        const proposed = temp[0]
        const mouseStart = temp[1]
        const mouseEnd = temp[2]
        if (isNaN(proposed) || proposed.charAt(0) === "." || proposed.includes(" ")) {
            e.preventDefault()
            return
        }
        var [hasDot, leftOfDot, rightOfDot] = deconstruktNumber(proposed)
        switch (input.className) {
            case "datatypeAmount":
                // A valid Amount must be a number and can only have a max of 2 digits at the right of the dot
                if (hasDot && rightOfDot.length > 2) {
                    e.preventDefault();
                } else {
                    sendProposedInput(input, e, proposed, mouseStart, mouseEnd)
                }
                break;
            case "datatypeQuantity":
                // Any valid number is accepted
                sendProposedInput(input, e, proposed, mouseStart, mouseEnd)
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
                    sendProposedInput(input, e, proposed, mouseStart, mouseEnd)
                }
                break;
        }
    })
    // This Listener assumes that the current input is valid according to the "beforeinput"-Listener
    input.addEventListener("focusout", (e) => {
        const proposed = e.target.value
        if (proposed.charAt(proposed.length -1) === ".") {
            sendProposedInput(e.target, e, proposed + "0")
        }
    })
}

function addAwesompleteRestriction(input) {
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
            sendProposedInput(input, e, "")
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
            sendProposedInput(input, e, proposed, selectionStart+inputLen, selectionEnd+inputLen)
        }
    });
}

function deconstruktNumber(num){
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
