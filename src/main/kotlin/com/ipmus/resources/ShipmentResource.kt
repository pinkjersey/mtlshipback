package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Shipment
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
@Path("shipments")
class ShipmentResource : GenericResource<Shipment>(Shipment.type, ::Shipment){
    @GET
    @Produces("application/json")
    fun shipments(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getShipment(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun newShipment(entity: Shipment) : String {
        return newEntity(entity)
    }
}