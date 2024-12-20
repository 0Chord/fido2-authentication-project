package authentication_project.fido.user.usecase.signup

import authentication_project.fido.user.domain.User
import authentication_project.fido.user.domain.service.PasswordService
import authentication_project.fido.user.dto.SignupUserDto
import authentication_project.fido.user.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SignupService(
    private val userRepository: UserRepository<User, Long>,
    private val passwordService: PasswordService
) : SignupUsecase {

    @Transactional
    override fun signup(@Valid signupUserDto: SignupUserDto) {
        val encryptedPassword: String = passwordService.encryptPassword(signupUserDto.password)
        val user: User = User.create(signupUserDto.email, encryptedPassword, signupUserDto.nickname)
        userRepository.insert(user)
    }
}