package authentication_project.fido.user.usecase.login

import authentication_project.fido.user.dto.LoginRequest

interface LoginUsecase {
    fun login(loginRequest: LoginRequest): String
}