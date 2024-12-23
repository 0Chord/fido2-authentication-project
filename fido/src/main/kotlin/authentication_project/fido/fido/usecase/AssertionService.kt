package authentication_project.fido.fido.usecase

import authentication_project.fido.common.exception.*
import authentication_project.fido.fido.domain.AuthenticatorEntity
import authentication_project.fido.fido.domain.ChallengeEntity
import authentication_project.fido.fido.dto.*
import authentication_project.fido.fido.repository.AuthenticatorRepository
import authentication_project.fido.fido.repository.ChallengeRepository
import authentication_project.fido.user.domain.User
import authentication_project.fido.user.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.webauthn4j.WebAuthnManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Service
class AssertionService(
    private val userRepository: UserRepository<User, Long>,
    private val challengeRepository: ChallengeRepository<ChallengeEntity, Long>,
    private val authenticatorRepository: AuthenticatorRepository<AuthenticatorEntity, Long>
) {
    private val webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager()

    @Transactional
    fun createCredentialCreationOptions(request: AuthenticationServerPublicKeyCredentialGetOptionsRequest): AuthenticationServerPublicKeyCredentialGetOptionsResponse {
        val user = userRepository.findByEmail(request.username) ?: throw NotFoundUserException("해당하는 유저를 찾을 수 없습니다")
        val authenticator = authenticatorRepository.findByUserId(user.userId)
            ?: throw NotFoundUserException("해당하는 유저의 인증 정보를 찾을 수 없습니다")
        val challenge = ByteArray(32).apply {
            SecureRandom().nextBytes(this)
        }

        challengeRepository.save(ChallengeEntity.create(user.userId, challenge))

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

    @Transactional
    fun verifyAssertion(assertion: AuthenticationServerPublicKeyCredential):ServerResponse {
        val clientData = String(Base64.getDecoder().decode(assertion.response.clientDataJSON))
        val clientDataJson = ObjectMapper().readTree(clientData)
        val challenge = clientDataJson.get("challenge").asText()
        val challengeEntity = (challengeRepository.findByChallenge(Base64.getUrlDecoder().decode(challenge))
            ?: throw InvalidChallengeException("해당하는 challenge 정보를 찾을 수 없습니다"))

        val expectedOrigin = "http://localhost:3000"
        if (clientDataJson.get("origin").asText() != expectedOrigin) {
            throw InvalidOriginException("origin 정보가 일치하지 않습니다")
        }

        val authenticatorEntity = authenticatorRepository.findByUserId(challengeEntity.userId)
            ?: throw NotFoundUserException("해당하는 유저의 인증 정보를 찾을 수 없습니다")

        val authData: ByteArray = Base64.getDecoder().decode(assertion.response.authenticatorData)
        val rpIdHash = authData.copyOfRange(0, 32)
        val flags = authData[32]
        val signCount = ByteBuffer.wrap(authData.copyOfRange(33, 37)).getInt()

        val expectedRpIdHash = MessageDigest.getInstance("SHA-256")
            .digest("localhost".toByteArray())
        if (!rpIdHash.contentEquals(expectedRpIdHash)) {
            throw InvalidRpIdHashException("RPIDHash가 일치하지 않습니다")
        }
        if ((flags.toInt() and 0x01) == 0) {
            throw InvalidFlagException("User Presence 플래그가 설정되지 않았습니다")
        }

        val clientDataHash = MessageDigest.getInstance("SHA-256")
            .digest(Base64.getDecoder().decode(assertion.response.clientDataJSON))
        val signatureBase = ByteBuffer.allocate(authData.size + clientDataHash.size)
            .put(authData)
            .put(clientDataHash)
            .array()

        val signature = Base64.getDecoder().decode(assertion.response.signature)

        val publicKey = KeyFactory.getInstance("EC")
            .generatePublic(X509EncodedKeySpec(authenticatorEntity.publicKey?: throw InvalidPublicKeyException("공개키가 null입니다")))
        Signature.getInstance("SHA256withECDSA")
            .apply {
                initVerify(publicKey)
                update(signatureBase)
            }
            .let {
                if (!it.verify(signature)) {
                    throw InvalidSignatureException("서명이 일치하지 않습니다")
                }
            }
        if (signCount >= authenticatorEntity.signCount) {
            authenticatorEntity.updateSignCount(signCount.toLong())
            authenticatorRepository.save(authenticatorEntity)
        } else {
            throw InvalidSignCountException("서명 카운트가 이전 값보다 작거나 같습니다")
        }
        challengeRepository.deleteByUserId(challengeEntity.userId)

        return ServerResponse("ok", "")
    }
}