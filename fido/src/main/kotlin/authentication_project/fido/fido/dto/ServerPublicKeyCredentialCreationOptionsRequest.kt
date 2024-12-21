package authentication_project.fido.fido.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ServerPublicKeyCredentialCreationOptionsRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val displayName: String,
    @NotNull
    val authenticatorSelection: AuthenticatorSelectionRequest,
    @NotBlank
    val attestation: String,
)