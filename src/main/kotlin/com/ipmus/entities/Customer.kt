package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents customers.
 */

data class Customer(override val entityID: String, val customerName: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), customerName = entity.getProperty("customerName") as String)

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val customer = txn.newEntity("Customer");
        customer.setProperty("customerName", customerName);
    }
}