document.addEventListener("DOMContentLoaded", function () {
    const testlist = ["0", "test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10"];
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
        new Awesomplete(input, {
          minChars: 1,
          maxItems: 6,
          autoFirst: true,
          tabSelect: true,
          // TODO: Give every awesomplete a unique list, corresponding to the requirements in the XRechnung
          list: testlist,
          // filter: Awesomplete.FILTER_STARTSWITH,
        });
        console.log("here")
    });

    document.querySelectorAll("input.completionchecker").forEach(input => {
        input.addEventListener("beforeinput", (e) => {
            const { selectionStart, selectionEnd, value } = input;
            // Create input the user wants to type in. Slicing is necessary to test in-between inserts,
            // and in case several letters are marked at once with the cursor
            const proposed =
                value.slice(0, selectionStart) +
                e.data +
                value.slice(selectionEnd);
            // TODO: check the individual list of the input, instead of testlist
            const matches = testlist.some(entry => entry.startsWith(proposed));
            // e.data is null if DELETE or BACKSPACE are pressed (which needs to be allowed)
            if (!matches && !(e.data === null)) {
                e.preventDefault();
            }
        });
    });
});


function CreatePosition(positionID) {
    fetch(`/invoiceLine?positionID=${positionID.toString()}`)
        .then(response => response.text())
        .then(html => {
            const targetDiv = document.getElementById("positionContainer");
            targetDiv.insertAdjacentHTML("beforeend", html);
        })
    .catch(error => console.error("Fehler beim Laden der Position:", error));
    positionID++
    return positionID
}
