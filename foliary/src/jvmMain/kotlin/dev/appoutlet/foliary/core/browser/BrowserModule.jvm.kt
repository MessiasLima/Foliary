@file:Suppress("Filename", "MatchingDeclarationName")

package dev.appoutlet.foliary.core.browser

import dev.appoutlet.foliary.core.logging.logger
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.awt.Desktop
import java.net.URI

@Module
actual object PlatformBrowserModule {
    @Single
    fun provideBrowserLauncher(): BrowserLauncher = DesktopBrowserLauncher()
}

private class DesktopBrowserLauncher : BrowserLauncher {
    private val log by logger()

    override fun launch(url: String) {
        val desktop = Desktop.getDesktop()
        val runtime = Runtime.getRuntime()

        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(url))
        } else {
            log.w { "Desktop utils not supported. Trying with xdg-open" }
            runtime.exec(arrayOf("xdg-open", url))
        }
    }
}
