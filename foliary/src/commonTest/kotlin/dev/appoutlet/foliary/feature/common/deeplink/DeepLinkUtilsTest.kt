package dev.appoutlet.foliary.feature.common.deeplink

import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DeepLinkUtilsTest {

    @Test
    fun `should return empty map when no fragment is present`() {
        "https://example.com".getAdditionalQueryParameters().shouldBeEmpty()
    }

    @Test
    fun `should return empty map when fragment is empty`() {
        "https://example.com#".getAdditionalQueryParameters().shouldBeEmpty()
    }

    @Test
    fun `should parse single parameter from fragment`() {
        val result = "https://example.com#access_token=123".getAdditionalQueryParameters()
        result shouldBe mapOf("access_token" to "123")
    }

    @Test
    fun `should parse multiple parameters from fragment`() {
        val result = "https://example.com#access_token=123&refresh_token=456&type=bearer"
            .getAdditionalQueryParameters()
        
        result shouldBe mapOf(
            "access_token" to "123",
            "refresh_token" to "456",
            "type" to "bearer"
        )
    }

    @Test
    fun `should filter out blank keys or values`() {
        val result = "https://example.com#valid=ok&empty_val=& =blank_key&=no_key"
            .getAdditionalQueryParameters()
        
        result shouldBe mapOf("valid" to "ok")
    }

    @Test
    fun `should handle parameters with multiple equals signs`() {
        val result = "https://example.com#key=value=with=equals".getAdditionalQueryParameters()
        result shouldBe mapOf("key" to "value=with=equals")
    }

    @Test
    fun `should handle malformed fragments`() {
        "https://example.com#&&&".getAdditionalQueryParameters().shouldBeEmpty()
        "https://example.com#===".getAdditionalQueryParameters().shouldBeEmpty()
    }
}
