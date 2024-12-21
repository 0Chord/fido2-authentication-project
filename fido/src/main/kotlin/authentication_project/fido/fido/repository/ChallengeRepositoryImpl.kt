package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.Challenge
import org.springframework.stereotype.Repository

@Repository
class ChallengeRepositoryImpl(
    private val repository: JpaChallengeRepository
) : ChallengeRepository<Challenge, Long> {
    override fun deleteByUserId(tid: Long) {
        repository.deleteById(tid)
    }

    override fun save(t: Challenge) {
        repository.save(t)
    }

    override fun findByUserId(tid: Long): Challenge? {
        return repository.findById(tid).orElse(null)
    }

}