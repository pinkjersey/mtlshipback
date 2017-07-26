package com.ipmus

/**
 * Created by mozturk on 7/26/2017.
 */

import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application
import java.util.HashSet

@ApplicationPath("/")
class JerseyApp : Application() {
    override fun getClasses(): Set<Class<*>> {
        val classes = HashSet<Class<*>>()
        classes.add(ItemResource::class.java)
        return classes
    }
}