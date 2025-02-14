package com.inventory.api;
import com.inventory.dao.InvoiceDAO;
import com.inventory.dao.SalesDAO;
import com.inventory.model.Invoice;
import com.inventory.util.DatabaseUtil;
import com.inventory.model.Sales;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import java.sql.*;
import java.util.List;

@Path("/invoices")
public class InvoiceResource {


    private static InvoiceDAO invoiceDAO=new InvoiceDAO();;
    private static SalesDAO salesDAO=new SalesDAO();;
    public InvoiceResource() {
        System.out.println("InvoiceResource initialized");


    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInvoices() {
        try {
            // Fetch all invoices with limited fields
            System.out.println("getting invoicess");
            List<Invoice> invoices = invoiceDAO.getAllInvoices();
            System.out.println(invoices);
            return Response.ok(invoices).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching invoices: " + e.getMessage())
                    .build();
        }
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvoiceById(@PathParam("id") int id) {
        try {
            // Fetch the invoice
            Invoice invoice = invoiceDAO.getInvoiceById(id);
            if (invoice == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Invoice with ID " + id + " not found.")
                        .build();
            }

            // Fetch the sales associated with the invoice
            List<Sales> sales = salesDAO.getSalesByInvoiceId(id);
            invoice.setSales(sales);

            return Response.ok(invoice).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching invoice: " + e.getMessage())
                    .build();
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInvoice(List<Sales> products) {
        // Validate incoming data
        System.out.println(products);
        if (products == null || products.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No products provided.")
                    .build();
        }
        for (Sales product : products) {
            if (product.getSalePrice() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Sale price for item ID " + product.getItemId() + " must be greater than zero.")
                        .build();
            }
            if (product.getQuantity() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Quantity for item ID " + product.getItemId() + " must be greater than zero.")
                        .build();
            }
        }

        // Database transaction handling
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Step 1: Insert into invoice table
            double totalAmount = products.stream()
                    .mapToDouble(p -> p.getSalePrice() * p.getQuantity())
                    .sum();
            System.out.println(totalAmount);

            String insertInvoiceQuery = "INSERT INTO invoice (total_amount) VALUES (?) RETURNING id";
            int invoiceId;
            try (PreparedStatement invoiceStmt = conn.prepareStatement(insertInvoiceQuery)) {
                invoiceStmt.setDouble(1, totalAmount);
                ResultSet rs = invoiceStmt.executeQuery();
                if (!rs.next()) {
                    conn.rollback(); // Rollback the transaction if invoice creation fails
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Failed to create invoice.")
                            .build();
                }
                invoiceId = rs.getInt(1); // Retrieve the generated invoice ID
                System.out.println(invoiceId);
            }

            // Step 2: Insert into sales table
            String insertSalesQuery = "INSERT INTO sales (invoice_id, item_id, quantity, sale_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement salesStmt = conn.prepareStatement(insertSalesQuery)) {
                for (Sales product : products) {
                    System.out.println(product);

                    String query = "SELECT 1 FROM items WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, product.getItemId());
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()){
                                salesStmt.setInt(1, invoiceId); // Foreign key to invoice
                                salesStmt.setInt(2, product.getItemId()); // Item ID
                                salesStmt.setInt(3, product.getQuantity()); // Quantity sold
                                salesStmt.setDouble(4, product.getSalePrice()); // Sale price
                                salesStmt.addBatch();
                            }
                            else{
                                return Response.status(Response.Status.BAD_REQUEST)
                                        .entity("Item with ID " + product.getItemId() + " not found.")
                                        .build();
                            }
                        }
                    }


                }
                // Execute batch
                try {
                    salesStmt.executeBatch();
                } catch (BatchUpdateException e) {
                    SQLException nextException = e.getNextException();
                    if (nextException != null && nextException.getMessage().contains("Insufficient stock")) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Out of stock for one or more items.")
                                .build();
                    } else {
                        throw e; // Re-throw for unexpected exceptions
                    }
                }

            }

            // Commit the transaction
            conn.commit();

            // Respond with the created invoice ID and total amount
            return Response.status(Response.Status.CREATED)
                    .entity("Invoice created successfully with ID: " + invoiceId)
                    .build();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while creating invoice: " + e.getMessage())
                    .build();
        }
    }

}