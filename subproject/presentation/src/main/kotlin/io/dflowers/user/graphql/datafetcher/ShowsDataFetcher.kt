package io.dflowers.user.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import reactor.core.publisher.Flux
import java.time.OffsetDateTime

@DgsComponent
class ShowsDataFetcher {
    private val now = OffsetDateTime.now()
    private val shows =
        listOf(
            Show(
                "당신 거기 있어줄래요?",
                "sattlub123@gmail.com",
                "최민규",
                now,
                now,
            ),
            Show(
                "Stranger Things",
                "tnwls8798@hanmail.net",
                "유수진",
                now,
                now,
            ),
            Show(
                "멍마들",
                "otti@dogs.com",
                "김오띠",
                now,
                now,
            ),
            Show(
                "거기서도 행복하고 많이 기다려줘",
                "pola@dogs.com",
                "폴라",
                now,
                now,
            ),
        )

    @PreAuthorize("isAuthenticated()")
    @DgsQuery
    fun shows(
        @InputArgument nameFilter: String?,
    ): Flux<Show> =
        if (nameFilter != null) {
            Flux.just(shows.filter { it.name.contains(nameFilter) }[0])
        } else {
            Flux.just(shows[0])
        }

    data class Show(
        val id: String,
        val email: String,
        val name: String,
        val created: OffsetDateTime,
        val modified: OffsetDateTime,
    )
}
