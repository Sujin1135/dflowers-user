package io.dflowers.user.dto

import org.springframework.security.core.GrantedAuthority

class Principal(
    val username: String,
    val authorities: Collection<GrantedAuthority>,
) {
    companion object {
        fun of(
            username: String,
            authorities: Collection<GrantedAuthority>,
        ) = Principal(username, authorities)
    }
}
