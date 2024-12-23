package authentication_project.fido.fido.usecase

import authentication_project.fido.common.exception.NotFoundUserException
import authentication_project.fido.fido.domain.AuthenticatorEntity
import authentication_project.fido.fido.domain.ChallengeEntity
import authentication_project.fido.fido.dto.AuthenticationAllowCredential
import authentication_project.fido.fido.dto.AuthenticationServerPublicKeyCredentialGetOptionsRequest
import authentication_project.fido.fido.dto.AuthenticationServerPublicKeyCredentialGetOptionsResponse
import authentication_project.fido.fido.repository.AuthenticatorRepository
import authentication_project.fido.fido.repository.ChallengeRepository
import authentication_project.fido.user.domain.User
import authentication_project.fido.user.repository.UserRepository
import com.webauthn4j.WebAuthnManager
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*

@Service
class AssertionService(
    private val userRepository: UserRepository<User, Long>,
    private val challengeRepository: ChallengeRepository<ChallengeEntity, Long>,
    private val authenticatorRepository: AuthenticatorRepository<AuthenticatorEntity, Long>
) {
    private val webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager()

    fun createCredentialCreationOptions(request: AuthenticationServerPublicKeyCredentialGetOptionsRequest): AuthenticationServerPublicKeyCredentialGetOptionsResponse {
        val user = userRepository.findByEmail(request.username) ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")
        val authenticator = authenticatorRepository.findByUserId(user.userId)
            ?: throw NotFoundUserException("해당하는 유저의 인증 정보를 찾을 수 없습니다")
        val challenge = ByteArray(32).apply {
            SecureRandom().nextBytes(this)
        }

        challengeRepository.save(ChallengeEntity.create(user.userId, challenge))

        val userHandle = ByteBuffer.allocate(8).putLong(user.userId).array()

        return AuthenticationServerPublicKeyCredentialGetOptionsResponse(
            status = "ok",
            errorMessage = "",
            challenge = Base64.getEncoder().encodeToString(challenge),
            timeout = 20000L,
            rpId = "localhost",
            allowCredentials = listOf(
                AuthenticationAllowCredential(
                    id = authenticator.credentialId,
                    type = "public-key",
                )
            ),
            userVerification = "required"
        )
    }
}