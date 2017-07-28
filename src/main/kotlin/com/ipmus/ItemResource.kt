package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.entities.Item
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("item")
class ItemResource {
    @Path("items")
    @GET
    @Produces("application/json")
    fun hello(): String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val items: List<Item> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Item").map { Item(it) }
        }
        mapper.writeValue(out, items)
        entityStore.close()
        return out.toString()
    }
}