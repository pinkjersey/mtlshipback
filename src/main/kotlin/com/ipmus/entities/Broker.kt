package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents brokers.
 */

data class Broker(override val entityID: String, val brokerName: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), brokerName = entity.getProperty("brokerName") as String)

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val customer = txn.newEntity("Broker");
        customer.setProperty("brokerName", brokerName);
    }
}