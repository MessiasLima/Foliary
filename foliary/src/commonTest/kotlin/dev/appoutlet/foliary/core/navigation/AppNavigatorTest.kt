package dev.appoutlet.foliary.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlin.test.Test

class AppNavigatorTest {

    @Test
    fun `should navigate to new destination when it is different from last`() {
        val backStack: NavBackStack<NavKey> = NavBackStack(TestNavKey)
        val navigator = AppNavigator(backStack)

        navigator.navigate(OtherNavKey)

        backStack.last() shouldBe OtherNavKey
    }

    @Test
    fun `should not navigate to same destination when it is already at the top`() {
        val backStack: NavBackStack<NavKey> = NavBackStack(TestNavKey)
        val navigator = AppNavigator(backStack)

        navigator.navigate(TestNavKey)

        backStack.size shouldBe 1
    }

    @Test
    fun `should set root and clear existing stack`() {
        val backStack: NavBackStack<NavKey> = NavBackStack(TestNavKey, OtherNavKey)
        val navigator = AppNavigator(backStack)

        navigator.setRoot(ThirdNavKey)

        backStack.size shouldBe 1
        backStack.last() shouldBe ThirdNavKey
    }

    @Test
    fun `should go back to previous destination`() {
        val backStack: NavBackStack<NavKey> = NavBackStack(TestNavKey, OtherNavKey)
        val navigator = AppNavigator(backStack)

        navigator.goBack()

        backStack.size shouldBe 1
        backStack.last() shouldBe TestNavKey
    }
}

@Serializable
private data object TestNavKey : NavKey

@Serializable
private data object OtherNavKey : NavKey

private data object ThirdNavKey : NavKey
