package io.dflowers.user.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import io.dflowers.user.entity.User

@DgsComponent
class UserDataFetcher {
    private val testFixtures =
        listOf(
            User,
        )
}
