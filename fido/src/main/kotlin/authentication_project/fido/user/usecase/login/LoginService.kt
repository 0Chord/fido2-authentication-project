package authentication_project.fido.user.usecase.login

import authentication_project.fido.auth.usecase.token.TokenProvider
import authentication_project.fido.common.exception.NotFoundUserException
import authentication_project.fido.user.domain.User
import authentication_project.fido.user.domain.service.PasswordService
import authentication_project.fido.user.dto.LoginRequest
import authentication_project.fido.user.dto.LoginResponse
import authentication_project.fido.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository<User, Long>,
    private val passwordService: PasswordService,
    private val tokenProvider: TokenProvider
) : LoginUsecase {
    override fun login(loginRequest: LoginRequest): LoginResponse {
        userRepository.findByEmail(loginRequest.email)?.let { findUser: User ->
            passwordService.verifyPassword(findUser.password, loginRequest.password)
            val accessToken = tokenProvider.createToken(findUser.userId, findUser.email)
            return LoginResponse(accessToken,findUser.userId)
        } ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")
    }
}