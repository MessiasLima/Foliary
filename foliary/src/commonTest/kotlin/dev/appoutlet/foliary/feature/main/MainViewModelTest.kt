package dev.appoutlet.foliary.feature.main

import dev.appoutlet.foliary.core.testing.ViewModelTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MainViewModelTest : ViewModelTest<MainViewModel, MainViewData, MainAction>() {
    override fun createViewModel() = MainViewModel()

    @Test
    fun `should select today tab by default`() {
        test {
            currentState.selectedTab shouldBe MainTab.Today
        }
    }

    @Test
    fun `should switch to upcoming tab`() {
        test {
            viewModel.onTabSelected(MainTab.Upcoming)

            expectState { copy(selectedTab = MainTab.Upcoming) }
        }
    }

    @Test
    fun `should switch to profile tab`() {
        test {
            viewModel.onTabSelected(MainTab.Profile)

            expectState { copy(selectedTab = MainTab.Profile) }
        }
    }
}
