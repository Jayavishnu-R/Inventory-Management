package com.inventory.api;

import com.inventory.model.UserCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class LoginResource {

    // Dummy users for simplicity. Replace with a database/userstore in production.
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("admin", "password"); // username: admin, password: password
        USERS.put("user", "12345");    // username: user, password: 12345
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials, @Context HttpServletRequest request) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        // Validate user
        if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
            // Create HTTP session
            HttpSession session = request.getSession(true); // Create a new session if one doesn't exist
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(30 * 60); // Set session timeout: 30 minutes

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", username);

            return Response.ok(response).build();
        } else {
            // Unauthorized response
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
        }
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Fetch the current session (don't create a new one)
        if (session != null) {
            session.invalidate(); // Destroy the session
        }
        return Response.ok("Logged out successfully").build();
    }

    @GET
    @Path("/current-user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response currentUser(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("username", (String) session.getAttribute("username"));
            return Response.ok(userInfo).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Not logged in").build();
    }
}