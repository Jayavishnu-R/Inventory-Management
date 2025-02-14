<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <script>
    async function login() {
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;

      try {
        const response = await fetch('api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ username, password }),
        });

        if (response.status === 200) {
          const result = await response.json();
          alert(result.message);
          window.location.href = "home.jsp"; // Redirect upon successful login
        } else {
          const error = await response.text();
          document.getElementById("error").textContent = error;
        }
      } catch (err) {
        console.error("An error occurred:", err);
        document.getElementById("error").textContent = "A network error occurred";
      }
    }
  </script>
</head>
<body>
<div>
  <h2>Login</h2>
  <input type="text" id="username" placeholder="Enter Username" required>
  <input type="password" id="password" placeholder="Enter Password" required>
  <button onclick="login()">Login</button>
  <p id="error" style="color:red;"></p>
</div>
</body>
</html>