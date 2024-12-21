package authentication_project.fido.fido.api

import authentication_project.fido.fido.dto.ServerPublicKeyCredentialCreationOptionsRequest
import authentication_project.fido.fido.dto.ServerPublicKeyCredentialCreationOptionsResponse
import authentication_project.fido.fido.usecase.FidoService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/attestation")
class RegisterController(
    private val fidoService: FidoService
) {

    @PostMapping("/options")
    fun register(@Valid @RequestBody serverPublicKeyCredentialCreationOptionsRequest: ServerPublicKeyCredentialCreationOptionsRequest): ResponseEntity<ServerPublicKeyCredentialCreationOptionsResponse> {
        val serverPublicKeyCredentialCreationOptionsResponse =
            fidoService.createCredentialCreationOptions(serverPublicKeyCredentialCreationOptionsRequest)
        return ResponseEntity.ok().body(serverPublicKeyCredentialCreationOptionsResponse)
    }
}