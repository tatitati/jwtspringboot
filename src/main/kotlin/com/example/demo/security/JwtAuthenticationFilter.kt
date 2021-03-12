package com.example.demo.security

import com.example.demo.SecurityConstants
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// the first time a user try to login this Filter is hit.
// This filter check if username and password are correct, and if so, it creates a JWT Bearer Token, which has
// some expiratio period
class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    val authManager: AuthenticationManager

    init {
        setFilterProcessesUrl(SecurityConstants.login_url)
        authManager = authenticationManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse
    ): Authentication {
        val authenticationToken = UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password")
        )

        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain,
            authentication: Authentication
    ) {
        val user = authentication.getPrincipal() as User
        val roles = user.getAuthorities().map{it -> it.authority}
        val signingKey = SecurityConstants.JWT_SECRET.toByteArray()

        val newToken = Jwts
                .builder()
                // jwt token header
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)   // JWT header: alg field
                .setHeaderParam("typ", SecurityConstants.JWT)                   // JWT header: typ field
                // jwt token payload
                .setIssuer(SecurityConstants.secure_api)                              // JWT payload: iss field
                .setAudience(SecurityConstants.secure_app)                            // JWT payload: aud field
                .setSubject(user.getUsername())                                       // JWT payload: sub field
                .setExpiration(Date(System.currentTimeMillis() + 864000000))     // JWT payload: exp field
                .claim("rol", roles)                                            // JWT payload: rol field
                .compact()

        response.addHeader(
                SecurityConstants.Authorization,
                SecurityConstants.Bearer + newToken
        )
    }
}
