package dev.appoutlet.foliary.feature.common.deeplink

import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeepLinkDispatcherTest {

    @Test
    fun `should handle multiple deeplinks`() = runTest {
        val deeplink1 = Deeplink.fixture(path = "path1")
        val deeplink2 = Deeplink.fixture(path = "path2")
        val received = mutableListOf<Deeplink>()

        val collectorJob = DeepLinkDispatcher.deeplinks.onEach {
            received.add(it)
        }.launchIn(this)

        DeepLinkDispatcher.dispatch(deeplink1)

        advanceUntilIdle()

        DeepLinkDispatcher.dispatch(deeplink2)

        advanceUntilIdle()

        received.shouldContainExactly(deeplink1, deeplink2)

        collectorJob.cancel()
    }
}
