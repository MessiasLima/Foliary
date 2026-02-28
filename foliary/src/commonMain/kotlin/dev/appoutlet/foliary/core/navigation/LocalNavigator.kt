package dev.appoutlet.foliary.core.navigation

import androidx.compose.runtime.compositionLocalOf

val LocalNavigator = compositionLocalOf<Navigator> { error("No Navigator provided") }
