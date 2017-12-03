package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.OurPurchaseOrder
import com.ipmus.entities.Vendor
import com.ipmus.entities.Item
import jetbrains.exodus.entitystore.EntityRemovedInDatabaseException
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.POST
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


/**
 * Created by mdozturk on 7/27/17.
 */
@Path("vendors")
class VendorResource : GenericResource<Vendor>(Vendor.type, ::Vendor) {
    @GET
    @Produces("application/json")
    fun vendors(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getVendor(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/purchaseOrders")
    @GET
    @Produces("application/json")
    fun getVendorPurchaseOrders(@PathParam("entityID") entityID: String) : String {
        return getChildren<OurPurchaseOrder>(entityID, "ourPurchaseOrders", ::OurPurchaseOrder)
    }

    @Path("/{entityID}/unassignedItems")
    @GET
    @Produces("application/json")
    fun getVendorUnassignedItems(@PathParam("entityID") entityID: String) : String {
        return getChildren<Item>(entityID, "unassignedItems", ::Item)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newVendor(entity: Vendor) : String {
        return newEntity(entity)
    }
}