package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.ChallengeEntity
import org.springframework.stereotype.Repository

@Repository
class ChallengeRepositoryImpl(
    private val repository: JpaChallengeRepository
) : ChallengeRepository<ChallengeEntity, Long> {
    override fun deleteByUserId(userId: Long) {
        repository.deleteById(userId)
    }

    override fun save(t: ChallengeEntity) {
        repository.save(t)
    }

    override fun findByUserId(userId: Long): ChallengeEntity? {
        return repository.findById(userId).orElse(null)
    }

    override fun findByChallenge(challenge: ByteArray): ChallengeEntity? {
        return repository.findByChallenge(challenge)
    }

    override fun existsByChallenge(challenge: ByteArray): Boolean {
        return repository.existsByChallenge(challenge)
    }

}