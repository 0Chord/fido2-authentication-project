package authentication_project.fido.user.usecase.login

import authentication_project.fido.user.dto.LoginRequest
import authentication_project.fido.user.dto.LoginResponse

interface LoginUsecase {
    fun login(loginRequest: LoginRequest): LoginResponse
}