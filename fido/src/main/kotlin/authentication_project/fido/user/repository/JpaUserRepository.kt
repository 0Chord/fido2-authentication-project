package authentication_project.fido.user.repository

import authentication_project.fido.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaUserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}