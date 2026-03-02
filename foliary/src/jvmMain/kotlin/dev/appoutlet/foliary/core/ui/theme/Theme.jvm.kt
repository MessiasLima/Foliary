package dev.appoutlet.foliary.core.ui.theme

import androidx.compose.runtime.Composable

@Composable
actual fun SetupPlatformStatusBar(isDarkTheme: Boolean) {
    // No-op on JVM, as we don't have a status bar to manage.
}