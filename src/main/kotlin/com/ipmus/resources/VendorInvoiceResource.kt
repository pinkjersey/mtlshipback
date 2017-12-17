package com.ipmus.resources

import com.ipmus.entities.Item
import com.ipmus.entities.VendorInvoice
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("vendorInvoices")
class VendorInvoiceResource : GenericResource<VendorInvoice>(VendorInvoice.type, ::VendorInvoice) {
    @GET
    @Produces("application/json")
    fun vendorInvoices() : String{
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun vendorInvoice(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/items")
    @GET
    @Produces("application/json")
    fun invoiceItems(@PathParam("entityID") entityID: String) : String {
        return getChildren<Item>(entityID, "items", ::Item)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newVendorInvoice(entity: VendorInvoice) : String {
        if (!entity.entityID.isEmpty()) {
            throw IllegalStateException("the entityID of new vendor invoice needs to be blank")
        }
        return newEntity(entity)
    }
}