package dev.appoutlet.foliary.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavKey

val LocalNavigator = compositionLocalOf<Navigator> {
    object : Navigator {
        override fun navigate(destination: NavKey) {
            TODO("No implementation provided for LocalNavigator")
        }

        override fun goBack() {
            TODO("No implementation provided for LocalNavigator")
        }
    }
}
