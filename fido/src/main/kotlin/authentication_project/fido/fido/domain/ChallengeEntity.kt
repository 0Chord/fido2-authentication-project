package authentication_project.fido.fido.domain

import authentication_project.fido.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "challenges")
class ChallengeEntity private constructor(
    @Id
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "challenge", nullable = false)
    val challenge: ByteArray
) : BaseEntity() {

    companion object {
        fun create(userId: Long, challenge: ByteArray): ChallengeEntity {
            return ChallengeEntity(
                userId = userId,
                challenge = challenge
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChallengeEntity

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}