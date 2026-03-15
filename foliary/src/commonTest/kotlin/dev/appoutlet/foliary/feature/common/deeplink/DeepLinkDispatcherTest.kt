package dev.appoutlet.foliary.feature.common.deeplink

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DeepLinkDispatcherTest {
    @Test
    fun `should dispatch deeplink`() = runTest {
        val deeplink = Deeplink.fixture()

        DeepLinkDispatcher.dispatch(deeplink)

        DeepLinkDispatcher.deeplinks.first() shouldBe deeplink
    }

    @Test
    fun `should handle multiple deeplinks`() = runTest {
        val deeplink1 = Deeplink.fixture(path = "path1")
        val deeplink2 = Deeplink.fixture(path = "path2")

        DeepLinkDispatcher.dispatch(deeplink1)
        DeepLinkDispatcher.deeplinks.first() shouldBe deeplink1

        DeepLinkDispatcher.dispatch(deeplink2)
        DeepLinkDispatcher.deeplinks.first() shouldBe deeplink2
    }
}
