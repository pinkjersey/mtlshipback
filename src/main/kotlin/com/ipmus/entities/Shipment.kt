package com.ipmus.entities

import java.time.LocalDate
import jetbrains.exodus.entitystore.*
import jetbrains.exodus.entitystore.Entity

/**
 * This constructor is the primary constructor and is used by the secondary constructor when creating the
 * entity using database data or is called directly when being constructed by jackson due to a REST API call.
 */


data class Shipment(override val entityID: String, val shipmentTypeID: String,
                    val ETA: String, val vesselID: String, val brokerID: String, val containerID: String,
                    val status: ShipmentStatus) : com.ipmus.entities.Entity {
    init {
        // sanity check dates
        LocalDate.parse(ETA)
    }

    /**
     * This constructor takes data from the data store to create a new customer entity.
     */
    constructor (entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    shipmentTypeID = entity.getLink("shipmentType")!!.toIdString(),
                    ETA = entity.getProperty("ETA") as String,
                    vesselID = entity.getLink("vessel")!!.toIdString(),
                    brokerID = entity.getLink("broker")!!.toIdString(),
                    containerID = entity.getLink("container")!!.toIdString(),
                    status = ShipmentStatus.valueOf(entity.getProperty("status") as String)
            )

    /**
     * Saves the entity to the data store.
     */
    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String {
        val shipment = txn.newEntity(type);
        shipment.setProperty("status", status.toString())
        shipment.setProperty("ETA", ETA)

        val shipmentTypeEntityId = PersistentEntityId.toEntityId(shipmentTypeID, store)
        val shipmentTypeEntity = txn.getEntity(shipmentTypeEntityId)
        shipment.addLink("shipmentType", shipmentTypeEntity)

        val vesselEntityId = PersistentEntityId.toEntityId(vesselID, store)
        val vesselEntity = txn.getEntity(vesselEntityId)
        shipment.addLink("vessel", vesselEntity)

        val brokerEntityId = PersistentEntityId.toEntityId(brokerID, store)
        val brokerEntity = txn.getEntity(brokerEntityId)
        shipment.addLink("broker", brokerEntity)

        val containerEntityId = PersistentEntityId.toEntityId(containerID, store)
        val containerEntity = txn.getEntity(containerEntityId)
        shipment.addLink("container", containerEntity)

        return shipment.toIdString()
    }

    enum class ShipmentStatus { S, D }

    companion object {
        val type = "Shipment"
    }
}