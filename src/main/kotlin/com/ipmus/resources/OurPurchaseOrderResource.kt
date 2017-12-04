package com.ipmus.resources

import com.ipmus.entities.Item
import com.ipmus.entities.OurPurchaseOrder
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("ourPurchaseOrders")
class OurPurchaseOrderResource : GenericResource<OurPurchaseOrder>(OurPurchaseOrder.type, ::OurPurchaseOrder) {
    @GET
    @Produces("application/json")
    fun purchaseOrders() : String{
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun purchaseOrder(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/items")
    @GET
    @Produces("application/json")
    fun POItems(@PathParam("entityID") entityID: String) : String {
        return getChildren<Item>(entityID, "items", ::Item)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun updatePO(entity: OurPurchaseOrder) : String {
        return updateEntity(entity)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newPO(entity: OurPurchaseOrder) : String {
        return newEntity(entity)
    }
}