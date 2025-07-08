package htps.death.htpswebsitekotlin.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

/**
 * JWT工具类，用于处理JWT令牌的生成、解析和验证
 * @property jwtProperties JWT配置属性
 */
@Component
class JwtUtil(
    private val jwtProperties: JwtProperties
) {
    /**
     * 生成JWT令牌
     * @param email 邮箱
     * @return 生成的JWT令牌字符串
     */
    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + jwtProperties.expiration)

        return Jwts.builder()
            .setSubject(email)   //标识
            .setIssuedAt(now)       //签发时间
            .setExpiration(expiry)  //过期时间
            .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()), SignatureAlgorithm.HS256) //签名
            .compact()
    }

    /**
     * 从JWT令牌中提取邮箱
     * @param token JWT令牌
     * @return 邮箱
     */
    fun extractEmail(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(jwtProperties.secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @return 如果令牌有效则返回true，否则返回false
     */
    fun validateToken(token: String): Boolean =
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.secret.toByteArray())
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
}
