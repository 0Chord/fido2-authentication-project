package authentication_project.fido.user.repository

import org.springframework.stereotype.Repository

@Repository
interface UserRepository<T, TID> {
    fun save(t: T): Unit
    fun findById(uid: TID): T?
    fun findByEmail(email: String): T?
}