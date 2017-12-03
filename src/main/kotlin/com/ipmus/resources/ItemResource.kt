package com.ipmus.resources

/**
 * Created by mozturk on 7/26/2017.
 */


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Item
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("items")
class ItemResource : GenericResource<Item>(Item.type, ::Item) {
    @GET
    @Produces("application/json")
    fun getItems(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getItem(@PathParam("entityID") entityID: String): String {
        return getSpecific(entityID)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun newItem(entity: Item) : String {
        return newEntity(entity)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateItem(entity: Item) : String {
        return updateEntity(entity)
    }
}