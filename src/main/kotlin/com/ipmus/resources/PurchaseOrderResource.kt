package com.ipmus.resources

import com.ipmus.entities.PurchaseOrder
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("purchaseOrders")
class PurchaseOrderResource : GenericResource<PurchaseOrder>(PurchaseOrder.type, ::PurchaseOrder) {
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newPO(entity: PurchaseOrder) : String {
        return newEntity(entity)
    }
}