package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents vendors.
 */

data class Vendor(override val entityID: String, val vendorName: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), vendorName = entity.getProperty("vendorName") as String)

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val vendor = txn.newEntity(type)
        vendor.setProperty("vendorName", vendorName)
        return vendor.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "Vendor"
    }
}