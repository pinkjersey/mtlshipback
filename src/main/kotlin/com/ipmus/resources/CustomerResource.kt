package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Customer
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
class CustomerResource {
    @GET
    @Produces("application/json")
    fun customers(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val customers = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Customer").map { Customer(it) }
        }
        mapper.writeValue(out, customers)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getCustomer(@PathParam("entityID") entityID: String) : String {

        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val customer = readCustomer(entityID)
        mapper.writeValue(out, customer)

        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newCustomer(customer: Customer) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        var newCustomerID: String? = null
        entityStore.executeInTransaction { txn ->
            newCustomerID = customer.save(txn, entityStore)
        }
        entityStore.close()
        val cID = newCustomerID ?: throw NotFoundException()
        val rc = readCustomer(cID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    fun readCustomer(customerID: String) : Customer {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(customerID, entityStore)
        val customer = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val customerEntity = txn.getEntity(xodusEntityId)
                Customer(customerEntity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        entityStore.close()
        return customer
    }
}