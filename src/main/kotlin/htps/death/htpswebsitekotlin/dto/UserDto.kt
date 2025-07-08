package htps.death.htpswebsitekotlin.dto

data class UserDto(
    val id: String = "",
    val createDate: String = "",
    var nickName: String = "",
    var email: String = "",
    var password: String = "",
    var roles: String? = null
)
