package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.PersistentEntityId
import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OurInvoice(override val entityID: String, val customerID: String, val invoiceID: String,
                 val date: String, val terms: Int, val paid: Boolean)
    : com.ipmus.entities.Entity {

    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // date sanity check
        LocalDate.parse(date, formatter)
    }

    constructor(entity: Entity) :
            this(
                    entityID = entity.toIdString(),
                    customerID = entity.getLink(customerPropName)!!.toIdString(),
                    invoiceID = entity.getProperty(invoiceIDPropName) as String,
                    date = entity.getProperty(invoiceDatePropName) as String,
                    terms = entity.getProperty(termsPropName) as Int,
                    paid = entity.getProperty(paidPropName) as Boolean
            )

    override fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val invoice = txn.newEntity(type)
        invoice.setProperty(invoiceIDPropName, invoiceID)
        invoice.setProperty(invoiceDatePropName, date)
        invoice.setProperty(termsPropName, terms)
        invoice.setProperty(paidPropName, paid)

        val customerEntityId = PersistentEntityId.toEntityId(customerID, store)
        val customerEntity = txn.getEntity(customerEntityId)
        customerEntity.addLink("ourInvoices", invoice)
        invoice.setLink(customerPropName, customerEntity)
        return invoice.toIdString()
    }

    override fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl): String {
        val entityID = PersistentEntityId.toEntityId(this.entityID, store)
        val invoice = txn.getEntity(entityID)
        invoice.setProperty(invoiceIDPropName, invoiceID)
        invoice.setProperty(invoiceDatePropName, date)
        invoice.setProperty(termsPropName, terms)
        invoice.setProperty(paidPropName, paid)
        return invoice.toIdString()
    }

    companion object {
        val type = "OurInvoice"
        val customerPropName = "customer"
        val invoiceIDPropName = "invoiceID"
        val invoiceDatePropName = "invoiceDate"
        val termsPropName = "terms"
        val paidPropName = "paid"
    }
}