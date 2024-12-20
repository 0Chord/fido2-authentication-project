package authentication_project.fido.user.usecase.signup

import authentication_project.fido.user.dto.SignupUserDto

interface SignupUsecase {
    fun signup(signupUserDto: SignupUserDto): Unit
}