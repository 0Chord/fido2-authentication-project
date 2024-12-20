package authentication_project.fido.user.repository

import authentication_project.fido.user.domain.User
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val repository: JpaUserRepository
) : UserRepository<User, Long> {
    override fun insert(t: User) {
        repository.save(t)
    }

    override fun findById(uid: Long): Optional<User> {
        return repository.findById(uid)
    }

    override fun findByEmail(email: String): Optional<User> {
        return repository.findByEmail(email)
    }
}