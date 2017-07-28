package com.ipmus.entities

import com.fasterxml.jackson.annotation.JsonCreator
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.StoreTransaction

/**
 * Created by mdozturk on 7/27/17.
 */

data class Customer(val entityID: String, val customerName: String) {
    constructor (entity: Entity) :
            this(entityID = entity.toIdString(), customerName = entity.getProperty("customerName") as String)

    fun save(txn: StoreTransaction) {
        val customer = txn.newEntity("Customer");
        customer.setProperty("customerName", customerName);
    }
}