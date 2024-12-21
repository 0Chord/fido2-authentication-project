package authentication_project.fido.user.dto

data class LoginRequest(
    val email:String,
    val password:String,
)
