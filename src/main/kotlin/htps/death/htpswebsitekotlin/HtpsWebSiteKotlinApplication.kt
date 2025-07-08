package htps.death.htpswebsitekotlin

import htps.death.htpswebsitekotlin.security.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class HtpsWebSiteKotlinApplication

fun main(args: Array<String>) {
    runApplication<HtpsWebSiteKotlinApplication>(*args)
}
