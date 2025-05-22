package htps.death.htpswebsitekotlin.entity


data class UserEntity(
    val id: String = "",
    val createDate: String = "",
    var nickName: String = "",
    var email: String = "",
    var password: String = ""
)