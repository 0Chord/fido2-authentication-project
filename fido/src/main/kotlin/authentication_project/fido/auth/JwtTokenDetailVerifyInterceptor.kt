package authentication_project.fido.auth

import authentication_project.fido.auth.usecase.token.TokenProvider
import authentication_project.fido.common.exception.InvalidTokenException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtTokenDetailVerifyInterceptor(
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper
): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.getHeader("Authorization")?.let { token ->
            {
                val accessToken = token.split(" ")[1]
                accessToken.let {
                    val requestBody = request.reader.lines().reduce { acc, s -> acc + s }.orElse("")
                    val jsonNode = objectMapper.readTree(requestBody)
                    val userId = jsonNode.get("userId").asText().toLong()
                    if (tokenProvider.getUserId(accessToken) != userId) {
                        throw InvalidTokenException("토큰이 유효하지 않습니다")
                    }
                }
            }
        }
        return true
    }
}