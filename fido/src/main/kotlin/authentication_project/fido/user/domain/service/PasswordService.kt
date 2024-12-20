package authentication_project.fido.user.domain.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService(
    private val passwordEncoder: PasswordEncoder
) {

    fun encryptPassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

    fun verifyPassword(userPassword: String, rawPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, userPassword)
    }
}