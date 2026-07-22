package dev.appoutlet.foliary.data.authentication.util

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline

@JvmInline
value class UserInfoMetadataKey(val key: String)

object UserInfoMetadataKeys {
    val Avatar = UserInfoMetadataKey("avatar_url")
    val Email = UserInfoMetadataKey("email")
    val EmailVerified = UserInfoMetadataKey("email_verified")
    val FullName = UserInfoMetadataKey("full_name")
    val Iss = UserInfoMetadataKey("iss")
    val Name = UserInfoMetadataKey("name")
    val PhoneVerified = UserInfoMetadataKey("phone_verified")
    val Picture = UserInfoMetadataKey("picture")
    val ProviderId = UserInfoMetadataKey("provider_id")
    val Sub = UserInfoMetadataKey("sub")
}

val UserInfo.Companion.Metadata
    get() = UserInfoMetadataKeys

fun UserInfo.metadata(key: UserInfoMetadataKey): JsonPrimitive? {
    val jsonElement = userMetadata?.get(key.key) ?: return null

    return when (jsonElement) {
        is JsonPrimitive -> {
           if (jsonElement != JsonNull) jsonElement else null
        }
        is JsonArray -> null
        is JsonObject -> null
    }
}

fun UserInfo.name(): String? {
    return (metadata(UserInfo.Metadata.Name) ?: metadata(UserInfo.Metadata.FullName))?.content
}


