package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Vessel
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
@Path("vessels")
class VesselResource {
    @GET
    @Produces("application/json")
    fun vessels(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val vessels = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Vessel").map { Vessel(it) }
        }
        mapper.writeValue(out, vessels)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getVessels(@PathParam("entityID") entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val vessel = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val vesselEntity = txn.getEntity(xodusEntityId)
                Vessel(vesselEntity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        mapper.writeValue(out, vessel)
        entityStore.close()
        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun newVessel(vessel: Vessel) : Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            vessel.save(txn, entityStore)
        }
        entityStore.close()
        return Response.status(200).build()
    }
}