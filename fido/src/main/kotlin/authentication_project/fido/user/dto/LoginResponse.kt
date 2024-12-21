package authentication_project.fido.user.dto

data class LoginResponse(
    val token: String,
    val userId: Long
)
