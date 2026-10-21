@file:Suppress("Filename", "MatchingDeclarationName")

package dev.appoutlet.foliary.core.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual object PlatformBrowserModule {
    @Single
    fun provideBrowserLauncher(context: Context): BrowserLauncher = AndroidBrowserLauncher(context)
}

private class AndroidBrowserLauncher(private val context: Context) : BrowserLauncher {
    override fun launch(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
