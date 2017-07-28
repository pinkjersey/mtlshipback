package com.ipmus.resources


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Design
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
@Path("designs")
class DesignResource {
    @GET
    @Produces("application/json")
    fun designs(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val designs = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll(Design.type).map { Design(it) }
        }
        mapper.writeValue(out, designs)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getDesign(@PathParam("entityID") entityID: String): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val design = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val designEntity = txn.getEntity(xodusEntityId)
                Design(designEntity)
            } catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        mapper.writeValue(out, design)
        entityStore.close()
        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun newDesign(design: Design): Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            design.save(txn, entityStore)
        }
        entityStore.close()
        return Response.status(200).build()
    }
}
