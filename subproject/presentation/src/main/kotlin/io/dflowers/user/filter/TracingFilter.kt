package io.dflowers.user.filter

import brave.propagation.CurrentTraceContext
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono

const val KEY_TXID = "traceId"
const val KEY_SPID = "spanId"

@Component
@Order(1)
class TracingFilter(
    private val currentTraceContext: CurrentTraceContext,
) : WebFilter {
    // ContextRegistry에 ThreadLocal 접근자를 등록하고 Reactor 컨텍스트와 MDC 간의 상호 작용을 설정한다.
    init {
        // Reactor 연산자가 실행되는 동안 컨텍스트를 자동으로 전파하도록 설정
        Hooks.enableAutomaticContextPropagation()
    }

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val traceId = currentTraceContext.get().traceId()
        val spanId = currentTraceContext.get().spanId()
        MDC.put(KEY_TXID, traceId.toString())
        MDC.put(KEY_SPID, spanId.toString())
        return chain.filter(exchange)
    }
}
