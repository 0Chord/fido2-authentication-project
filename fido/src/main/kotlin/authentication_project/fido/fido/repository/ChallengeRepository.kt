package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.Challenge

interface ChallengeRepository<T,TID> {
    fun save(t: T): Unit
    fun findByUserId(tid: TID): Challenge?
    fun deleteByUserId(tid: TID)
}