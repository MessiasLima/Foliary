package dev.appoutlet.foliary.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

data class Navigator(val backStack: NavBackStack<NavKey>) {
    fun navigate(destination: NavKey) {
        if (backStack.last() != destination) {
            backStack.add(destination)
        }
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}
