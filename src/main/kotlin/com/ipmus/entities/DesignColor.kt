package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * This entity represents designs and colors. For example if a design is Doggo and has three colors, red, green,
 * blue, then each combination of design and color would be one entity. Doggo, Red; Doggo, Blue; Doggo, Green.
 */

data class DesignColor(override val entityID: String, val designID: String, val color: String) : com.ipmus.entities.Entity {
    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    designID = entity.getLink("design")!!.toIdString(),
                    color = entity.getProperty("color") as String
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val designColor = txn.newEntity(type);
        designColor.setProperty("color", color);

        val designEntityId = PersistentEntityId.toEntityId(designID, store)
        val designEntity = txn.getEntity(designEntityId)
        designEntity.addLink("colors", designColor)
        // design colors can only have one design, to enforce this, the setLink API is used
        designColor.setLink("design", designEntity)
        return designColor.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "DesignColor"
    }
}