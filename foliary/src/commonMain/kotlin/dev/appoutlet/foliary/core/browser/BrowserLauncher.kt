package dev.appoutlet.foliary.core.browser

import org.koin.core.annotation.Provided

@Provided
interface BrowserLauncher {
    fun launch(url: String)
}
