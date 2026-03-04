@file:Suppress("ForbiddenImport")

package dev.appoutlet.foliary.core.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.mutableLoggerConfigInit
import co.touchlab.kermit.platformLogWriter
import dev.appoutlet.foliary.BuildKonfig

private val loggerConfig = mutableLoggerConfigInit(
    minSeverity = if (BuildKonfig.isDebug) Severity.Debug else Severity.Warn,
    logWriters = arrayOf(
        platformLogWriter(),
        SentryLogWriter(),
    ),
)

fun getLogger(tag: String): Logger {
    return Logger(loggerConfig).withTag(tag)
}
