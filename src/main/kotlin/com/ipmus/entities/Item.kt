package com.ipmus.entities

import java.time.LocalDate
import jetbrains.exodus.entitystore.*
import jetbrains.exodus.entitystore.Entity

/**
 * This constructor is the primary constructor and is used by the secondary constructor when creating the
 * entity using database data or is called directly when being constructed by jackson due to a REST API call.
 */

//{"entityID":"","cancelled":false,"poNum":"","vendorID":"4-0","designColorID":"3-2","orderedYards":500,"shippedYards":0,"FOB":0,"LDP":500,"customerPOID":"1-1","millETS":"1980-01-01".}
data class Item(override val entityID: String, val cancelled: Boolean, val ourPOID: String?,
                val vendorID: String, val designColorID: String, val orderedYards: Double,
                val shippedYards: Double, val fob: Int, val ldp: Int,
                val customerPOID: String, val millETS: String) : com.ipmus.entities.Entity {


    init {
        // sanity check dates
        LocalDate.parse(millETS)
    }

    /**
     * This constructor takes data from the data store to create a new customer entity.
     */
    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    cancelled = entity.getProperty(cancelledPropName) == "true",
                    ourPOID = entity.getLink(ourPOPropName)?.toIdString(),
                    vendorID = entity.getLink(vendorPropName)!!.toIdString(),
                    designColorID = entity.getLink(designColorPropName)!!.toIdString(),
                    orderedYards = (entity.getProperty(orderedYardsPropName) as String).toDouble(),
                    shippedYards = (entity.getProperty(shippedYardsPropName) as String).toDouble(),
                    fob = (entity.getProperty(FOBPropName) as String).toInt(),
                    ldp = (entity.getProperty(LDPPropName) as String).toInt(),
                    customerPOID = entity.getLink(customerPOPropName)!!.toIdString(),
                    millETS = entity.getProperty(millETSPropName) as String
            )

    /**
     * This is used to create new items.
     *
     * Saves the entity to the data store. However the ourPO field is not set.
     * This will get updated when the item is added to a vendor PO.
     */
    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        // Create item
        val item = txn.newEntity(type);
        updateEntityProps(item)

        // add item to purchase order
        val customerPOEntityId = PersistentEntityId.toEntityId(customerPOID, store)
        val customerPOEntity = txn.getEntity(customerPOEntityId)
        item.setLink(customerPOPropName, customerPOEntity)
        customerPOEntity.addLink("items", item)

        // add vendor to item
        val vendorEntityId = PersistentEntityId.toEntityId(vendorID, store)
        val vendorEntity = txn.getEntity(vendorEntityId)
        item.setLink(vendorPropName, vendorEntity)
        vendorEntity.addLink("unassignedItems", item)

        // add design color to item
        val designColorEntityId = PersistentEntityId.toEntityId(designColorID, store)
        val designColorEntity = txn.getEntity(designColorEntityId)
        item.addLink(designColorPropName, designColorEntity)

        return item.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val entityID = PersistentEntityId.toEntityId(this.entityID, store)
        val item = txn.getEntity(entityID)
        if (ourPOID != null) {
            val ourPO = item.getLink(ourPOPropName)
            if (ourPO == null) {
                // PO ID set by the client but not yet saved in the db
                val ourPOEntityId = PersistentEntityId.toEntityId(ourPOID, store)
                val ourPOEntity = txn.getEntity(ourPOEntityId)
                item.setLink(ourPOPropName, ourPOEntity)
                ourPOEntity.addLink("items", item)

                // remove item from unassignedItems
                val vendorEntityId = PersistentEntityId.toEntityId(vendorID, store)
                val vendorEntity = txn.getEntity(vendorEntityId)
                vendorEntity.deleteLink("unassignedItems", item)
            }
        }
        updateEntityProps(item)
        return item.toIdString()
    }

    fun updateEntityProps(item: Entity) {
        if (cancelled) {
            item.setProperty(cancelledPropName, "true");
        } else {
            item.setProperty(cancelledPropName, "false");
        }
        item.setProperty(orderedYardsPropName, "%.2f".format(orderedYards))
        item.setProperty(shippedYardsPropName, "%.2f".format(shippedYards))
        item.setProperty(FOBPropName, fob.toString())
        item.setProperty(LDPPropName, ldp.toString())
        item.setProperty(millETSPropName, millETS.toString())
    }

    companion object {
        val type = "Item"

        val cancelledPropName = "cancelled"
        val ourPOPropName = "ourPO"
        val vendorPropName = "vendor"
        val designColorPropName = "designColor"
        val orderedYardsPropName = "orderedYards"
        val shippedYardsPropName = "shippedYards"
        val FOBPropName = "FOB"
        val LDPPropName = "LDP"
        val customerPOPropName = "customerPO"
        val millETSPropName = "millETS"
    }
}