package com.ipmus.resources

import com.ipmus.Configuration
import jetbrains.exodus.entitystore.EntityRemovedInDatabaseException
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStores
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jetbrains.exodus.entitystore.Entity


open class GenericResource<T>(private val resourceType: String, val factory : (Entity) -> T) {

    public fun getAll() : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val items: List<T> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll(resourceType).map { factory(it) }
        }
        mapper.writeValue(out, items)
        entityStore.close()
        return out.toString()
    }


    fun getSpecific(entityID: String) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entity = readXodusEntityAndConvert(entityID)
        mapper.writeValue(out, entity)

        return out.toString()
    }

    fun newEntity(entity: com.ipmus.entities.Entity) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        var entityID: String? = null
        entityStore.executeInTransaction { txn ->
            entityID = entity.save(txn, entityStore)
        }
        entityStore.close()
        val checkID = entityID ?: throw NotFoundException()
        val rc = readXodusEntityAndConvert(checkID)
        mapper.writeValue(out, rc)
        return out.toString()
    }


    protected fun readXodusEntityAndConvert(entityID: String) : T {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        val readEntity = entityStore.computeInReadonlyTransaction { txn ->
            try {
                val entity = txn.getEntity(xodusEntityId)
                factory(entity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        entityStore.close()
        return readEntity
    }
}