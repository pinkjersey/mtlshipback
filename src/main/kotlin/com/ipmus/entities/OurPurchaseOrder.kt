package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OurPurchaseOrder(override val entityID: String, val vendorID: String, val ourPO: String,
                       val date: String)
    : com.ipmus.entities.Entity {
    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // date sanity check
        LocalDate.parse(date, formatter)
    }

    constructor(entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    vendorID = entity.getLink("vendor")!!.toIdString(),
                    ourPO = entity.getProperty("ourPO") as String,
                    date = entity.getProperty("date") as String
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val po = txn.newEntity(type)
        po.setProperty("ourPO", ourPO)
        po.setProperty("date", date)

        val vendorEntityId = PersistentEntityId.toEntityId(vendorID, store)
        val vendorEntity = txn.getEntity(vendorEntityId)
        vendorEntity.addLink("ourPurchaseOrders", po)
        po.setLink("vendor", vendorEntity)
        return po.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val entityID = PersistentEntityId.toEntityId(this.entityID, store)
        val ourPOXodusEntity = txn.getEntity(entityID)
        ourPOXodusEntity.setProperty("date", date) // only the date is updatable
        return ourPOXodusEntity.toIdString()
    }

    companion object {
        val type = "OurPurchaseOrder"
    }
}