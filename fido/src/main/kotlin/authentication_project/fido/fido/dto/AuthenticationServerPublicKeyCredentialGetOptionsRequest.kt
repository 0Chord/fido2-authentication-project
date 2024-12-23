package authentication_project.fido.fido.dto

import jakarta.validation.constraints.NotBlank

data class AuthenticationServerPublicKeyCredentialGetOptionsRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val userVerification: String,
)
