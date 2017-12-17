package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class VendorInvoice(override val entityID: String, val vendorID: String,
                         val invoiceID: String, val invoiceDate: String)
    : com.ipmus.entities.Entity {
    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // date sanity check
        LocalDate.parse(invoiceDate, formatter)
    }

    constructor(entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    vendorID = entity.getLink("vendor")!!.toIdString(),
                    invoiceID = entity.getProperty("invoiceID") as String,
                    invoiceDate = entity.getProperty("invoiceDate") as String
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val invoice = txn.newEntity(VendorInvoice.type)
        invoice.setProperty("invoiceID", invoiceID)
        invoice.setProperty("invoiceDate", invoiceDate)

        val vendorEntityId = PersistentEntityId.toEntityId(vendorID, store)
        val vendorEntity = txn.getEntity(vendorEntityId)
        vendorEntity.addLink("invoices", invoice)
        invoice.setLink("vendor", vendorEntity)
        return invoice.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val type = "VendorInvoice"
    }
}