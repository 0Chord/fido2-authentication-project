package authentication_project.fido.fido.dto

data class ServerPublicKeyCredentialCreationOptionsResponse(
    val status: String,
    val errorMessage: String,
    val rp: RpEntityResponse,
    val user: UserResponse,
    val challenge: String,
    val pubKeyCredParams: List<PublicKeyCredentialParameters>,
    val timeout: Long,
    val excludeCredentials: List<PublicKeyCredentialDescriptorResponse>,
    val authenticatorSelection: AuthenticatorSelectionResponse,
    val attestation: String
)
