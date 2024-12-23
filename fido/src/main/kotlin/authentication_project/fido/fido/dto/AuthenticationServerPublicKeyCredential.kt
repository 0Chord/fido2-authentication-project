package authentication_project.fido.fido.dto

data class AuthenticationServerPublicKeyCredential(
    val id:String,
    val response:ServerAuthenticatorAssertionResponse,
    val type:String
)
