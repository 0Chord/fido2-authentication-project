package authentication_project.fido.fido.dto

data class ServerPublicKeyCredential(
    val id: String,
    val response: ServerAuthenticatorAttestationResponse,
    val type: String
)
