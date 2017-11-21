package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Vendor
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
@Path("vendors")
class VendorResource {
    @GET
    @Produces("application/json")
    fun vendors(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val vendors = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Vendor").map { Vendor(it) }
        }
        mapper.writeValue(out, vendors)
        entityStore.close()
        return out.toString()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun getVendor(@PathParam("entityID") entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val broker = readVendor(entityID)
        mapper.writeValue(out, broker)

        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newVendor(vendor: Vendor) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        var newVendorID: String? = null
        entityStore.executeInTransaction { txn ->
            newVendorID = vendor.save(txn, entityStore)
        }
        entityStore.close()
        val cID = newVendorID ?: throw NotFoundException()
        val rc = readVendor(cID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    fun readVendor(entityID: String) : Vendor {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val vendor = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val vendorEntity = txn.getEntity(xodusEntityId)
                Vendor(vendorEntity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        entityStore.close()
        return vendor
    }
}