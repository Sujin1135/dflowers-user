package io.dflowers.user.dto

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

data class BearerToken(
    private val token: String,
) : AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
    override fun getCredentials() = token

    override fun getPrincipal() = token
}
