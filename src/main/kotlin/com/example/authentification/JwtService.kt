package com.example.authentification

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.EmployeeModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class JwtService {

    private val issuer = "trading-company-backend"
    private val jwtSecret = "super-strong-secret"
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(employee: EmployeeModel): String {
        return JWT.create()
            .withSubject("EmployeeAuth")
            .withIssuer(issuer)
            .withClaim("login", employee.login)
            .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))
            .sign(algorithm)
    }

    fun getVerifier(): JWTVerifier = verifier
}