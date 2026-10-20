package dev.appoutlet.foliary.feature.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable

@Composable
actual fun getWindowDecorationPadding() = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
