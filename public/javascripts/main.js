document.addEventListener("DOMContentLoaded", function () {
    var invoiceDateInput = document.getElementsByName("InvoiceIssueDate");
    invoiceDateInput.forEach(function (invoiceDateInput) {
        invoiceDateInput.setAttribute("value", new Date().toISOString().split("T")[0]);
    });

    let positionID = 0
    document.getElementById("addPositionButton").addEventListener("click", function () {
        fetch(`/invoiceLine?positionID=${positionID.toString()}`)
            .then(response => response.text())
            .then(html => {
                const targetDiv = document.getElementById("positionContainer");
                targetDiv.insertAdjacentHTML("beforeend", html);
                positionID ++;
            })
        .catch(err => console.error("Fehler beim Laden der Position:", err));
    });
});
