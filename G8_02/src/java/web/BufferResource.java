/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package web;

import model.Item;
import service.BufferService;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import com.google.gson.Gson;

@Path("v1")
public class BufferResource {
    private static int sequenceCounter = 1;
    private final Gson gson = new Gson();

    @POST
    @Path("produce")
    @Consumes("application/json")
    @Produces("application/json")
    public Response produce(String json) {
        Item item = gson.fromJson(json, Item.class);

        if (item.getSequence_no() == 0) {
            item.setSequence_no(sequenceCounter++);
        }

        String status = BufferService.produce(item);
        return Response.ok(gson.toJson(new StatusResponse(status))).build();
    }

    @GET
    @Path("consume/{consumer_name}")
    @Produces("application/json")
    public Response consume(@PathParam("consumer_name") String consumerName) {
        Item item = BufferService.consume();
        if (item == null) {
            return Response.ok(gson.toJson(new StatusResponse("empty"))).build();
        } else {
            ConsumerResponse res = new ConsumerResponse("ok", consumerName, item);
            return Response.ok(gson.toJson(res)).build();
        }
    }

    // 輔助類別
    private static class StatusResponse {
        String status;
        StatusResponse(String status) { this.status = status; }
    }

    private static class ConsumerResponse {
        String status;
        String consumer_name;
        Item item;

        ConsumerResponse(String status, String consumer_name, Item item) {
            this.status = status;
            this.consumer_name = consumer_name;
            this.item = item;
        }
    }
}