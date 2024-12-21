package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.Challenge
import org.springframework.data.jpa.repository.JpaRepository

interface JpaChallengeRepository : JpaRepository<Challenge, Long> {
}