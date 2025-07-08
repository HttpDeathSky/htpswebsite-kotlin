package htps.death.htpswebsitekotlin.db

import jakarta.annotation.PostConstruct
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseInit(val jdbcTemplate: JdbcTemplate) {

    @PostConstruct
    fun init() {
        jdbcTemplate.execute(
            """
            CREATE TABLE IF NOT EXISTS users (
                id TEXT PRIMARY KEY,
                createDate TEXT,
                nickName TEXT,
                email TEXT,
                password TEXT,
                roles TEXT
            )
        """.trimIndent()
        )
    }
}
