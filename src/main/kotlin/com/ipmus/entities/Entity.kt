package com.ipmus.entities

import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

interface Entity {
    val entityID: String

    fun create(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String
    fun update(txn: StoreTransaction, store: PersistentEntityStoreImpl) : String

}