package com.inventory.api;

import com.inventory.model.Sales;
import com.inventory.util.DatabaseUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

//@Path("invoices/sales")
public class SalesResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSales(
            @QueryParam("invoiceId") int invoiceId,
            List<Sales> products
    ) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String insertQuery = "INSERT INTO sales (invoiceId, itemId, quantity, salePrice) VALUES (?, ?, ?, ?)";

            for (Sales product : products) {
                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setInt(1, invoiceId); // Set invoiceId for FK
                    stmt.setInt(2,product.getId()); // Set itemId (productId from JSON)
                    stmt.setInt(3, product.getQuantity()); // Set quantity sold
                    stmt.setDouble(4, product.getSalePrice()); // Set salePrice
                    stmt.executeUpdate();
                }
            }
            return Response.status(Response.Status.CREATED).entity("Sales added successfully.").build();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding sales: " + e.getMessage())
                    .build();
        }
    }
}
