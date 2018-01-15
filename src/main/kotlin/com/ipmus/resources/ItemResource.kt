package com.ipmus.resources

/**
 * Created by mozturk on 7/26/2017.
 */

import com.ipmus.entities.Item
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("items")
class ItemResource : GenericResource<Item>(Item.type, ::Item) {
    @GET
    @Produces("application/json")
    fun getItems(@QueryParam("search") search: String?): String {
        return getAll(search)
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