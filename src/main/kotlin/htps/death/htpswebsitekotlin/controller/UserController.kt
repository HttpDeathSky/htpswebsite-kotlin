package htps.death.htpswebsitekotlin.controller

import cn.hutool.core.util.StrUtil
import htps.death.htpswebsitekotlin.dto.UserDto
import htps.death.htpswebsitekotlin.response.ResponseWrapper
import htps.death.htpswebsitekotlin.security.JwtUtil
import htps.death.htpswebsitekotlin.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/login")
    fun login(
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) password: String
    ): ResponseEntity<Map<String, String>> {
        return try {
            val authToken = UsernamePasswordAuthenticationToken(email, password)
            val authentication = authenticationManager.authenticate(authToken)
            SecurityContextHolder.getContext().authentication = authentication
            val token = jwtUtil.generateToken(email)
            ResponseEntity.ok(mapOf("token" to token))
        } catch (ex: AuthenticationException) {
            // 认证失败，返回 401 或自定义错误信息
            ResponseEntity.status(401).body(mapOf("error" to "认证失败：${ex.message}"))
        }
    }

    @PostMapping("/insert")
    fun insertUser(
        @RequestParam(required = true) nickName: String,
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) password: String,
        roles: String?
    ): ResponseWrapper<String> {
        // Check email format
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        if (!emailRegex.matches(email)) {
            return ResponseWrapper.error("Invalid email format.")
        }
        // Check password strength
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,24}$")
        if (!passwordRegex.matches(password)) {
            return ResponseWrapper.error("Password must be 8–24 chars with upper, lower, and number only.")
        }

        val userDto = UserDto(
            nickName = nickName, email = email, password = password
        )

        // admin can insert user with roles
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null
            && authentication !is AnonymousAuthenticationToken
            && authentication.isAuthenticated
            && authentication.authorities.any { it.authority == "ROLE_ADMIN" }
            && roles != null
        ) {
            userDto.roles = roles
        }
        return ResponseWrapper.success(
            userService.insertUser(userDto).toString()
        )
    }

    @PatchMapping("/update/{id}")
    fun updateUser(
        @PathVariable id: String, nickName: String?, email: String?, password: String?
    ): ResponseWrapper<String> {
        val userDto = UserDto(id)
        nickName?.let { userDto.nickName = it }
        email?.let { userDto.email = it }
        password?.let { userDto.password = it }

        if (StrUtil.isEmpty(nickName) && StrUtil.isEmpty(email) && StrUtil.isEmpty(password)) {
            return ResponseWrapper.error("At least one field must be provided for update.")
        }
        return ResponseWrapper.success(userService.updateUser(userDto).toString())
    }
}