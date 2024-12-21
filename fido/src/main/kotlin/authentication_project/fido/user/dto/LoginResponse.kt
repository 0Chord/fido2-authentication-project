package authentication_project.fido.user.dto

data class LoginResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val nickname: String,
)
