package authentication_project.fido.fido.dto

data class ServerAuthenticatorAttestationResponse(
    val clientDataJSON: String,
    val attestationObject: String
)
