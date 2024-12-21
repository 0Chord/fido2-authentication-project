package authentication_project.fido.user.dto

import authentication_project.fido.common.exception.PasswordMismatchException
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupUserRequest(
    @Email
    val email: String,
    @NotBlank
    val password: String,
    @NotBlank
    val rePassword: String,
    @NotBlank
    val nickname: String
){
    fun checkPassword() {
        if (password != rePassword) {
            throw PasswordMismatchException("비밀번호가 일치하지 않습니다")
        }
    }
}

