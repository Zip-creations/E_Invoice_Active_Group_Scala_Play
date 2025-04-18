document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });
    let positionID = 0
    positionID = CreatePosition(positionID)

    document.getElementById("addPositionButton").addEventListener("click", function () {
        positionID = CreatePosition(positionID)
    });
});

document.addEventListener("click", function (event) {
    if (event.target.classList.contains("removePositionButton")) {
      const container = event.target.closest(".inputContainer");
        container.remove();
    }
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
