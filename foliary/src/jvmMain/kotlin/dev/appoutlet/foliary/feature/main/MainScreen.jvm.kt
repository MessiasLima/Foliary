package dev.appoutlet.foliary.feature.main

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kdroidfilter.nucleus.core.runtime.Platform

actual fun getWindowDecorationPadding(): Dp {
    return when (Platform.Current) {
        Platform.Linux -> 0.dp
        Platform.Windows -> 0.dp
        Platform.MacOS -> 20.dp
        Platform.Unknown -> 0.dp
    }
}
