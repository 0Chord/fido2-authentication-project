package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.AuthenticatorEntity

interface AuthenticatorRepository<T, TID> {
    fun save(t: T): Unit
    fun deleteById(tid: TID): Unit
    fun findByUserId(userId:Long): AuthenticatorEntity?
    fun deleteByUserId(userId:Long)
}