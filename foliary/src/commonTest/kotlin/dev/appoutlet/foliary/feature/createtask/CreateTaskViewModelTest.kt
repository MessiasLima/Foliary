package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.time.TimeProvider
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.capture.Capture
import dev.mokkery.matcher.capture.capture
import dev.mokkery.matcher.capture.get
import dev.mokkery.mock
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test
import kotlin.time.Instant
import kotlin.uuid.Uuid

class CreateTaskViewModelTest : ViewModelTest<CreateTaskViewModel, CreateTaskViewData, CreateTaskAction>() {
    private val now = Instant.parse("2026-07-21T12:00:00Z")
    private val mockTaskRepository = mock<TaskRepository>(mode = MockMode.autoUnit)
    private val mockTimeProvider = mock<TimeProvider>()

    override fun createViewModel() = CreateTaskViewModel(
        taskRepository = mockTaskRepository,
        timeProvider = mockTimeProvider,
    ).apply {
        every { mockTimeProvider.now() } returns now
    }

    @Test
    fun `initial state is empty and not saving`() = test {
        viewModel.container.stateFlow.value shouldBe CreateTaskViewData()
    }

    @Test
    fun `blank title on save shows validation error`() = test {
        viewModel.onEvent(CreateTaskEvent.SaveClicked)

        expectState(CreateTaskViewData(showTitleError = true))
    }

    @Test
    fun `blank title on save does not call repository`() = test {
        viewModel.onEvent(CreateTaskEvent.SaveClicked)

        expectState(CreateTaskViewData(showTitleError = true))
        verifyNoMoreCalls(mockTaskRepository)
    }

    @Test
    fun `entering a valid title clears the title error`() = test {
        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        expectState(CreateTaskViewData(showTitleError = true))

        viewModel.onEvent(CreateTaskEvent.TitleChanged("Task"))
        expectState(CreateTaskViewData(title = "Task", showTitleError = false))
    }

    @Test
    fun `valid title saves a task`() = test {
        val slot = Capture.slot<Task>()
        everySuspend { mockTaskRepository.save(capture(slot)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged("Task title"))
        expectState(CreateTaskViewData(title = "Task title"))

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        expectState(CreateTaskViewData(title = "Task title", isSaving = true))

        verifySuspend { mockTaskRepository.save(any()) }
        with(slot.get()) {
            title shouldBe "Task title"
            description shouldBe null
            creationDate shouldBe now
            dueDate shouldBe null
            completionDate shouldBe null
            priority shouldBe null
            url shouldBe null
            location shouldBe null
            id shouldNotBe Uuid.parse("00000000-0000-0000-0000-000000000000")
        }
        expectSideEffect(CreateTaskAction.NavigateBack)
    }

    @Test
    fun `valid title plus description saves both values`() = test {
        val slot = Capture.slot<Task>()
        everySuspend { mockTaskRepository.save(capture(slot)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged("Task title"))
        expectState(CreateTaskViewData(title = "Task title"))

        viewModel.onEvent(CreateTaskEvent.DescriptionChanged("Description"))
        expectState(CreateTaskViewData(title = "Task title", description = "Description"))

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        expectState(CreateTaskViewData(title = "Task title", description = "Description", isSaving = true))

        verifySuspend { mockTaskRepository.save(any()) }
        with(slot.get()) {
            title shouldBe "Task title"
            description shouldBe "Description"
            creationDate shouldBe now
        }
        expectSideEffect(CreateTaskAction.NavigateBack)
    }

    @Test
    fun `blank description is saved as null`() = test {
        val slot = Capture.slot<Task>()
        everySuspend { mockTaskRepository.save(capture(slot)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged("Task title"))
        expectState(CreateTaskViewData(title = "Task title"))

        viewModel.onEvent(CreateTaskEvent.DescriptionChanged("   "))
        expectState(CreateTaskViewData(title = "Task title", description = "   "))

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        expectState(CreateTaskViewData(title = "Task title", description = "   ", isSaving = true))

        slot.get().description shouldBe null
        expectSideEffect(CreateTaskAction.NavigateBack)
    }

    @Test
    fun `back event emits NavigateBack without saving`() = test {
        viewModel.onEvent(CreateTaskEvent.BackClicked)

        expectSideEffect(CreateTaskAction.NavigateBack)
        verifyNoMoreCalls(mockTaskRepository)
    }
}
