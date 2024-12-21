package authentication_project.fido.fido.usecase

import authentication_project.fido.common.exception.NotFoundUserException
import authentication_project.fido.fido.domain.Challenge
import authentication_project.fido.fido.dto.*
import authentication_project.fido.fido.repository.ChallengeRepository
import authentication_project.fido.user.domain.User
import authentication_project.fido.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*

@Service
class FidoService(
    private val userRepository: UserRepository<User, Long>,
    private val challengeRepository: ChallengeRepository<Challenge, Long>
) {
    fun createCredentialCreationOptions(request: ServerPublicKeyCredentialCreationOptionsRequest): ServerPublicKeyCredentialCreationOptionsResponse {

        val user = userRepository.findByEmail(request.username) ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")

        val challenge = ByteArray(32).apply {
            SecureRandom().nextBytes(this)
        }

        challengeRepository.save(Challenge.create(user.userId, challenge))

        val userHandle = ByteBuffer.allocate(8).putLong(user.userId).array()

        return ServerPublicKeyCredentialCreationOptionsResponse(
            status = "ok",
            errorMessage = "",
            rp = RpEntityResponse("fido"),
            user = UserResponse(Base64.getEncoder().encodeToString(userHandle), user.email, user.nickname),
            challenge = Base64.getEncoder().encodeToString(challenge),
            pubKeyCredParams = listOf(
                PublicKeyCredentialParameters("public-key", -7)
            ),
            timeout = 10000L,
            excludeCredentials = emptyList(),
            authenticatorSelection = AuthenticatorSelectionResponse(
                authenticatorAttachment = request.authenticatorSelection.authenticatorAttachment,
                requireResidentKey = request.authenticatorSelection.requireResidentKey,
                userVerification = request.authenticatorSelection.userVerification
            ),
            attestation = request.attestation
        )

    }
}