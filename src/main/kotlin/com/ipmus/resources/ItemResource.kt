package com.ipmus.resources

/**
 * Created by mozturk on 7/26/2017.
 */


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.Configuration
import com.ipmus.entities.Item
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("items")
class ItemResource {
    val resourceType = Item.type

    @GET
    @Produces("application/json")
    fun getAll(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val items: List<Item> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll(resourceType).map { Item(it) }
        }
        mapper.writeValue(out, items)
        entityStore.close()
        return out.toString()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun newItem(item: Item) : Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            item.save(txn, entityStore)
        }
        entityStore.close()
        return Response.status(200).build()
    }

    @DELETE
    fun deleteAll() : Response {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        entityStore.executeInTransaction { txn ->
            txn.getAll(resourceType).forEach {
                it.delete()
            }
        }
        entityStore.close()
        return Response.status(200).build()
    }
}