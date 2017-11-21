package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.PurchaseOrder
import jetbrains.exodus.entitystore.EntityRemovedInDatabaseException
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("purchaseOrders")
class PurchaseOrderResource {
    private val resourceType = PurchaseOrder.type

    @GET
    @Produces("application/json")
    fun getAll(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val items: List<PurchaseOrder> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll(resourceType).map { PurchaseOrder(it) }
        }
        mapper.writeValue(out, items)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getVendor(@PathParam("entityID") entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val broker = readPurchaseOrder(entityID)
        mapper.writeValue(out, broker)

        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newVendor(po: PurchaseOrder) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        var newPOID: String? = null
        entityStore.executeInTransaction { txn ->
            newPOID = po.save(txn, entityStore)
        }
        entityStore.close()
        val cID = newPOID ?: throw NotFoundException()
        val rc = readPurchaseOrder(cID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    private fun readPurchaseOrder(entityID: String) : PurchaseOrder {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val purchaseOrder = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val entity = txn.getEntity(xodusEntityId)
                PurchaseOrder(entity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        entityStore.close()
        return purchaseOrder
    }
}