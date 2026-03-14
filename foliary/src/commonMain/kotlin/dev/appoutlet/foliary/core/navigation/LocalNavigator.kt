package dev.appoutlet.foliary.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavKey

val LocalNavigator = compositionLocalOf<Navigator> {
    object : Navigator {
        override fun navigate(destination: NavKey) {
            error("No implementation provided for LocalNavigator")
        }

        override fun goBack() {
            error("No implementation provided for LocalNavigator")
        }

        override fun setRoot(destination: NavKey) {
            error("No implementation provided for LocalNavigator")
        }
    }
}
