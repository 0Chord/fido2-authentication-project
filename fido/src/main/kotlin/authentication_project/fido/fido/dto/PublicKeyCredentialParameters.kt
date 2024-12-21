package authentication_project.fido.fido.dto

data class PublicKeyCredentialParameters(
    val type: String,
    val alg: Int
)
