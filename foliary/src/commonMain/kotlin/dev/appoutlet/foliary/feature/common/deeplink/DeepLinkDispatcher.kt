package dev.appoutlet.foliary.feature.common.deeplink

import dev.appoutlet.foliary.core.logging.logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object DeepLinkDispatcher {
    private val log by logger()

    private val channel = Channel<Deeplink>(capacity = 1)
    val deeplinks = channel.receiveAsFlow()

    fun dispatch(deeplink: Deeplink) {
        log.i { "deeplink received: $deeplink" }
        channel.trySend(deeplink)
    }
}
