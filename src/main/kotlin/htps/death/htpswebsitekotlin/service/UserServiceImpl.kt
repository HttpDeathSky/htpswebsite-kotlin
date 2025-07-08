package htps.death.htpswebsitekotlin.service

import htps.death.htpswebsitekotlin.dto.UserDto
import htps.death.htpswebsitekotlin.entity.UserEntity
import htps.death.htpswebsitekotlin.mapper.UserMapper
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class UserServiceImpl(private val userMapper: UserMapper) : UserService, UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userMapper.getUserByEmail(UserEntity(email = email))
            ?: throw UsernameNotFoundException("User not found with username: $email")
        user.roles
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.roles.split(",")   // 拆分字符串
                .map { it.trim() }             // 去除空格
                .filter { it.isNotEmpty() }    // 去除空项
                .map { SimpleGrantedAuthority(it) } // 转换为权限对象
        ) // 权限可根据实际调整
    }

    override fun insertUser(userDto: UserDto): Int {
        // 需要新增失败返回
        userMapper.getUserByEmail(UserEntity(email = userDto.email))?.let {
            return -1
        }
        return userMapper.insertUser(
            UserEntity(
                UUID.randomUUID().toString(),
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                userDto.nickName,
                userDto.email,
                BCryptPasswordEncoder().encode(userDto.password),
                userDto.roles ?: "ROLE_USER" // 默认角色
            )
        )
    }

    override fun updateUser(userDto: UserDto): Int {
        val userEntity = UserEntity(userDto.id)
        userDto.nickName?.let { userEntity.nickName = it }
        userDto.email?.let { userEntity.email = it }
        userDto.password?.let { userEntity.password = it }
        userDto.roles?.let { userEntity.roles = it }
        return userMapper.updateUser(userEntity)
    }

    override fun getUserByEmail(userDto: UserDto): UserDto? {
        val userEntity = userMapper.getUserByEmail(UserEntity(userDto.email))
        return userEntity?.let { UserDto(it.id, it.createDate, it.nickName, it.email, it.password, it.roles) }
    }

    override fun login(userDto: UserDto): UserDto {
        TODO("Not yet implemented")
    }
}