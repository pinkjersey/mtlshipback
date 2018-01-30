package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ipmus.filter.AuthFilter
import com.ipmus.filter.CORSResponseFilter
import com.ipmus.resources.*
import com.ipmus.util.DigestPassword
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import java.io.File
import javax.ws.rs.ApplicationPath
import javax.ws.rs.ext.ContextResolver

@ApplicationPath("/")
class JerseyApp : ResourceConfig(setOf(ItemResource::class.java, CustomerResource::class.java,
        BrokerResource::class.java, VesselResource::class.java, VendorResource::class.java,
        DesignResource::class.java, DesignColorResource::class.java, ShipmentTypeResource::class.java,
        ShipmentResource::class.java, ContainerResource::class.java, PurchaseOrderResource::class.java,
        OurPurchaseOrderResource::class.java, VendorInvoiceResource::class.java, LoginResource::class.java,
        OurInvoiceResource::class.java)) {
    private val logger = LoggerFactory.getLogger(JerseyApp::class.java)

    init {
        register(AuthFilter::class.java)
        register(CORSResponseFilter::class.java)
        register(ContextResolver<ObjectMapper> { ObjectMapper().registerModule(KotlinModule()) })
        val opsys = System.getProperty("os.name").toLowerCase()
        logger.info("JerseyApp init $opsys")
        val userFile = if (opsys.contains("windows")) {
            "c:/temp/users.txt"
        } else {
            "/home/ubuntu/users.txt"
        }
        initUsers(userFile)
    }

    private fun initUsers(filename: String) {
        val userType = "User"
        val es = GenericResource.entityStore
        val done = es.computeInReadonlyTransaction { txn ->
            val loadedUsers = txn.getAll(userType)
            if (!loadedUsers.isEmpty) {
                logger.info("Users exist. Doing nothing")
                true
            } else {
                false
            }
        }

        if (done) {
            return
        }

        logger.info("Users not initialized, initializing")
        val users = File(filename).readLines()
        users.forEach {
            val tokens = it.split("\t")
            if (tokens.size == 3) {
                logger.info("Adding user ${tokens[1]}")
                es.executeInTransaction { txn ->
                    val user = txn.newEntity(userType)
                    user.setProperty("email", tokens[0])
                    user.setProperty("name", tokens[1])
                    user.setProperty("pass", DigestPassword.doWork(tokens[2]))
                }
            }
        }



    }
}
