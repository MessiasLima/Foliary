package dev.appoutlet.foliary.core.testing

import dev.appoutlet.foliary.core.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.orbitmvi.orbit.test.OrbitTestContext
import org.orbitmvi.orbit.test.test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.time.Duration
import dev.appoutlet.foliary.core.mvi.Action as MviAction

@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModelTest<ViewModel : MviViewModel<ViewData, Action>, ViewData : Any, Action : MviAction> {
    protected lateinit var viewModel: ViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = createViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    abstract fun createViewModel(): ViewModel

    protected fun test(
        block: suspend ViewModelClassScope<ViewModel, ViewData, Action>.() -> Unit,
    ) = runTest {
        viewModel.test(this) {
            runOnCreate()
            ViewModelClassScope(testScope = this@runTest, orbitTestContext = this).block()
            cancelAndIgnoreRemainingItems()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelClassScope<ViewModel : MviViewModel<ViewData, Action>, ViewData : Any, Action : MviAction>(
    private val testScope: TestScope,
    private val orbitTestContext: OrbitTestContext<ViewData, Action, ViewModel>
) {
    fun advanceTimeBy(delayTime: Duration) = testScope.advanceTimeBy(delayTime)
    fun advanceUntilIdle() = testScope.advanceUntilIdle()
    suspend fun awaitState() = orbitTestContext.awaitState()
    suspend fun awaitSideEffect() = orbitTestContext.awaitSideEffect()
    suspend fun expectState(viewData: ViewData) = orbitTestContext.expectState(viewData)
    suspend fun expectSideEffect(action: Action) = orbitTestContext.expectSideEffect(action)
}
