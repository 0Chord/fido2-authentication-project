package authentication_project.fido.fido.api

import authentication_project.fido.common.dto.BaseResponse
import authentication_project.fido.fido.dto.AuthenticationServerPublicKeyCredential
import authentication_project.fido.fido.dto.AuthenticationServerPublicKeyCredentialGetOptionsRequest
import authentication_project.fido.fido.dto.AuthenticationServerPublicKeyCredentialGetOptionsResponse
import authentication_project.fido.fido.dto.LoginResponse
import authentication_project.fido.fido.usecase.AssertionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/assertion")
class AuthenticateController(
    private val assertionService: AssertionService
) {
    @PostMapping("/options")
    fun authenticate(@Valid @RequestBody authenticationServerPublicKeyCredentialGetOptionsRequest: AuthenticationServerPublicKeyCredentialGetOptionsRequest)
            : ResponseEntity<AuthenticationServerPublicKeyCredentialGetOptionsResponse> {
        val creationOptions =
            assertionService.createCredentialCreationOptions(authenticationServerPublicKeyCredentialGetOptionsRequest)
        return ResponseEntity.ok().body(creationOptions)
    }

    @PostMapping("/result")
    fun authenticateResult(@Valid @RequestBody authenticationServerPublicKeyCredential: AuthenticationServerPublicKeyCredential): ResponseEntity<BaseResponse<LoginResponse>> {
        val loginResponse: LoginResponse = assertionService.verifyAssertion(authenticationServerPublicKeyCredential)
        return ResponseEntity.ok().body(
            BaseResponse<LoginResponse>(
                resultCode = HttpStatus.OK.value(),
                resultMessage = HttpStatus.OK.reasonPhrase,
                resultData = loginResponse
            )
        )
    }
}