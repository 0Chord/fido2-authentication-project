package authentication_project.fido.user.repository

import authentication_project.fido.user.domain.User
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val repository: JpaUserRepository
) : UserRepository<User, Long> {
    override fun insert(t: User) {
        repository.save(t)
    }

    override fun findById(uid: Long): User? {
        return repository.findById(uid).orElse(null)
    }

    override fun findByEmail(email: String): User? {
        return repository.findByEmail(email)
    }
}