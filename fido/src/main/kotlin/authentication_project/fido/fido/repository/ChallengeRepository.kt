package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.ChallengeEntity

interface ChallengeRepository<T,TID> {
    fun save(t: T): Unit
    fun findByUserId(tid: TID): ChallengeEntity?
    fun deleteByUserId(tid: TID)
}