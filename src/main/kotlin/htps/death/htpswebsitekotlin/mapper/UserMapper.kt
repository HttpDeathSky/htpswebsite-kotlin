package htps.death.htpswebsitekotlin.mapper

import htps.death.htpswebsitekotlin.entity.UserEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserMapper {
    @Insert("INSERT INTO users (id, createDate, nickName, email, password) VALUES (#{id}, #{createDate}, #{nickName}, #{email}, #{password})")
    fun insertUser(userEntity: UserEntity): Int

    @Update("""
        <script>
        UPDATE users
        <set>
          <if test="nickName != null and nickName != ''">nickName = #{nickName},</if>
          <if test="email != null and email != ''">email = #{email},</if>
          <if test="password != null and password != ''">password = #{password},</if>
        </set>
        WHERE id = #{id}
        </script>
    """)
    fun updateUser(userEntity: UserEntity): Int

    @Select("SELECT * FROM users WHERE email = #{email}")
    fun getUserByEmail(userEntity: UserEntity): UserEntity?
}
