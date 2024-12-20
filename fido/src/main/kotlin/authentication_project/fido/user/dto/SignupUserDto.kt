package authentication_project.fido.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupUserDto(
    @Email
    val email: String,
    @NotBlank
    val password: String,
    @NotBlank
    val nickname: String
)
