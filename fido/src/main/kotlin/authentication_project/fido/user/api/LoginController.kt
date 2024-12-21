package authentication_project.fido.user.api

import authentication_project.fido.common.dto.BaseResponse
import authentication_project.fido.user.dto.LoginRequest
import authentication_project.fido.user.dto.LoginResponse
import authentication_project.fido.user.usecase.login.LoginUsecase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/user")
class LoginController(
    private val loginUsecase: LoginUsecase
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<BaseResponse<LoginResponse>> {
        val loginResponse: LoginResponse = loginUsecase.login(loginRequest)
        return ResponseEntity.ok(BaseResponse<LoginResponse>(200, "OK", loginResponse))
    }
}