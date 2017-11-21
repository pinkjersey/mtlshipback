package com.ipmus.entities

import jetbrains.exodus.entitystore.*
import jetbrains.exodus.entitystore.Entity

data class PurchaseOrder(override val entityID: String, val customerID: String, val customerPO: String)
    : com.ipmus.entities.Entity {
    constructor(entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    customerID = entity.getLink("customer")!!.toIdString(),
                    customerPO = entity.getProperty("customerPO") as String
            )

    override fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val po = txn.newEntity(type)

        val customerEntityId = PersistentEntityId.toEntityId(customerID, store)
        val customerEntity = txn.getEntity(customerEntityId)
        po.addLink("customer", customerEntity)

        po.setProperty("customerPO", customerPO)

        return po.toIdString()
    }

    companion object {
        val type = "PurchaseOrder"
    }
}