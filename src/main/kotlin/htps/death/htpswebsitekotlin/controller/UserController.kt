package htps.death.htpswebsitekotlin.controller

import cn.hutool.core.util.StrUtil
import htps.death.htpswebsitekotlin.dto.UserDto
import htps.death.htpswebsitekotlin.response.ResponseWrapper
import htps.death.htpswebsitekotlin.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
    @PostMapping("/insert")
    fun insertUser(
        @RequestParam(required = true) nickName: String,
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) password: String
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
        return ResponseWrapper.success(
            userService.insertUser(
                UserDto(
                    nickName = nickName, email = email, password = password
                )
            ).toString()
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