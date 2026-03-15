import io.kotest.matchers.shouldBe
import java.net.URI
import kotlin.test.Test

class DeepLinkHandlerTest {
    @Test
    fun `should convert URI to Deeplink`() {
        val uri = URI("foliary://host/path?param1=value1&param2=value2")
        val deeplink = uri.toDeeplink()

        deeplink.schema shouldBe "foliary"
        deeplink.host shouldBe "host"
        deeplink.path shouldBe "/path"
        deeplink.queryParameters shouldBe mapOf(
            "param1" to "value1",
            "param2" to "value2"
        )
    }

    @Test
    fun `should convert URI with fragment to Deeplink`() {
        val uri = URI("foliary://host/path?param1=value1#param2=value2")
        val deeplink = uri.toDeeplink()

        deeplink.schema shouldBe "foliary"
        deeplink.host shouldBe "host"
        deeplink.path shouldBe "/path"
        deeplink.queryParameters shouldBe mapOf(
            "param1" to "value1",
            "param2" to "value2"
        )
    }

    @Test
    fun `should convert URI with only fragment to Deeplink`() {
        val uri = URI("foliary://host/path#param1=value1")
        val deeplink = uri.toDeeplink()

        deeplink.schema shouldBe "foliary"
        deeplink.host shouldBe "host"
        deeplink.path shouldBe "/path"
        deeplink.queryParameters shouldBe mapOf(
            "param1" to "value1"
        )
    }

    @Test
    fun `should handle URI without query or fragment`() {
        val uri = URI("foliary://host/path")
        val deeplink = uri.toDeeplink()

        deeplink.schema shouldBe "foliary"
        deeplink.host shouldBe "host"
        deeplink.path shouldBe "/path"
        deeplink.queryParameters shouldBe emptyMap()
    }

    @Test
    fun `should handle URI with empty parts`() {
        val uri = URI("foliary:///path")
        val deeplink = uri.toDeeplink()

        deeplink.schema shouldBe "foliary"
        deeplink.host shouldBe ""
        deeplink.path shouldBe "/path"
        deeplink.queryParameters shouldBe emptyMap()
    }

    @Test
    fun `should handle query parameter without value`() {
        val uri = URI("foliary://host/path?param1&param2=value2")
        val deeplink = uri.toDeeplink()

        deeplink.queryParameters shouldBe mapOf(
            "param1" to "",
            "param2" to "value2"
        )
    }
}
