package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ipmus.resources.*
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.ApplicationPath
import javax.ws.rs.ext.ContextResolver

@ApplicationPath("/")
class JerseyApp : ResourceConfig(setOf(ItemResource::class.java, CustomerResource::class.java,
        BrokerResource::class.java, VesselResource::class.java, VendorResource::class.java,
        DesignResource::class.java, DesignColorResource::class.java, ShipmentTypeResource::class.java,
        ShipmentResource::class.java, ContainerResource::class.java, PurchaseOrderResource::class.java,
        OurPurchaseOrderResource::class.java, VendorInvoiceResource::class.java)) {
    private val logger = LoggerFactory.getLogger(JerseyApp::class.java)

    init {
        val opsys = System.getProperty("os.name").toLowerCase();
        logger.info("JerseyApp init ${opsys}")
        register(CORSResponseFilter::class.java)
        register(ContextResolver<ObjectMapper> { ObjectMapper().registerModule(KotlinModule()) })
    }

}
