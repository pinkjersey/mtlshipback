package com.ipmus.util

import java.security.MessageDigest
import java.util.*


object DigestPassword {
    fun doWork(plainTextPassword: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(plainTextPassword.toByteArray(charset("UTF-8")))
            val passwordDigest = md.digest()
            return String(Base64.getEncoder().encode(passwordDigest))
        } catch (e: Exception) {
            throw RuntimeException("Exception encoding password", e)
        }

    }
}