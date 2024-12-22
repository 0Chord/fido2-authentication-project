package authentication_project.fido.fido.repository

import authentication_project.fido.fido.domain.AuthenticatorEntity

interface AuthenticatorRepository<T, TID> {
    fun save(t: T): Unit
    fun findByUserId(tid: TID): AuthenticatorEntity?
    fun deleteByUserId(tid: TID)
}