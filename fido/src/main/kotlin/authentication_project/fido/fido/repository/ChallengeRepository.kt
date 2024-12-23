package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.ChallengeEntity

interface ChallengeRepository<T, TID> {
    fun save(t: T): Unit
    fun findByUserId(userId: Long): ChallengeEntity?
    fun findByChallenge(challenge: ByteArray): ChallengeEntity?
    fun deleteByUserId(userId: Long)
    fun existsByChallenge(challenge: ByteArray): Boolean
}