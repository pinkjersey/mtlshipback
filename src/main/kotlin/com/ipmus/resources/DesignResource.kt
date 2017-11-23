package com.ipmus.resources


import com.ipmus.entities.Design
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newDesign(entity: com.ipmus.entities.Entity) : String {
        return newEntity(entity)
    }
}
