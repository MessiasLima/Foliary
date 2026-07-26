package dev.appoutlet.foliary.data.authentication.util

import dev.appoutlet.foliary.core.testing.externalfixtures.fixture
import io.github.jan.supabase.auth.user.UserInfo
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test

class UserInfoExtensionTest {
    @Test
    fun `should return metadata primitive when key maps to primitive`() {
        val userInfo = UserInfo.fixture()

        userInfo.metadata(UserInfo.Metadata.Name) shouldBe JsonPrimitive("Messias")
    }

    @Test
    fun `should return null when metadata is missing or null`() {
        val userInfo = UserInfo.fixture(
            userMetadata = JsonObject(
                mapOf(UserInfo.Metadata.Name.key to JsonNull)
            )
        )

        userInfo.metadata(UserInfoMetadataKey("invalid field")) shouldBe null
        userInfo.metadata(UserInfo.Metadata.Name) shouldBe null
    }

    @Test
    fun `should return null when metadata key maps to array or object`() {
        val arrayUserInfo = UserInfo.fixture(
            userMetadata = JsonObject(
                mapOf(
                    UserInfo.Metadata.Name.key to JsonArray(listOf(JsonPrimitive(1)))
                )
            )
        )

        val objectUserInfo = UserInfo.fixture(
            userMetadata = JsonObject(
                mapOf(
                    UserInfo.Metadata.Name.key to JsonObject(
                        mapOf("first_name" to JsonPrimitive(1))
                    )
                )
            )
        )

        arrayUserInfo.metadata(UserInfo.Metadata.Name) shouldBe null
        objectUserInfo.metadata(UserInfo.Metadata.Name) shouldBe null
    }

    @Test
    fun `should prefer name metadata over full name`() {
        val userInfo = UserInfo.fixture(
            userMetadata = JsonObject(
                mapOf(
                    UserInfo.Metadata.Name.key to JsonPrimitive("Messias"),
                    UserInfo.Metadata.FullName.key to JsonPrimitive("Messias Junior")
                )
            )
        )

        userInfo.name() shouldBe "Messias"
    }

    @Test
    fun `should fallback to full name when name is unavailable`() {
        val userInfo = UserInfo.fixture(
            userMetadata = JsonObject(
                mapOf(
                    UserInfo.Metadata.Name.key to JsonNull,
                    UserInfo.Metadata.FullName.key to JsonPrimitive("Messias Junior")
                )
            )
        )

        userInfo.name() shouldBe "Messias Junior"
    }
}
