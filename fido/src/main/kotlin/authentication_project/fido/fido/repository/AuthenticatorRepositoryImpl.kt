package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.AuthenticatorEntity
import org.springframework.stereotype.Repository

@Repository
class AuthenticatorRepositoryImpl(
    private val repository: JpaAuthenticatorRepository
):AuthenticatorRepository<AuthenticatorEntity,Long> {
    override fun deleteByUserId(tid: Long) {
       repository.deleteById(tid)
    }

    override fun save(t: AuthenticatorEntity) {
        repository.save(t)
    }

    override fun findByUserId(tid: Long): AuthenticatorEntity? {
        return repository.findById(tid).orElse(null)
    }
}