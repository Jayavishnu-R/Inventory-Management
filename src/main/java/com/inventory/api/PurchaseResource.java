package com.inventory.api;

import com.inventory.dao.PurchaseDAO;
import com.inventory.model.Purchase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/purchases") // Base path for PurchaseResource
public class PurchaseResource {

    private final PurchaseDAO purchaseDAO;

    public PurchaseResource() {
        this.purchaseDAO = new PurchaseDAO(); // Initialize PurchaseDAO
    }

    // Create a new purchase
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPurchase(Purchase purchase) {
        try {
            purchaseDAO.addPurchase(purchase);
            return Response.status(Response.Status.CREATED).entity("Purchase added successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding purchase: " + e.getMessage())
                    .build();
        }
    }

    // Retrieve a list of all purchases
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPurchases() {
        try {
            List<Purchase> purchases = purchaseDAO.getAllPurchases();
            System.out.println(purchases);
            return Response.ok(purchases).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while fetching purchases: " + e.getMessage())
                    .build();
        }
    }

    // Retrieve a specific purchase by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchaseById(@PathParam("id") int id) {
        try {
            Purchase purchase = purchaseDAO.getPurchaseById(id);
            if (purchase != null) {
                return Response.ok(purchase).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase with ID " + id + " not found.")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while fetching purchase: " + e.getMessage())
                    .build();
        }
    }

    // Update an existing purchase
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePurchase(@PathParam("id") int id, Purchase purchase) {
        try {
            purchase.setId(id); // Ensure the ID is set for the update
            purchaseDAO.updatePurchase(purchase);
            return Response.ok("Purchase updated successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while updating purchase: " + e.getMessage())
                    .build();
        }
    }

    // Delete a purchase by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePurchase(@PathParam("id") int id) {
        try {
            purchaseDAO.deletePurchase(id);
            return Response.ok("Purchase with ID " + id + " deleted successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while deleting purchase: " + e.getMessage())
                    .build();
        }
    }
}