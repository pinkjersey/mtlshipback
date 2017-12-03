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

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String{
        val broker = txn.newEntity(type);
        broker.setProperty("brokerName", brokerName);
        return broker.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "Broker"
    }
}