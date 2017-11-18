package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Broker
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
@Path("brokers")
class BrokerResource {
    @GET
    @Produces("application/json")
    fun brokers(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val brokers = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Broker").map { Broker(it) }
        }
        mapper.writeValue(out, brokers)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getBroker(@PathParam("entityID") entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val broker = readBroker(entityID)
        mapper.writeValue(out, broker)

        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newBroker(broker: Broker) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        var newBrokerID: String? = null
        entityStore.executeInTransaction { txn ->
            newBrokerID = broker.save(txn, entityStore)
        }
        entityStore.close()
        val cID = newBrokerID ?: throw NotFoundException()
        val rc = readBroker(cID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    fun readBroker(entityID: String) : Broker {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val broker = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val brokerEntity = txn.getEntity(xodusEntityId)
                Broker(brokerEntity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        entityStore.close()
        return broker
    }
}