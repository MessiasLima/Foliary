package dev.appoutlet.foliary.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.mp.KoinPlatform

/**
 * CompositionLocal for providing Analytics instance throughout the composable hierarchy.
 * Allows automatic screen tracking in the Screen component without explicit parameter passing.
 *
 * Uses staticCompositionLocalOf since Analytics is a singleton that doesn't change at runtime.
 * Defaults to DebugAnalytics for safety if not provided.
 */
val LocalAnalytics = staticCompositionLocalOf<Analytics> { KoinPlatform.getKoin().get() }
