@file:Suppress("ForbiddenImport")

package dev.appoutlet.foliary.core.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import dev.appoutlet.foliary.BuildKonfig
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.log.SentryLogBuilder
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb

class SentryLogWriter : LogWriter() {
    override fun isLoggable(tag: String, severity: Severity) = BuildKonfig.isDebug.not()

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val sentryLevel = sentryLevelMapping[severity]

        addBreadcrumb(sentryLevel, message, tag)

        trackSentryLog(sentryLevel, message, tag)

        // Capture exception if present
        throwable?.let {
            Sentry.captureException(throwable) { scope ->
                scope.level = sentryLevel
                scope.setTag("isDebug", BuildKonfig.isDebug.toString())
                scope.setTag("versionCode", BuildKonfig.versionCode.toString())
                scope.setTag("versionName", BuildKonfig.versionName)
                scope.setTag("versionUuid", BuildKonfig.versionUuid)
            }
        }
    }

    private fun trackSentryLog(
        sentryLevel: SentryLevel?,
        message: String,
        tag: String,
    ) {
        val logBuilder: SentryLogBuilder.() -> Unit = {
            message("[$tag] $message")
        }

        when (sentryLevel) {
            SentryLevel.DEBUG -> Sentry.logger.debug(logBuilder)
            SentryLevel.INFO -> Sentry.logger.info(logBuilder)
            SentryLevel.WARNING -> Sentry.logger.warn(logBuilder)
            SentryLevel.ERROR -> Sentry.logger.error(logBuilder)
            SentryLevel.FATAL -> Sentry.logger.fatal(logBuilder)
            null -> Sentry.logger.warn(logBuilder)
        }
    }

    private fun addBreadcrumb(level: SentryLevel?, message: String, tag: String) {
        val breadcrumb = Breadcrumb().apply {
            this.level = level
            this.type = level?.name?.lowercase()
            this.message = message
            this.category = tag
        }

        Sentry.addBreadcrumb(breadcrumb)
    }
}

private val sentryLevelMapping = mapOf(
    Severity.Verbose to SentryLevel.DEBUG,
    Severity.Debug to SentryLevel.DEBUG,
    Severity.Info to SentryLevel.INFO,
    Severity.Warn to SentryLevel.WARNING,
    Severity.Error to SentryLevel.ERROR,
    Severity.Assert to SentryLevel.FATAL
)
