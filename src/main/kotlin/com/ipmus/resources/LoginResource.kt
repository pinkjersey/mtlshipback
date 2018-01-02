package com.ipmus.resources

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.util.DigestPassword
import com.ipmus.util.MTLKeyGenerator
import java.io.ByteArrayOutputStream
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders.AUTHORIZATION
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.time.LocalDateTime
import java.util.Date
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import java.time.ZoneId




@Path("login")
class LoginResource {
    private val logger = LoggerFactory.getLogger(LoginResource::class.java)
    data class Credentials(val email: String, val pass: String)
    data class User(val email: String, val fullName: String, val token: String)

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun login(cred: Credentials) : Response {
        try {
            authenticate(cred)

            val token = issueToken(cred.email)

            // if we get here, then the user is authenticated
            val out = ByteArrayOutputStream()
            val mapper = jacksonObjectMapper()
            val u = User(email = cred.email, fullName = "Bobcat Questionmark", token = token)
            mapper.writeValue(out, u)
            return Response.status(200).header(AUTHORIZATION, "Bearer " + token).entity(out.toString()).build();
        }
        catch (_: Exception) {
            throw NotAuthorizedException("Issue logging in")
        }
    }

    private fun authenticate(cred: Credentials) {
        logger.info("Authenticating ${cred.email}")
        val es = GenericResource.entityStore
        val digest = DigestPassword.doWork(cred.pass)
        es.executeInReadonlyTransaction { txn ->  
            val candidates = txn.find("User", "email", cred.email);
            val matching = candidates.filter {
                logger.info("Found candidate")
                it.getProperty("pass") == digest
            }
            if (matching.size != 1) {
                throw NotAuthorizedException("Loging failure ${matching.size}")
            }
        }

    }

    private fun issueToken(email: String): String {
        val key = MTLKeyGenerator.generateKey()
        val jwtToken = Jwts.builder()
                .setSubject(email)
                .setIssuer("mtlshipback.ipmus.com")
                .setIssuedAt(Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact()
        logger.info("#### generating token for a key : $jwtToken - $key")
        return jwtToken
    }

    private fun toDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }
}