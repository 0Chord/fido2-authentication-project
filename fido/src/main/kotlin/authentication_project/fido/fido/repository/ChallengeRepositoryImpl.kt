package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.ChallengeEntity
import org.springframework.stereotype.Repository

@Repository
class ChallengeRepositoryImpl(
    private val repository: JpaChallengeRepository
) : ChallengeRepository<ChallengeEntity, Long> {
    override fun deleteByUserId(tid: Long) {
        repository.deleteById(tid)
    }

    override fun save(t: ChallengeEntity) {
        repository.save(t)
    }

    override fun findByUserId(tid: Long): ChallengeEntity? {
        return repository.findById(tid).orElse(null)
    }

}