package authentication_project.fido.user.api

import authentication_project.fido.common.dto.BaseResponse
import authentication_project.fido.user.dto.SignupUserDto
import authentication_project.fido.user.dto.SignupUserRequest
import authentication_project.fido.user.usecase.signup.SignupUsecase
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
@RequestMapping("/user")
class SignupController(
    private val signupUsecase: SignupUsecase
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupUserRequest: SignupUserRequest): ResponseEntity<BaseResponse<*>> {
        signupUserRequest.checkPassword()
        val signupUserDto =
            SignupUserDto(signupUserRequest.email, signupUserRequest.password, signupUserRequest.nickname)
        signupUsecase.signup(signupUserDto)
        return ResponseEntity.ok(BaseResponse<Unit>(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase))
    }
}