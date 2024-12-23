package io.dflowers.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun apiHttpSecurity(
        http: ServerHttpSecurity,
        authenticationWebFilter: AuthenticationWebFilter,
    ): SecurityWebFilterChain =
        http {
            securityMatcher(PathPatternParserServerWebExchangeMatcher("/**"))
            authorizeExchange {
                authorize("/users/sign-in", permitAll)
                authorize("/users/sign-up", permitAll)
                authorize("/webjars/swagger-ui.html", permitAll)
                authorize("/webjars/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/**", authenticated)
            }
            addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            formLogin { disable() }
            logout { disable() }
            csrf { disable() }
            cors { disable() }
        }

    @Bean
    fun authenticationWebFilter(
        converter: ServerAuthenticationConverter,
        manager: ReactiveAuthenticationManager,
        authenticationFailureHandler: ServerAuthenticationFailureHandler,
    ): AuthenticationWebFilter {
        val filter = AuthenticationWebFilter(manager)
        filter.setServerAuthenticationConverter(converter)
        filter.setAuthenticationFailureHandler(authenticationFailureHandler)

        return filter
    }
}
