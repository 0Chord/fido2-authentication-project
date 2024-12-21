package authentication_project.fido.fido.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthenticatorSelectionRequest(
    @NotNull
    val requireResidentKey: Boolean,
    @NotBlank
    val authenticatorAttachment: String,
    @NotBlank
    val userVerification: String
)
