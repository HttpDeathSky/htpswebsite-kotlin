package htps.death.htpswebsitekotlin.service

import htps.death.htpswebsitekotlin.dto.UserDto

interface UserService {
    fun insertUser(userDto: UserDto): Int
    fun updateUser(userDto: UserDto): Int
    fun getUserByEmail(userDto: UserDto): UserDto?
    fun login(userDto: UserDto): UserDto
}