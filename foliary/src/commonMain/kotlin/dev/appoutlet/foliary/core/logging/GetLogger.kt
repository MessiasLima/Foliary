package dev.appoutlet.foliary.core.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.mutableLoggerConfigInit
import co.touchlab.kermit.platformLogWriter

private val loggerConfig = mutableLoggerConfigInit(
    logWriters = arrayOf(
        platformLogWriter(),
    ),
)

fun getLogger(tag: String): Logger {
    return Logger(loggerConfig).withTag(tag)
}