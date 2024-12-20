package authentication_project.fido.user.dto

import authentication_project.fido.common.exception.PasswordMismatchException
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupUserRequest(
    @Email
    var email: String,
    @NotBlank
    var password: String,
    @NotBlank
    var rePassword: String,
    @NotBlank
    var nickname: String
){
    fun checkPassword() {
        if (password != rePassword) {
            throw PasswordMismatchException("비밀번호가 일치하지 않습니다")
        }
    }
}

