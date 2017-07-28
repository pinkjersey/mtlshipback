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

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val vendor = txn.newEntity("Vendor");
        vendor.setProperty("vendorName", vendorName);
    }
}