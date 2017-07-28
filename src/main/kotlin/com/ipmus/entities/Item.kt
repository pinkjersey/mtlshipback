package com.ipmus.entities

import java.time.LocalDate
import jetbrains.exodus.bindings.StringBinding
import jetbrains.exodus.entitystore.*
import jetbrains.exodus.entitystore.Entity

/**
 * This constructor is the primary constructor and is used by the secondary constructor when creating the
 * entity using database data or is called directly when being constructed by jackson due to a REST API call.
 */

data class Item(override val entityID: String, val cancelled: Boolean, val date: String, val poNum: String,
        //val vendor: Vendor,
        //val design: DesignColor,
                val shippedYards: Double, val FOB: Int, val LDP: Int,
                val customerID: String,
                val customerPO: String, val millETS: String) : com.ipmus.entities.Entity {
    //val shipment: Shipment?) {
    init {
        // sanity check dates
        LocalDate.parse(date)
        LocalDate.parse(millETS)
    }

    /**
     * This constructor takes data from the data store to create a new customer entity.
     */
    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    cancelled = entity.getProperty("cancelled") == "true",
                    date = entity.getProperty("date") as String,
                    poNum = entity.getProperty("poNum") as String,
                    shippedYards = (entity.getProperty("shippedYards") as String).toDouble(),
                    FOB = (entity.getProperty("FOB") as String).toInt(),
                    LDP = (entity.getProperty("LDP") as String).toInt(),
                    customerID = entity.getLink("customer")!!.toIdString(),
                    customerPO = entity.getProperty("customerPO") as String,
                    millETS = entity.getProperty("millETS") as String
            )

    /**
     * Saves the entity to the data store.
     */
    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) {
        val item = txn.newEntity("Item");
        if (cancelled) {
            item.setProperty("cancelled", "true");
        } else {
            item.setProperty("cancelled", "false");
        }
        item.setProperty("date", date.toString())
        item.setProperty("poNum", poNum)
        item.setProperty("shippedYards", "%.2f".format(shippedYards))
        item.setProperty("FOB", FOB.toString())
        item.setProperty("LDP", LDP.toString())
        item.setProperty("customerPO", customerPO)
        item.setProperty("millETS", millETS.toString())

        val customerEntityId = PersistentEntityId.toEntityId(customerID, store)
        val customeEntity = txn.getEntity(customerEntityId)
        item.addLink("customer", customeEntity)
    }

}