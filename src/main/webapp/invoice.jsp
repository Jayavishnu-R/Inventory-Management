
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoices</title>
    <style>
        /* General Styling */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }

        h1 {
            color: #333;
            text-align: center;
        }

        /* Table Styling */
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px auto;
            background-color: white;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            text-align: left;
            padding: 8px;
            cursor: pointer;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        /* Detailed Invoice Container */
        #invoiceDetails {
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            display: none; /* Hidden by default */
            width: 90%;
            max-width: 600px;
            border-radius: 5px;
        }

        #invoiceDetails h2 {
            margin-top: 0;
            color: #333;
            text-align: center;
        }

        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }

        .detail-row:last-child {
            border-bottom: none;
        }

        .sales-table {
            margin-top: 20px;
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 8px 10px;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<h1>Invoices</h1>

<table id="invoiceTable">
    <thead>
    <tr>
        <th>Invoice ID</th>
        <th>Date & Time</th>
        <th>Total Amount</th>
    </tr>
    </thead>
    <tbody>
    <!-- Rows will be dynamically inserted here -->
    </tbody>
</table>

<!-- Invoice Details Section -->
<div id="invoiceDetails">
    <h2>Invoice Details</h2>
    <div id="invoiceInfo"></div>

    <button onclick="closeInvoiceDetails()">Go Back</button>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        fetchInvoices(); // Automatically fetch invoices on page load
    });

    /**
     * Fetches all invoices from the API and populates them in the table
     */
    async function fetchInvoices() {
        try {
            const response = await fetch("http://localhost:8080/inventory/api/invoices/");
            if (!response.ok) throw new Error("Failed to fetch invoices");

            const invoices = await response.json();
            populateInvoiceTable(invoices);
        } catch (error) {
            console.error("Error:", error);
            alert("An error occurred while fetching invoices.");
        }
    }

    /**
     * Populates the invoice table with data and adds a click handler to each row
     */
    function populateInvoiceTable(invoices) {
        const tableBody = document.querySelector("#invoiceTable tbody");
        tableBody.innerHTML = ""; // Clear previous rows

        invoices.forEach(invoice => {
            const row = document.createElement("tr");
            row.addEventListener("click", () => fetchInvoiceDetails(invoice.id)); // Add click event

            // Invoice ID cell
            const idCell = document.createElement("td");
            idCell.textContent = invoice.id;
            row.appendChild(idCell);

            // Invoice Date and Time cell (display as-is)
            const dateCell = document.createElement("td");
            dateCell.textContent = invoice.invoiceDate; // Show raw timestamp
            row.appendChild(dateCell);

            // Total Amount cell (display as-is)
            const totalAmountCell = document.createElement("td");
            totalAmountCell.textContent = invoice.totalAmount !== undefined && invoice.totalAmount !== null
                ? invoice.totalAmount // Display raw totalAmount as-is
                : "N/A"; // Handle missing totalAmount
            row.appendChild(totalAmountCell);

            tableBody.appendChild(row);
        });
    }

    /**
     * Formats a PostgreSQL timestamp into a readable date and time format
     * @param {string} timestamp - The timestamp string from PostgreSQL
     * @returns {string} - Formatted date and time (e.g., "YYYY-MM-DD HH:MM:SS")
     */
    function formatTimestamp(timestamp) {
        const date = new Date(timestamp);

        if (isNaN(date.getTime())) {
            return "Invalid Date"; // Handle invalid input
        }

        const options = {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
        };

        return date.toLocaleString(undefined, options); // Combine date and time
    }

    /**
     * Fetch details of a specific invoice by ID and display it
     */
    async function fetchInvoiceDetails(invoiceId) {
        try {
            const response = await fetch(`http://localhost:8080/inventory/api/invoices/${invoiceId}`);
            if (!response.ok) throw new Error("Failed to fetch invoice details");

            const invoice = await response.json();
            showInvoiceDetails(invoice);
        } catch (error) {
            console.error("Error:", error);
            alert("An error occurred while fetching the invoice details.");
        }
    }

    /**
     * Displays the invoice details, including sales information
     */
    function showInvoiceDetails(invoice) {
        const invoiceInfo = document.getElementById("invoiceInfo");

        // Populate invoice header details
        invoiceInfo.innerHTML = `
            <div class="detail-row"><strong>Invoice ID:</strong> ${invoice.id}</div>
            <div class="detail-row"><strong>Date & Time:</strong> ${invoice.invoiceDate}</div> <!-- Show raw timestamp -->
            <div class="detail-row"><strong>Total Amount:</strong> $${invoice.totalAmount != null && !isNaN(invoice.totalAmount) ? invoice.totalAmount.toFixed(2) : "N/A"}</div>
        `;

        // Show the details container
        document.getElementById("invoiceDetails").style.display = "block";
    }

    /**
     * Closes the invoice details section
     */
    function closeInvoiceDetails() {
        document.getElementById("invoiceDetails").style.display = "none";
    }
</script>
</body>
</html>