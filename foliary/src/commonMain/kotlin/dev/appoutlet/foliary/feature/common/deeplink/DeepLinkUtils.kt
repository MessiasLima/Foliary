package dev.appoutlet.foliary.feature.common.deeplink

fun String.getAdditionalQueryParameters(): Map<String, String> {
    return this.split('#').getOrNull(1)
        ?.split("&")
        ?.associate {
            val arg = it.split("=", ignoreCase = false, limit = 2)
            arg[0] to arg.getOrElse(1) { "" }
        }
        ?.filter { it.key.isNotBlank() && it.value.isNotBlank() } ?: emptyMap()
}
