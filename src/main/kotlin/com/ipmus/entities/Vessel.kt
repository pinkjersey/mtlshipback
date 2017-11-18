package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents vessels.
 */

data class Vessel(override val entityID: String, val vesselName: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), vesselName = entity.getProperty("vesselName") as String)

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val newEntity = txn.newEntity("Vessel");
        newEntity.setProperty("vesselName", vesselName);
        return newEntity.toIdString()
    }
}