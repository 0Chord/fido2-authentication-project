package authentication_project.fido.fido.dto


data class AuthenticationServerPublicKeyCredentialGetOptionsResponse(
    val status: String,
    val errorMessage: String,
    val challenge: String,
    val timeout: Long,
    val rpId: String,
    val allowCredentials: List<AuthenticationAllowCredential>,
    val userVerification: String,
)
