package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ipmus.resources.*
import org.glassfish.jersey.server.ResourceConfig
import javax.ws.rs.ApplicationPath
import javax.ws.rs.ext.ContextResolver

@ApplicationPath("/")
class JerseyApp : ResourceConfig(setOf(ItemResource::class.java, CustomerResource::class.java,
        BrokerResource::class.java, VesselResource::class.java, VendorResource::class.java,
        DesignResource::class.java, DesignColorResource::class.java)) {
    init {
        register(ContextResolver<ObjectMapper> { ObjectMapper().registerModule(KotlinModule()) })
    }

}
