package com.ipmus.entities

import jetbrains.exodus.entitystore.PersistentEntityStoreImpl
import jetbrains.exodus.entitystore.StoreTransaction

interface Entity {
    val entityID: String

    fun save(txn: StoreTransaction, store: PersistentEntityStoreImpl)
}