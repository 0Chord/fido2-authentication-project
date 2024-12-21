package authentication_project.fido.user.repository

import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository<T, TID> {
    fun insert(t: T): Unit
    fun findById(uid: TID): T?
    fun findByEmail(email:String): T?
}