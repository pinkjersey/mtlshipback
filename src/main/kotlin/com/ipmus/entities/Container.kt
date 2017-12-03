package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents shipment types.
 */

data class Container(override val entityID: String, val containerName: String, val containerType: String,
                     val items: List<String>) : com.ipmus.entities.Entity {


    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    containerName = entity.getProperty("containerName") as String,
                    containerType = entity.getProperty("containerType") as String,
                    items = entity.getLinks("item").map {
                        it.toIdString()
                    }
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val newEntity = txn.newEntity(type);
        newEntity.setProperty("containerName", containerName);
        newEntity.setProperty("containerType", containerType);

        items.forEach {
            val itemEntityId = PersistentEntityId.toEntityId(it, store)
            val itemEntity = txn.getEntity(itemEntityId)
            newEntity.addLink("item", itemEntity)
        }

        return newEntity.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "Container"
    }
}