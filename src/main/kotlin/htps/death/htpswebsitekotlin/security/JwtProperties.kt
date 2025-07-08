package htps.death.htpswebsitekotlin.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

// JwtProperties.kt
@Component
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    lateinit var secret: String
    var expiration: Long = 3600000
}
