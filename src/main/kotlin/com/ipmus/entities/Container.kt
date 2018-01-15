package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents shipment types.
 */

data class Container(override val entityID: String, val shipmentID: String, val containerName: String,
                     val containerType: String)
    : com.ipmus.entities.Entity {


    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    shipmentID = entity.getLink("shipment")!!.toIdString(),
                    containerName = entity.getProperty("containerName") as String,
                    containerType = entity.getProperty("containerType") as String
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val newContainer = txn.newEntity(type)
        newContainer.setProperty("containerName", containerName)
        newContainer.setProperty("containerType", containerType)

        val shipmentEntityId = PersistentEntityId.toEntityId(shipmentID, store)
        val shipmentEntity = txn.getEntity(shipmentEntityId)
        newContainer.setLink("shipment", shipmentEntity)
        shipmentEntity.addLink("containers", newContainer)

        return newContainer.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "Container"
    }
}