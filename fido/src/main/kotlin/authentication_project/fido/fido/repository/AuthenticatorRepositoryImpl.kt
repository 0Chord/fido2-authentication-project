package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.AuthenticatorEntity
import org.springframework.stereotype.Repository

@Repository
class AuthenticatorRepositoryImpl(
    private val repository: JpaAuthenticatorRepository
):AuthenticatorRepository<AuthenticatorEntity,Long> {
    override fun deleteByUserId(userId: Long) {
       repository.deleteById(userId)
    }

    override fun save(t: AuthenticatorEntity) {
        repository.save(t)
    }

    override fun deleteById(tid: Long) {
        repository.deleteById(tid)
    }

    override fun findByUserId(userId: Long): AuthenticatorEntity? {
        return repository.findById(userId).orElse(null)
    }
}