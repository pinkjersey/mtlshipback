package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents customers.Container
 */

data class Customer(override val entityID: String, val customerName: String) : com.ipmus.entities.Entity {

    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), customerName = entity.getProperty("customerName") as String)

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val customer = txn.newEntity(type);
        customer.setProperty("customerName", customerName);
        return customer.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "Customer"
    }
}