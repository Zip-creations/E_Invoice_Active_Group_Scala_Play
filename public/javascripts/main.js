document.addEventListener("DOMContentLoaded", function () {
    var calendars = document.getElementsByName("InvoiceIssueDate");
    calendars.forEach(function (calendar) {
        calendar.setAttribute("min", new Date().toISOString().split("T")[0]);
        calendar.setAttribute("max", new Date().toISOString().split("T")[0]);
    });
});
