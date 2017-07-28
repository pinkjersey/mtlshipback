package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents shipment types.
 */

data class ShipmentType(override val entityID: String, val shipmentType: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), shipmentType = entity.getProperty("shipmentType") as String)

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val newEntity = txn.newEntity(type);
        newEntity.setProperty("shipmentType", shipmentType);
    }

    companion object {
        val type = "ShipmentType"
    }
}