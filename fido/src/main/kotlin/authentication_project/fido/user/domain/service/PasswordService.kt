package authentication_project.fido.user.domain.service

import authentication_project.fido.common.exception.PasswordMismatchException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService(
    private val passwordEncoder: PasswordEncoder
) {

    fun encryptPassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

    fun verifyPassword(userPassword: String, rawPassword: String): Unit {
        if (!passwordEncoder.matches(rawPassword, userPassword)) {
            throw PasswordMismatchException("비밀번호가 일치하지 않습니다")
        }
    }
}