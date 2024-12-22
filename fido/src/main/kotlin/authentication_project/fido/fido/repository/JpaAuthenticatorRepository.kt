package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.AuthenticatorEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAuthenticatorRepository : JpaRepository<AuthenticatorEntity, Long> {
}