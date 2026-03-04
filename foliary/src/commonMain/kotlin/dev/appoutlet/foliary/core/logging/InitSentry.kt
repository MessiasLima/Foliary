@file:Suppress("ForbiddenImport")

package dev.appoutlet.foliary.core.logging

import co.touchlab.kermit.Logger
import dev.appoutlet.foliary.BuildKonfig
import io.sentry.kotlin.multiplatform.Sentry

fun initSentry() {
    // Sentry is not initialized in debug builds to avoid cluttering the logs
    if (BuildKonfig.isDebug) return

    if (BuildKonfig.sentryDsn.isBlank()) {
        Logger.withTag("InitSentry").w { "Sentry DSN is not set. Sentry will be disabled." }
        return
    }

    Sentry.init { options ->
        options.dsn = BuildKonfig.sentryDsn
        options.debug = BuildKonfig.isDebug
        options.release = "dev.appoutlet.foliary@${BuildKonfig.versionName}+${BuildKonfig.versionCode}"
        options.environment = "release"
        options.proguardUuid = BuildKonfig.versionUuid
        options.logs.enabled = true
    }
}
