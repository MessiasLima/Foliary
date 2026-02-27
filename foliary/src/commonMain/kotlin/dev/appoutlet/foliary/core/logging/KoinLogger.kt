package dev.appoutlet.foliary.core.logging

import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.logger.Logger

fun getKoinLogger(): Logger {
    return KermitKoinLogger(getLogger("Koin"))
}