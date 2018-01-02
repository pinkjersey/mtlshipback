package com.ipmus.util

import java.security.Key
import javax.crypto.spec.SecretKeySpec


object MTLKeyGenerator {

    // ======================================
    // =          Business methods          =
    // ======================================

    fun generateKey(): Key {
        val keyString = "simplekey"
        return SecretKeySpec(keyString.toByteArray(), 0, keyString.toByteArray().size, "DES")
    }
}