package authentication_project.fido.fido.usecase

import authentication_project.fido.auth.usecase.token.TokenProvider
import authentication_project.fido.common.exception.NotFoundChallengeException
import authentication_project.fido.common.exception.NotFoundUserException
import authentication_project.fido.common.exception.WebAuthnVerificationException
import authentication_project.fido.fido.domain.AuthenticatorEntity
import authentication_project.fido.fido.domain.ChallengeEntity
import authentication_project.fido.fido.dto.*
import authentication_project.fido.fido.repository.AuthenticatorRepository
import authentication_project.fido.fido.repository.ChallengeRepository
import authentication_project.fido.user.domain.User
import authentication_project.fido.user.repository.UserRepository
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationData
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.Challenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.verifier.exception.VerificationException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*

@Service
class AttestationService(
    private val userRepository: UserRepository<User, Long>,
    private val challengeRepository: ChallengeRepository<ChallengeEntity, Long>,
    private val tokenProvider: TokenProvider,
    private val authenticatorRepository: AuthenticatorRepository<AuthenticatorEntity, Long>
) {
    private val webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager()

    @Transactional
    fun createCredentialCreationOptions(request: ServerPublicKeyCredentialCreationOptionsRequest): ServerPublicKeyCredentialCreationOptionsResponse {
        val user = userRepository.findByEmail(request.username) ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")

        val challenge = ByteArray(32).apply {
            SecureRandom().nextBytes(this)
        }

        challengeRepository.save(ChallengeEntity.create(user.userId, challenge))

        val userHandle = ByteBuffer.allocate(8).putLong(user.userId).array()

        return ServerPublicKeyCredentialCreationOptionsResponse(
            status = "ok",
            errorMessage = "",
            rp = RpEntityResponse("localhost"),
            user = UserResponse(Base64.getUrlEncoder().encodeToString(userHandle), user.email, user.nickname),
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

    @Transactional
    fun verifyAttestation(request: ServerPublicKeyCredential, httpRequest: HttpServletRequest): ServerResponse {
        val attestationObject = Base64.getDecoder().decode(request.response.attestationObject)
        val clientDataJSON = Base64.getDecoder().decode(request.response.clientDataJSON)
        val accessToken = httpRequest.getHeader("Authorization").split(" ")[1]

        val userId: Long = tokenProvider.getUserId(accessToken)

        val registrationData: RegistrationData = webAuthnManager.parse(
            RegistrationRequest(
                attestationObject,
                clientDataJSON

            )
        )

        val challenge =
            challengeRepository.findByUserId(userId) ?: throw NotFoundChallengeException("해당하는 Challenge를 찾을 수 없습니다")
        val findUser = userRepository.findById(userId) ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")

        val origin = Origin("http://localhost:3000")
        val challengeValue = challenge.challenge
        val challengeObject = Challenge { challengeValue }

        val serverProperty = ServerProperty(
            origin,
            "localhost",
            challengeObject
        )

        val publicKeyCredentialParameters: List<com.webauthn4j.data.PublicKeyCredentialParameters> = listOf(
            com.webauthn4j.data.PublicKeyCredentialParameters(
                PublicKeyCredentialType.PUBLIC_KEY,
                COSEAlgorithmIdentifier.ES256
            )
        )

        val registrationParameters = RegistrationParameters(
            serverProperty,
            publicKeyCredentialParameters,
            false
        )

        try {
            val response = webAuthnManager.verify(registrationData, registrationParameters)

            val authenticatorEntity = AuthenticatorEntity.create(
                userId = findUser.userId,
                credentialId = Base64.getUrlEncoder()
                    .encodeToString(response.attestationObject?.authenticatorData?.attestedCredentialData?.credentialId),
                publicKey = response.attestationObject?.authenticatorData?.attestedCredentialData?.coseKey?.publicKey?.encoded,
                signCount = response.attestationObject?.authenticatorData?.signCount ?: 0
            )

            authenticatorRepository.save(authenticatorEntity)
            challengeRepository.deleteByUserId(findUser.userId)
            return ServerResponse("ok","")
        } catch (e: VerificationException) {
            println(e)
            throw WebAuthnVerificationException("WebAuthn Verification 실패")
        }
    }
}