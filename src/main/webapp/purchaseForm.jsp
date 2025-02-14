

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>





<!DOCTYPE html>
<html>
<head>
  <title>Add Purchase</title>
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
    .form-container {
      background-color: white;
      padding: 20px;
      box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
      margin-top: 20px;
      max-width: 400px;
      margin: auto;
    }
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: bold;
    }
    input {
      width: 100%;
      padding: 10px;
      margin-bottom: 15px;
      border: 1px solid #ccc;
      border-radius: 5px;
      font-size: 14px;
    }
    button[type="submit"] {
      width: 100%;
      padding: 10px;
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
      text-align: center;
      margin-top: 20px;
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
<h1>Add a New Purchase</h1>
<div class="form-container">
  <form id="purchaseForm" action="/inventory/api/purchases" method="POST" target="hiddenIframe">
    <!-- Item ID -->
    <label for="itemId">Item ID:</label>
    <input type="number" id="itemId" name="itemId" required min="1" placeholder="Enter Item ID">

    <!-- Quantity -->
    <label for="quantity">Quantity:</label>
    <input type="number" id="quantity" name="quantity" required min="1" placeholder="Enter Quantity">

    <!-- Purchase Price -->
    <label for="purchasePrice">Purchase Price:</label>
    <input type="number" step="0.01" id="purchasePrice" name="purchasePrice" required placeholder="Enter Purchase Price">

    <!-- Submit Button -->
    <button type="submit" onclick="submitPurchaseForm(event, this)">Add Purchase</button>
  </form>
</div>

<div class="actions">
  <a href="items.jsp">Back to inventory</a>
</div>

<!-- Hidden iframe to handle form submission -->
<iframe name="hiddenIframe" style="display: none;"></iframe>

<script>
  /**
   * Submit the form via AJAX using the Fetch API.
   */
  function submitPurchaseForm(event, button) {
    event.preventDefault(); // Prevent default form submission

    // Collect form data
    const form = button.closest('form');
    const formData = new FormData(form);

    // Convert form data to JSON for the POST request
    const jsonData = Object.fromEntries(formData.entries());

    // Send an AJAX POST request
    fetch(form.action, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(jsonData),
    })
            .then((response) => {
              if (response.ok) {
                alert('Purchase added successfully!');
                form.reset(); // Clear the form
              } else {
                response.text().then((text) => alert('Failed to add purchase: ' + text));
              }
            })
            .catch((error) => {
              alert('An error occurred: ' + error.message);
            });
  }
</script>
</body>
</html>