package com.inventory.api;

import com.inventory.dao.ItemDAO;
import com.inventory.model.Item;
import com.inventory.util.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/items")
public class ItemResource {

    private ItemDAO itemDAO;

    public ItemResource() throws SQLException, ClassNotFoundException {
        itemDAO = new ItemDAO();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        try {
            System.out.println("getting");
            List<Item> items = itemDAO.getAllItems();
            System.out.println(items);
            return Response.ok(items).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addItem(
            @FormParam("name") String name,
            @FormParam("quantity") int quantity,
            @FormParam("price") double price) {
        try {
            Item newItem = new Item();
            newItem.setName(name);
            newItem.setQuantity(quantity);
            newItem.setPrice(price);

            itemDAO.addItem(newItem);
            return Response.status(Response.Status.CREATED).entity("Item added successfully!").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItemPartial(@PathParam("id") int id, Item itemToUpdate) {
        try {
//            itemToUpdate.setId(id); // Ensure the item ID is set from the path
            itemDAO.partialUpdateItem(id,itemToUpdate);
            return Response.ok("Item updated successfully!").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") int id, Item itemToUpdate) {
        try {
            // Set the id of the item to update (from the path parameter)
            itemToUpdate.setId(id);

            boolean isUpdated = itemDAO.updateItem(itemToUpdate);
            if (isUpdated) {
                return Response.ok("Item updated successfully!").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found!").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") int id) {
        try {
            System.out.println("deleting....");
            boolean rowAffected=itemDAO.deleteItem(id);
            if(rowAffected) {
                return Response.ok("Item deleted successfully!").build();
            }
            else{
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Item not found")).build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }



    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("id") int id) {
        try {
            Item item = itemDAO.getItemById(id);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Item not found")
                ).build();
            }
            return Response.ok(item).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}



