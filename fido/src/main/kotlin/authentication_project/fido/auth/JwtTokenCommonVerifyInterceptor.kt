package authentication_project.fido.auth

import authentication_project.fido.auth.usecase.token.TokenProvider
import authentication_project.fido.common.exception.InvalidTokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtTokenCommonVerifyInterceptor(
    private val tokenProvider: TokenProvider
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.getHeader("Authorization")?.let { token ->
            {
                val accessToken = token.split(" ")[1]
                accessToken.let {
                    if (!tokenProvider.verifyToken(it)) {
                        throw InvalidTokenException("토큰이 유효하지 않습니다")
                    }
                }
            }
        }

        return true
    }
}