package authentication_project.fido.common.config

import authentication_project.fido.auth.JwtTokenCommonVerifyInterceptor
import authentication_project.fido.auth.JwtTokenDetailVerifyInterceptor
import authentication_project.fido.auth.usecase.token.TokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(JwtTokenCommonVerifyInterceptor(tokenProvider))
            .addPathPatterns("/**")
            .excludePathPatterns("/user/login", "/user/signup","/attestation/**")

        registry.addInterceptor(JwtTokenDetailVerifyInterceptor(tokenProvider, objectMapper))
            .addPathPatterns("/**")
            .excludePathPatterns("/user/login", "/user/signup","/attestation/**")
    }
}