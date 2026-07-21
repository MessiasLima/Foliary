package dev.appoutlet.foliary.core.database.typeconverter

import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.uuid.Uuid

class UuidTypeConverterTest {
    private val converter = UuidTypeConverter()

    @Test
    fun `should convert uuid to string`() {
        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")

        val result = converter.fromUuid(uuid)

        result shouldBe "123e4567-e89b-12d3-a456-426614174000"
    }

    @Test
    fun `should convert string to uuid`() {
        val string = "123e4567-e89b-12d3-a456-426614174000"

        val result = converter.toUuid(string)

        result shouldBe Uuid.parse(string)
    }
}
