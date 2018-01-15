package com.ipmus.resources

import com.ipmus.Configuration
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jetbrains.exodus.entitystore.*

open class GenericResource<out T>(private val resourceType: String, private val factory : (Entity) -> T) {

    fun getAll(queryParam: String? = null) : String {
        val out = ByteArrayOutputStream()
        val mapper = jacksonObjectMapper()
        val items: List<T> = entityStore.computeInReadonlyTransaction { txn ->
            if (queryParam != null) {
                val tokens = queryParam.split(":")
                if (tokens.size == 1) {
                    txn.find(resourceType, tokens[0], true)
                } else {
                    txn.find(resourceType, tokens[0], tokens[1])
                }
            } else {
                txn.getAll(resourceType)
            }.map { factory(it) }
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

    private fun readXodusEntityAndConvert(entityID: String) : T {
        val xodusEntityId = PersistentEntityId.toEntityId(entityID, entityStore)
        return entityStore.computeInReadonlyTransaction { txn ->
            try {
                val entity = txn.getEntity(xodusEntityId)
                factory(entity)
            }
            catch (e: EntityRemovedInDatabaseException) {
                throw NotFoundException()
            }
        }
    }

    companion object {
        val entityStore: PersistentEntityStoreImpl = PersistentEntityStores.newInstance(Configuration.dataLocation)
    }
}