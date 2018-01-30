package com.ipmus.resources

import com.ipmus.entities.Item
import com.ipmus.entities.OurInvoice
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("ourInvoices")
class OurInvoiceResource : GenericResource<OurInvoice>(OurInvoice.type, ::OurInvoice) {
    @GET
    @Produces("application/json")
    fun ourInvoices() : String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun invoice(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/items")
    @GET
    @Produces("application/json")
    fun invoiceItems(@PathParam("entityID") entityID: String) : String {
        return getChildren(entityID, "items", ::Item)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun updatePO(entity: OurInvoice) : String {
        return updateEntity(entity)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newPO(entity: OurInvoice) : String {
        return newEntity(entity)
    }
}