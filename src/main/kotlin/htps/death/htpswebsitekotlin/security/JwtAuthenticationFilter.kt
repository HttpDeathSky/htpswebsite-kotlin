package htps.death.htpswebsitekotlin.security

// 导入必要的依赖
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT认证过滤器
 * 用于拦截HTTP请求，验证JWT token并设置认证信息
 *
 * @property jwtUtil JWT工具类，用于token的相关操作
 * @property userDetailsService 用户详情服务，用于加载用户信息
 */
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    /**
     * 过滤器的主要处理逻辑
     *
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 从请求头中获取Authorization信息
        val authHeader = request.getHeader("Authorization")

        // 检查是否存在Bearer token
        if (authHeader?.startsWith("Bearer ") == true) {
            // 提取token字符串（去除"Bearer "前缀）
            val jwt = authHeader.substring(7)
            // 从token中提取邮箱
            val email = jwtUtil.extractEmail(jwt)

            // 如果成功提取到邮箱且当前SecurityContext中没有认证信息
            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                // 加载用户详情
                val userDetails = userDetailsService.loadUserByUsername(email)

                // 验证token有效性
                if (jwtUtil.validateToken(jwt)) {
                    // 创建认证token
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    // 设置认证详情
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    // 将认证信息设置到SecurityContext中
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        // 继续处理过滤器链
        filterChain.doFilter(request, response)
    }
}
