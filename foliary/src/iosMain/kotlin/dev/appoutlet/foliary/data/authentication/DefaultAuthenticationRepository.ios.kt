package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.BuildKonfig

actual fun getRedirectUrl() =
    if (BuildKonfig.isDebug) "foliary://auth" else "https://foliary.appoutlet.dev/auth"
