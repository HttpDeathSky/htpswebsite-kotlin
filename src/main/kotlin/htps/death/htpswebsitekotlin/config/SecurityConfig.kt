package htps.death.htpswebsitekotlin.config

import htps.death.htpswebsitekotlin.security.JwtAccessDeniedHandler
import htps.death.htpswebsitekotlin.security.JwtAuthenticationEntryPoint
import htps.death.htpswebsitekotlin.security.JwtAuthenticationFilter
import htps.death.htpswebsitekotlin.security.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Spring Security 配置类
 *
 * @param jwtUtil JWT工具类，用于处理JWT相关操作
 * @param userDetailsService 用户详情服务，用于加载用户信息
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    /**
     * 配置安全过滤器链
     *
     * @param http HttpSecurity对象
     * @return 配置好的SecurityFilterChain
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() } // 禁用CSRF保护
            .authorizeHttpRequests {
                it.requestMatchers("/api/user/login","/api/user/insert").permitAll() // 允许认证相关接口无需认证访问
                    .anyRequest().authenticated() // 其他所有请求都需要认证
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用无状态会话
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter::class.java
            ) // 添加JWT认证过滤器
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .build()
    }

    /**
     * 配置认证管理器
     *
     * @param auth 认证配置对象
     * @return AuthenticationManager实例
     */
    @Bean
    fun authenticationManager(auth: AuthenticationConfiguration): AuthenticationManager =
        auth.authenticationManager

    /**
     * 配置密码编码器
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

