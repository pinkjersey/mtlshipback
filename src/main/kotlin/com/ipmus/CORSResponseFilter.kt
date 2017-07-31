package com.ipmus

import java.io.IOException

import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.MultivaluedMap

class CORSResponseFilter : ContainerResponseFilter {

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {

        val headers = responseContext.headers

        headers.add("Access-Control-Allow-Origin", "*")
        //headers.add("Access-Control-Allow-Origin", "http://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org
        //headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        //headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia")
    }

}