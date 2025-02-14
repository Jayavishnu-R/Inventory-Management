<%@ page import="java.util.List" %>
<%@ page import="com.inventory.model.Item" %>
<%@ page import="com.inventory.dao.ItemDAO" %>
<%
    ItemDAO itemDAO = new ItemDAO();
    List<Item> items = itemDAO.getAllItems();
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory and Invoice Management</title>
    <script>

        function submitDeleteForm(event, button) {
            event.preventDefault(); // Prevent the default form submission

            // Get the form's action URL
            const form = button.closest('form');
            const deleteUrl = form.action;

            // Perform DELETE request using Fetch API
            fetch(deleteUrl, {
                method: 'DELETE',
            })
                .then((response) => {
                    if (response.ok) {
                        alert('Item deleted successfully!');
                        // Optionally, refresh the page to see the updated inventory
                        location.reload();
                    }
                    else if (response.status === 401) {
                        // Handle 401 Unauthorized error
                        const errorData = response.json(); // Parse the response JSON
                        const errorMessage = errorData.message || 'Unauthorized access.';
                        alert(errorMessage);
                    }
                    else {
                        alert('Failed to delete item');
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    alert('An error occurred while trying to delete the item.');
                });
        }
        function submitInvoice() {
            // Prevent default form submission
            const rows = document.querySelectorAll('#productRows .product-row');
            const invoiceData =
                []


            rows.forEach(row => {
                const productId = row.querySelector('input[name="productId[]"]').value;
                const productName = row.querySelector('input[name="productName[]"]').value;
                const productPrice = row.querySelector('input[name="productPrice[]"]').value;
                const quantity = row.querySelector('input[name="quantity[]"]').value;

                // Add product details to the array
                invoiceData.push({
                    itemId: productId, // Map productId to itemId
                    salePrice: parseFloat(productPrice), // Map productPrice to salePrice
                    quantity: parseInt(quantity) // Keep quantity as is
                });
            });

            // Send the data via AJAX as JSON
            $.ajax({
                url: "/inventory/api/invoices/",
                type: "POST",
                contentType: "application/json", // Set content type as JSON
                data: JSON.stringify(invoiceData), // Convert the data to JSON string
                success: function(response) {

                    if (response.error) {
                        alert('Failed to create invoice.');
                    }
                    else{
                        alert('Invoice created successfully!');
                    }

                    console.log(response);
                },
                error: function() {
                    alert('Failed to create invoice.');
                }
            });
        }
        function addProductRow() {
            const productRow = `
                <div class="product-row">
                    <input type="number" name="productId[]" placeholder="Product ID" onchange="fetchProductDetails(this)" required>
                    <input type="text" name="productName[]" placeholder="Product Name" readonly>
                    <input type="number" name="productPrice[]" placeholder="Price" readonly>
                    <input type="number" name="quantity[]" placeholder="Quantity" required>
                    <button type="button" onclick="removeProductRow(this)">Remove</button>
                </div>`;
            document.getElementById('productRows').insertAdjacentHTML('beforeend', productRow);
        }

        function removeProductRow(button) {
            button.parentElement.remove();
        }

        function fetchProductDetails(input) {
            console.log(input);
            const productId = input.value; // Get the entered product ID
            const row = input.parentElement;
            const ur ="/inventory/api/items/" + productId
            if (productId) {
                $.ajax({
                    url: ur, // Include the ID in the URL
                    type: "GET",
                    success: function(response) {
                        if (response.error) {
                            alert(response.error); // Display an error if the product is not found
                            row.querySelector('input[name="productName[]"]').value = '';
                            row.querySelector('input[name="productPrice[]"]').value = '';
                        } else {
                            row.querySelector('input[name="productName[]"]').value = response.name;
                            row.querySelector('input[name="productPrice[]"]').value = response.price;
                        }
                    },
                    error: function(jqXHR) {
                        if (jqXHR.status === 404 || jqXHR.status === 401) {
                            // Extract custom "message" from the JSON response body
                            const errorMessage = jqXHR.responseJSON.message ;
                            alert(errorMessage); // Show the error message to the user
                            // Clear relevant fields if needed
                            row.querySelector('input[name="productName[]"]').value = '';
                            row.querySelector('input[name="productPrice[]"]').value = '';
                        } else {
                            // Handle other errors
                            alert("An unexpected error occurred.");
                        }
                    }

                });
            }
        }
        function logout() {
            fetch("/inventory/api/auth/logout", { method: 'GET' })
                .then(response => {
                    if (response.ok) {
                        window.location.href = "login.jsp"; // Redirect to login on success
                    } else {
                        alert('Failed to log out.');
                    }
                })
                .catch(error => {
                    console.error('Logout failed:', error);
                    alert('An error occurred while logging out.');
                });
        }
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        .form-container {
            background-color: white;
            padding: 20px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }
        .product-row {
            display: flex;
            gap: 10px;
            margin-bottom: 10px;
        }
        .product-row input {
            padding: 10px;
            font-size: 16px;
            width: 200px;
        }
        .product-row button {
            background-color: red;
            color: white;
            padding: 5px 10px;
            cursor: pointer;
        }
        .product-row button:hover {
            background-color: darkred;
        }
        .form-action {
            margin-top: 20px;
        }
        button[type="submit"] {
            padding: 10px 20px;

            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }
        button[type="submit"]:hover {
            background-color: #0056b3;
        }
        .actions {
            display: flex;
            gap: 20px;
        }
        .actions a {
            text-decoration: none;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
        }

        .actions a:hover {
            background-color: #0056b3;
        }
    </style>

</head>
<body>
    <h1>Inventory Management</h1>
    <button class="logout" onclick="logout()">Logout</button>
    <!-- Inventory Table -->
    <h2>Inventory Items</h2>
    <a href="purchaseForm.jsp">Add a purchase</a>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Actions</th>
        </tr>
        <%
            for (Item item : items) {
        %>
        <tr>
            <td><%= item.getId() %></td>
            <td><%= item.getName() %></td>
            <td><%= item.getQuantity() %></td>
            <td><%= item.getPrice() %></td>
            <td>
                <a href="updateItem.jsp?id=<%= item.getId() %>">Update</a>
                <form id="deleteForm" action="/inventory/api/items/<%= item.getId() %>" method="POST" style="display: inline;">
                    <button type="submit" onclick="submitDeleteForm(event, this)">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>

    <div class="actions">
        <a href="addItem.jsp">Add New Item</a>

    </div>


    <div class="form-container" id="invoiceForm">
        <h2>Create Invoice</h2>
        <form id="invoiceFormAction" action="/inventory/api/invoices/" method="POST">
            <div id="productRows">
                <!-- Product rows will be dynamically added here -->
            </div>

            <button type="button" onclick="addProductRow()">Add Product</button>

            <div class="form-action">
                <button type="button" onclick="submitInvoice()">Create Invoice</button>
            </div>
        </form>

        <a href="invoice.jsp">View Invoices</a>
    </div>
</body>
</html>