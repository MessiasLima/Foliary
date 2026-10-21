@file:Suppress("Filename", "MatchingDeclarationName")

package dev.appoutlet.foliary.core.browser

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Module
actual object PlatformBrowserModule {
    @Single
    fun provideBrowserLauncher(): BrowserLauncher = IosBrowserLauncher()
}

private class IosBrowserLauncher : BrowserLauncher {
    override fun launch(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}
