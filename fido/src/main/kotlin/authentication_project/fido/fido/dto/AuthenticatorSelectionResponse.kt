package authentication_project.fido.fido.dto

data class AuthenticatorSelectionResponse(
    val authenticatorAttachment: String,
    val requireResidentKey: Boolean,
    val userVerification: String
)
