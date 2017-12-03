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
        val items: List<T> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll(resourceType).map { factory(it) }
        }
        mapper.writeValue(out, items)
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
        var entityID: String? = null
        entityStore.executeInTransaction { txn ->
            entityID = entity.create(txn, entityStore)
        }
        val checkID = entityID ?: throw NotFoundException()
        val rc = readXodusEntityAndConvert(checkID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    fun updateEntity(entity: com.ipmus.entities.Entity) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        var entityID: String? = null
        entityStore.executeInTransaction { txn ->
            entityID = entity.update(txn, entityStore)
        }
        val checkID = entityID ?: throw NotFoundException()
        val rc = readXodusEntityAndConvert(checkID)
        mapper.writeValue(out, rc)
        return out.toString()
    }

    fun <ChildT> getChildren(entityID: String, propName: String, childFactory : (Entity) -> ChildT) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        // we need to read the design entity from xodus, get the designColor entities
        // and convert these to the local entity type
        val es = entityStore
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, es)
        val ret = es.computeInReadonlyTransaction { txn ->
            try {
                val entity = txn.getEntity(xodusEntityId)
                entity.getLinks(propName).map {
                    childFactory(it)
                }
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
        mapper.writeValue(out, ret)
        return out.toString()
    }

    protected fun readXodusEntityAndConvert(entityID: String) : T {
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
        return readEntity
    }

    companion object {
        val entityStore = PersistentEntityStores.newInstance(Configuration.dataLocation)
    }
}