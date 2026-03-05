package dev.appoutlet.foliary.core.ui

actual val isApplePlatform: Boolean = System.getProperty("os.name").contains("Mac", ignoreCase = true)
