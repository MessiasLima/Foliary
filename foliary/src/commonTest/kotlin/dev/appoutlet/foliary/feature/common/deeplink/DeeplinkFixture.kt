package dev.appoutlet.foliary.feature.common.deeplink

fun Deeplink.Companion.fixture(
    schema: String = "foliary",
    host: String = "auth",
    path: String = "/callback",
    queryParameters: Map<String, String> = mapOf("code" to "123456")
) = Deeplink(
    schema = schema,
    host = host,
    path = path,
    queryParameters = queryParameters
)
