package com.ipmus.resources

import com.ipmus.entities.Broker
import javax.ws.rs.*
import javax.ws.rs.POST
import javax.ws.rs.core.MediaType


/**
 * Created by mdozturk on 7/27/17.
 */
@Path("brokers")
class BrokerResource : GenericResource<Broker>(Broker.type, ::Broker) {
    @GET
    @Produces("application/json")
    fun brokers() : String{
        return getAll()
    }

    @Path("/{entityID}")
    @GET
    @Produces("application/json")
    fun broker(@PathParam("entityID") entityID: String) : String {
        return getSpecific(entityID)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    fun newBroker(entity: com.ipmus.entities.Entity) : String {
        return newEntity(entity)
    }
}