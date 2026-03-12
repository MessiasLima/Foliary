package dev.appoutlet.foliary.feature.common.deeplink

data class Deeplink(
    val schema: String,
    val host: String,
    val path: String,
    val queryParameters: Map<String, String>
)
