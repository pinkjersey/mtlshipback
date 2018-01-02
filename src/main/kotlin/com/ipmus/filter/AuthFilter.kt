package com.ipmus.filter

import com.ipmus.JerseyApp
import com.ipmus.util.MTLKeyGenerator
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import java.security.Key
import java.util.logging.Logger
import javax.annotation.Priority
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.Priorities
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.ext.Provider
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.ws.rs.core.Response


@Provider
@TokenNeeded
@Priority(Priorities.AUTHENTICATION)
class AuthFilter() : ContainerRequestFilter {
    private val logger = LoggerFactory.getLogger(AuthFilter::class.java)

    override fun filter(requestContext: ContainerRequestContext?) {
        if (requestContext != null) {
            val authHeader: String = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)
                    ?: throw NotAuthorizedException("Invalid auth header")
            if (authHeader.startsWith("Bearer ")) {
                logger.info(authHeader)
                val token = authHeader.substring("Bearer".length).trim();
                try {
                    val key = MTLKeyGenerator.generateKey()
                    Jwts.parser().setSigningKey(key).parseClaimsJws(token);
                    logger.info("#### valid token : " + token);
                }
                catch (e: Exception) {
                    logger.error("#### invalid token : " + token);
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            } else {
                throw NotAuthorizedException("Invalid auth header")
            }
        } else {
            throw IllegalStateException("Something not set with the filter")
        }
    }

}