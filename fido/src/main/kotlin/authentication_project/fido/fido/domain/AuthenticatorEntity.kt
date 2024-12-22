package authentication_project.fido.fido.domain

import authentication_project.fido.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "authenticators")
class AuthenticatorEntity private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val authenticatorId: Long = 0,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "credential_id")
    val credentialId: String,
    @Column(name = "public_key")
    val publicKey: ByteArray?,
    @Column(name = "sign_count")
    val signCount: Long
):BaseEntity() {
    companion object {
        fun create(userId: Long, credentialId: String, publicKey: ByteArray?, signCount: Long): AuthenticatorEntity {
            return AuthenticatorEntity(
                userId = userId,
                credentialId = credentialId,
                publicKey = publicKey,
                signCount = signCount
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthenticatorEntity

        return authenticatorId == other.authenticatorId
    }

    override fun hashCode(): Int {
        return authenticatorId.hashCode()
    }


}