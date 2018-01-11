package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Container
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun newContainer(entity: Container) : String {
        return newEntity(entity)
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