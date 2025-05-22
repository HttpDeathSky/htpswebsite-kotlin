package htps.death.htpswebsitekotlin.service

import htps.death.htpswebsitekotlin.dto.UserDto
import htps.death.htpswebsitekotlin.entity.UserEntity
import htps.death.htpswebsitekotlin.mapper.UserMapper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class UserServiceImpl(private val userMapper: UserMapper) : UserService {

    override fun insertUser(userDto: UserDto): Int {
        userMapper.getUserByEmail(UserEntity(email = userDto.email))?.let {
            return -1
        }
        return userMapper.insertUser(
            UserEntity(
                UUID.randomUUID().toString(),
                SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(Date()),
                userDto.nickName,
                userDto.email,
                BCryptPasswordEncoder().encode(userDto.password)
            )
        )
    }

    override fun updateUser(userDto: UserDto): Int {
        val userEntity = UserEntity(userDto.id)
        userDto.nickName?.let { userEntity.nickName = it }
        userDto.email?.let { userEntity.email = it }
        userDto.password?.let { userEntity.password = it }
        return userMapper.updateUser(userEntity)
    }

    override fun getUserByEmail(userDto: UserDto): UserDto? {
        val userEntity = userMapper.getUserByEmail(UserEntity(userDto.email))
        return userEntity?.let { UserDto(it.id, it.createDate, it.nickName, it.email, it.password) }
    }

    override fun login(userDto: UserDto): UserDto {
        TODO("Not yet implemented")
    }
}