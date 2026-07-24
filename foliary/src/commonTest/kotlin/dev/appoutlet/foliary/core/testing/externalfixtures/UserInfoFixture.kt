package dev.appoutlet.foliary.core.testing.externalfixtures

import io.github.jan.supabase.auth.user.Identity
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserMfaFactor
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.time.Instant

private val fixtureInstant = Instant.parse("2026-07-24T12:00:00Z")

fun UserInfo.Companion.fixture(
    appMetadata: JsonObject? = JsonObject(mapOf("provider" to JsonPrimitive("google"))),
    aud: String = "authenticated",
    confirmationSentAt: Instant? = fixtureInstant,
    confirmedAt: Instant? = fixtureInstant,
    createdAt: Instant? = fixtureInstant,
    email: String? = "user@example.com",
    emailConfirmedAt: Instant? = fixtureInstant,
    factors: List<UserMfaFactor> = listOf(UserMfaFactor.fixture()),
    id: String = "user-id",
    identities: List<Identity>? = listOf(Identity.fixture(userId = id, email = email ?: "user@example.com")),
    lastSignInAt: Instant? = fixtureInstant,
    phone: String? = "+5511999999999",
    role: String? = "authenticated",
    updatedAt: Instant? = fixtureInstant,
    userMetadata: JsonObject? = JsonObject(
        mapOf(
            "email" to JsonPrimitive(email ?: "user@example.com"),
            "full_name" to JsonPrimitive("Messias Junior"),
            "name" to JsonPrimitive("Messias"),
        )
    ),
    phoneChangeSentAt: Instant? = fixtureInstant,
    newPhone: String? = "+5511888888888",
    emailChangeSentAt: Instant? = fixtureInstant,
    newEmail: String? = "new.user@example.com",
    invitedAt: Instant? = fixtureInstant,
    recoverySentAt: Instant? = fixtureInstant,
    phoneConfirmedAt: Instant? = fixtureInstant,
    actionLink: String? = "https://foliary.appoutlet.dev/auth/confirm",
    isAnonymous: Boolean? = false,
    isSSOUser: Boolean? = false,
    bannedUntil: Instant? = fixtureInstant,
    deletedAt: Instant? = fixtureInstant,
) = UserInfo(
    appMetadata = appMetadata,
    aud = aud,
    confirmationSentAt = confirmationSentAt,
    confirmedAt = confirmedAt,
    createdAt = createdAt,
    email = email,
    emailConfirmedAt = emailConfirmedAt,
    factors = factors,
    id = id,
    identities = identities,
    lastSignInAt = lastSignInAt,
    phone = phone,
    role = role,
    updatedAt = updatedAt,
    userMetadata = userMetadata,
    phoneChangeSentAt = phoneChangeSentAt,
    newPhone = newPhone,
    emailChangeSentAt = emailChangeSentAt,
    newEmail = newEmail,
    invitedAt = invitedAt,
    recoverySentAt = recoverySentAt,
    phoneConfirmedAt = phoneConfirmedAt,
    actionLink = actionLink,
    isAnonymous = isAnonymous,
    isSSOUser = isSSOUser,
    bannedUntil = bannedUntil,
    deletedAt = deletedAt,
)

private fun Identity.Companion.fixture(
    id: String = "identity-id",
    identityData: JsonObject = JsonObject(mapOf("email" to JsonPrimitive("user@example.com"))),
    identityId: String? = "provider-identity-id",
    lastSignInAt: String? = "2026-07-24T12:00:00Z",
    updatedAt: String? = "2026-07-24T12:00:00Z",
    createdAt: String? = "2026-07-24T12:00:00Z",
    provider: String = "google",
    userId: String = "user-id",
    email: String = "user@example.com",
) = Identity(
    id = id,
    identityData = if (identityData.isEmpty()) JsonObject(mapOf("email" to JsonPrimitive(email))) else identityData,
    identityId = identityId,
    lastSignInAt = lastSignInAt,
    updatedAt = updatedAt,
    createdAt = createdAt,
    provider = provider,
    userId = userId,
)

private fun UserMfaFactor.Companion.fixture(
    id: String = "factor-id",
    createdAt: Instant = fixtureInstant,
    updatedAt: Instant = fixtureInstant,
    status: String = "verified",
    friendlyName: String? = "Authenticator App",
    factorType: String = "totp",
    lastChallengedAt: Instant? = fixtureInstant,
) = UserMfaFactor(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status,
    friendlyName = friendlyName,
    factorType = factorType,
    lastChallengedAt = lastChallengedAt,
)
