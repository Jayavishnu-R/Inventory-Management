<%@ page import="com.inventory.model.Item" %>
<%@ page import="com.inventory.dao.ItemDAO" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    ItemDAO itemDAO = new ItemDAO();
    Item item = itemDAO.getItemById(id); // Create this method in your DAO to fetch a single item by its ID
%>
<!DOCTYPE html>
<html>
<head>
    <title>Update Item</title>
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
        .form-container label {
            display: block;
            margin-bottom: 10px;
        }
        .form-container input {
            padding: 10px;
            font-size: 16px;
            width: 100%;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .form-container button[type="submit"] {
            width: 100%;
        }
    </style>
</head>
<body>
<h1>Update Item</h1>
<div class="form-container">
    <form id="updateForm" action="/inventory/api/items/<%= item.getId() %>" method="POST" target="hiddenIframe">
        <label for="name">Name:</label>
        <input type="text" name="name" id="name" value="<%= item.getName() %>" required><br>

        <label for="quantity">Quantity:</label>
        <input type="number" name="quantity" id="quantity" value="<%= item.getQuantity() %>" required><br>

        <label for="price">Price:</label>
        <input type="number" step="0.01" name="price" id="price" value="<%= item.getPrice() %>" required><br>

        <button type="submit" onclick="submitUpdateForm(event, this)">Update Item</button>
    </form>
</div>

<a href="items.jsp" class="actions">Back to Inventory</a>

<!-- Hidden iframe to handle the form submission -->
<iframe name="hiddenIframe" style="display: none;" onload="redirectToItemsPage()"></iframe>

<script>
    function submitUpdateForm(event, button) {
        event.preventDefault(); // Prevent default form submission

        // Collect form data
        const form = button.closest('form');
        const formData = new FormData(form);

        // Convert form data to JSON for the PUT request
        const jsonData = Object.fromEntries(formData.entries());

        // Send an AJAX PUT request
        fetch(form.action, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(jsonData),
        })
            .then((response) => {
                if (response.ok) {
                    alert('Item updated successfully!');
                    // Optionally, reload or redirect
                    location.reload();
                } else {
                    alert('Failed to update item');
                }
            })
            .catch((error) => {
                alert('An error occurred: ' + error.message);
            });
    }

    function redirectToItemsPage() {
        // Redirect to items.jsp after the form is submitted
        window.location.href = "items.jsp";
    }
</script>
</body>
</html>