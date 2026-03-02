package dev.appoutlet.foliary.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface Navigator {
    fun navigate(destination: NavKey)
    fun goBack()
}

data class AppNavigator(val backStack: NavBackStack<NavKey>) : Navigator {
    override fun navigate(destination: NavKey) {
        if (backStack.last() != destination) {
            backStack.add(destination)
        }
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}
