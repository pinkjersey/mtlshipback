package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents designs.
 */

data class Design(override val entityID: String, val designName: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), designName = entity.getProperty("designName") as String)

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val design = txn.newEntity(type);
        design.setProperty("designName", designName);
    }

    companion object {
        val type = "Design"
    }
}