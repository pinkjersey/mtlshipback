package com.ipmus.resources

import com.ipmus.entities.Container
import com.ipmus.entities.Item
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


/**
 * Created by mdozturk on 7/27/17.
 */
@Path("containers")
class ContainerResource : GenericResource<Container>(Container.type, ::Container) {
    @GET
    @Produces("application/json")
    fun containers(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getContainer(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/items")
    @GET
    @Produces("application/json")
    fun containerItems(@PathParam("entityID") entityID: String) : String {
        return getChildren<Item>(entityID, "items", ::Item)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun newContainer(entity: Container) : String {
        return newEntity(entity)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateContainer(entity: Container) : String {
        return updateEntity(entity)
    }

    /*
    @DELETE
    fun deleteAll() : Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            txn.getAll(Container.type).forEach {
                it.delete()
            }
        }
        entityStore.close()
        return Response.status(200).build()
    }
    */
}