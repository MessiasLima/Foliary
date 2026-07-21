package dev.appoutlet.foliary.core.database.typeconverter

import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Instant

class InstantTypeConverterTest {
    private val converter = InstantTypeConverter()

    @Test
    fun `should convert epoch milliseconds to instant`() {
        val epochMilliseconds = 1_726_131_200_000L

        val result = converter.toInstant(epochMilliseconds)

        result shouldBe Instant.fromEpochMilliseconds(epochMilliseconds)
    }

    @Test
    fun `should convert instant to epoch milliseconds`() {
        val instant = Instant.fromEpochMilliseconds(1_726_131_200_000L)

        val result = converter.fromInstant(instant)

        result shouldBe 1_726_131_200_000L
    }
}
