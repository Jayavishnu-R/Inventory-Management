<!DOCTYPE html>
<html>
<head>
    <title>Add Item</title>
    <style>
        /* General Body Styling */
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }

        /* Page Title */
        h1 {
            text-align: center;
            color: #333;
            font-size: 24px;
        }

        /* Form Container */
        .form-container {
            width: 400px;
            margin: 40px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* Form Labels */
        label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
        }

        /* Input Fields */
        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        input:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
        }

        /* Buttons */
        button {
            width: 100%;
            padding: 10px 15px;
            background-color: #007bff;
            color: #ffffff;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        /* Back to Inventory Link */
        .back-link {
            display: block;
            text-align: center;
            margin: 20px auto;
            text-decoration: none;
            background-color: #007bff;
            color: #fff;
            padding: 10px 15px;
            border-radius: 5px;
            transition: background-color 0.3s;
            width: 150px;
        }

        .back-link:hover {
            background-color: #0056b3;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .form-container {
                width: 90%;
                margin: 20px auto;
                padding: 15px;
            }
            button {
                font-size: 14px;
            }
        }
    </style>
</head>
<body>
<h1>Add New Item</h1>
<div class="form-container">
    <form action="/inventory/api/items" method="POST" target="hiddenIframe">
        <!-- Name Input -->
        <label for="name">Name:</label>
        <input type="text" name="name" id="name" required placeholder="Enter item name">

        <!-- Quantity Input -->
        <label for="quantity">Quantity:</label>
        <input type="number" name="quantity" id="quantity" required placeholder="Enter item quantity">

        <!-- Price Input -->
        <label for="price">Price:</label>
        <input type="number" step="0.01" name="price" id="price" required placeholder="Enter item price">

        <!-- Submit Button -->
        <button type="submit">Add Item</button>
    </form>
</div>

<!-- Back to Inventory Link -->
<a class="back-link" href="items.jsp">Back to Inventory</a>

<!-- Hidden iframe to handle form submission -->
<iframe name="hiddenIframe" style="display: none;" onload="redirectToItemsPage()"></iframe>

<script>
    function redirectToItemsPage() {
        // Redirect to items.jsp after the form is submitted
        window.location.href = "items.jsp";
    }
</script>
</body>
</html>