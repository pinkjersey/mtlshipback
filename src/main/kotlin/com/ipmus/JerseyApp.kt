package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.entities.Customer
import org.glassfish.jersey.server.ResourceConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application
import java.util.HashSet
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.filter.LoggingFilter
import javax.ws.rs.ext.ContextResolver


@ApplicationPath("/")
class JerseyApp : ResourceConfig(setOf(ItemResource::class.java, CustomerResource::class.java )) {
    init {
        register(ContextResolver<ObjectMapper> { ObjectMapper().registerModule(KotlinModule()) })
    }

}

/*
@ApplicationPath("/")
class MyApplication : ResourceConfig() {
    init {
        // Register resources and providers using package-scanning.
        packages("my.package")

        // Register my custom provider - not needed if it's in my.package.
        register(SecurityRequestFilter::class.java)
        // Register an instance of LoggingFilter.
        register(LoggingFilter(ResourceConfig.LOGGER, true))

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL")
    }
}*/