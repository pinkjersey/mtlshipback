package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Customer
import com.ipmus.entities.PurchaseOrder
import com.ipmus.resources.GenericResource.Companion.entityStore
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
@Path("customers")
class CustomerResource : GenericResource<Customer>(Customer.type, ::Customer)  {
    @GET
    @Produces("application/json")
    fun customers(): String {
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getCustomer(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @Path("/{entityID}/purchaseOrders")
    @GET
    @Produces("application/json")
    fun designColors(@PathParam("entityID") entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        // we need to read the design entity from xodus, get the purchaseOrders entities
        // and convert these to the local entity type
        val entityStore = entityStore
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val colors = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val entity = txn.getEntity(xodusEntityId)
                entity.getLinks("purchaseOrders").map {
                    PurchaseOrder(it)
                }
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        mapper.writeValue(out, colors)
        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newCustomer(entity: Customer) : String {
        return newEntity(entity)
    }

}