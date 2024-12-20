package authentication_project.fido.user.domain

import authentication_project.fido.common.entity.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_email", columnList = "email", unique = true)
    ]
)
class User private constructor(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long = 0,
    @Email
    @Column(nullable = false)
    var email: String,
    @NotBlank
    @Column(nullable = false)
    var password: String,
    @NotBlank
    @Column(nullable = false)
    var nickname: String,
) : BaseEntity() {

    companion object {
        fun create(email: String, password: String, nickname: String): User {
            return User(
                email = email.trim(),
                password = password,
                nickname = nickname
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }

}