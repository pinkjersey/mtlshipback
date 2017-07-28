package com.ipmus

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.entities.Customer
import jetbrains.exodus.entitystore.EntityId
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
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val customer = entityStore.computeInReadonlyTransaction { txn ->
            val customerEntity = txn.getEntity(xodusEntityId)
            if (customerEntity == null) {
                throw NotFoundException()
            }
            Customer(customerEntity)
        }
        mapper.writeValue(out, customer)
        entityStore.close()
        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun newCustomer(customer: Customer) : Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            customer.save(txn)
        }
        entityStore.close()
        return Response.status(200).build()
    }

}