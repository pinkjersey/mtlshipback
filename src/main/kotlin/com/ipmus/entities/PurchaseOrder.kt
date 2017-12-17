package com.ipmus.entities

import jetbrains.exodus.entitystore.*
import jetbrains.exodus.entitystore.Entity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.fasterxml.jackson.annotation.JsonFormat.Value.forPattern

/**
 * @property date PO date in YYYYMMDD format
 */
data class PurchaseOrder(override val entityID: String, val customerID: String,
                         val customerPO: String, val date: String)
    : com.ipmus.entities.Entity {
    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // date sanity check
        LocalDate.parse(date, formatter)
    }

    constructor(entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    customerID = entity.getLink("customer")!!.toIdString(),
                    customerPO = entity.getProperty("customerPO") as String,
                    date = entity.getProperty("date") as String
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val po = txn.newEntity(type)
        po.setProperty("customerPO", customerPO)
        po.setProperty("date", date)

        val customerEntityId = PersistentEntityId.toEntityId(customerID, store)
        val customerEntity = txn.getEntity(customerEntityId)
        customerEntity.addLink("purchaseOrders", po)
        po.setLink("customer", customerEntity)
        return po.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "PurchaseOrder"
    }

}