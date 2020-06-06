package com.afi.billablehours.middlewares

import com.afi.billablehours.config.JWTTokenUtil
import com.afi.billablehours.services.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTRequestFilter(userService: UserService, jWTTokenUtil: JWTTokenUtil) : OncePerRequestFilter() {
    private val userService: UserService? = userService
    private val jWTTokenUtil: JWTTokenUtil? = jWTTokenUtil

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
                                  chain: FilterChain) {
        if (request.method == "OPTIONS") {
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH")
            response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, Authorization")
            response.setHeader("Access-Control-Max-Age", "3600")
        }
        request.setAttribute("Content-Type", "application/json")
        logger.info(request.method + "     " + request.requestURI)
        val requestTokenHeader = request.getHeader("Authorization")
        var username: String? = null
        var jWTToken = ""
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jWTToken = requestTokenHeader.substring(7)
            try {
                username = jWTTokenUtil?.getUsernameFromToken(jWTToken)
            } catch (e: IllegalArgumentException) {
                println("Unable to get JWT Token")
                return
            } catch (e: ExpiredJwtException) {
                println("JWT Token has expired")

            } catch (e: SignatureException) {
                logger.warn("JWT Signature in invalid")

            } catch (e: Exception) {
                logger.warn(e.javaClass.name + " Exception occurred")

            }
        } else {
            val requestURI = request.requestURI
            if (!requestURI.contains("auth") && !requestURI.contains("info")
                    && !requestURI.contains("home") && !requestURI.contains("actuator")) {
                logger.info(request.method + "     " + request.requestURI)
            }
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                val userDetails: UserDetails = userService!!.loadUserByUsername(username)

                // if token is valid configure Spring Security to manually set
                // authentication
                if (jWTTokenUtil!!.validateToken(jWTToken, userDetails)) {
                    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            } catch (e: UsernameNotFoundException) {
                e.printStackTrace()
                println("Token is no longer valid [due to email change: " + e.message)
            }
        }
        chain.doFilter(request, response)
    }

}
