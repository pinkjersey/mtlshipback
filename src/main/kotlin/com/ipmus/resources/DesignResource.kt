package com.ipmus.resources


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Design
import com.ipmus.entities.DesignColor
import jetbrains.exodus.entitystore.EntityRemovedInDatabaseException
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.POST
import javax.ws.rs.core.MediaType


/**
 * Created by mdozturk on 7/27/17.
 */
@Path("designs")
class DesignResource : GenericResource<Design>(Design.type, ::Design) {
    @GET
    @Produces("application/json")
    fun getDesigns() : String{
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun design(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/colors")
    @GET
    @Produces("application/json")
    fun designColors(@PathParam("entityID") entityID: String) : String {
        return getChildren<DesignColor>(entityID, "colors", ::DesignColor)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newDesign(entity: Design) : String {
        return newEntity(entity)
    }
}
