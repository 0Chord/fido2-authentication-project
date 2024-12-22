package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.ChallengeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaChallengeRepository : JpaRepository<ChallengeEntity, Long> {
}