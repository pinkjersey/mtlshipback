package com.ipmus.resources

import com.ipmus.entities.Customer
import com.ipmus.entities.PurchaseOrder
import com.ipmus.filter.TokenNeeded
import javax.ws.rs.*
import javax.ws.rs.POST
import javax.ws.rs.core.MediaType


/**
 * Created by mdozturk on 7/27/17.
 */
@Path("customers")
class CustomerResource : GenericResource<Customer>(Customer.type, ::Customer)  {
    @GET
    @Produces("application/json")
    @TokenNeeded
    fun customers(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getCustomer(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/purchaseOrders")
    @GET
    @Produces("application/json")
    fun purchaseOrders(@PathParam("entityID") entityID: String) : String {
        return getChildren<PurchaseOrder>(entityID, "purchaseOrders", ::PurchaseOrder)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newCustomer(entity: Customer) : String {
        return newEntity(entity)
    }

}