package dev.appoutlet.foliary.android

import android.app.Application
import dev.appoutlet.foliary.core.logging.initSentry

class FoliaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initSentry()
    }
}
