package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.provider.uuid.UuidProvider
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.capture.Capture
import dev.mokkery.matcher.capture.capture
import dev.mokkery.matcher.capture.get
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test
import kotlin.time.Instant
import kotlin.uuid.Uuid

class CreateTaskViewModelTest :
    ViewModelTest<CreateTaskViewModel, CreateTaskViewData, CreateTaskAction>() {
    private val mockTaskRepository = mock<TaskRepository>(mode = MockMode.autoUnit)
    private val mockTimeProvider = mock<TimeProvider>()
    private val mockUuidProvider = mock<UuidProvider>()
    private val startOfToday = Instant.parse("2026-07-22T00:00:00Z")
    private val expectedDisplayText = "22 Jul 2026"

    override fun createViewModel(): CreateTaskViewModel {
        every(mockTimeProvider::startOfToday) returns startOfToday
        every { mockTimeProvider.displayText(any()) } returns expectedDisplayText
        return CreateTaskViewModel(
            taskRepository = mockTaskRepository,
            timeProvider = mockTimeProvider,
            uuidProvider = mockUuidProvider
        )
    }

    @Test
    fun `TitleChanged - blank title button disabled`() = test {
        viewModel.onEvent(CreateTaskEvent.TitleChanged(""))
        currentState.saveButtonEnabled shouldBe false
    }

    @Test
    fun `TitleChanged - entering a valid title enables the save button`() = test {
        val title = "Task"

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))

        expectState {
            copy(title = title, saveButtonEnabled = true)
        }
    }

    @Test
    fun `DescriptionChanged - should update description`() = test {
        val description = "description"

        currentState.description shouldBe null

        viewModel.onEvent(CreateTaskEvent.DescriptionChanged(description))

        awaitState().description shouldBe description
    }

    @Test
    fun `BackClicked - should navigate back`() = test {
        viewModel.onEvent(CreateTaskEvent.BackClicked)

        expectSideEffect(CreateTaskAction.NavigateBack)
    }

    @Test
    fun `default state should have dueDate unset and minDueDate set to start of today`() = test {
        currentState.dueDate shouldBe null
        currentState.minDueDateMillis shouldBe startOfToday.toEpochMilliseconds()
    }

    @Test
    fun `DueDateChanged - should update dueDate`() = test {
        val dueDateMillis = 1_752_996_000_000

        viewModel.onEvent(CreateTaskEvent.DueDateChanged(dueDateMillis))

        awaitState().dueDate.also {
            it?.selectedDateMillis shouldBe dueDateMillis
            it?.selectedDateDisplayText shouldNotBe null
        }
    }

    @Test
    fun `DueDateChanged - null should clear dueDate`() = test {
        val dueDateMillis = 1_752_996_000_000
        val expectedDueDate = CreateTaskViewData.DueDateViewData.fixture(
            selectedDateMillis = dueDateMillis,
            selectedDateDisplayText = expectedDisplayText,
        )

        viewModel.onEvent(CreateTaskEvent.DueDateChanged(dueDateMillis))
        expectState { copy(dueDate = expectedDueDate) }

        viewModel.onEvent(CreateTaskEvent.DueDateChanged(null))

        awaitState().dueDate shouldBe null
    }

    @Test
    fun `SaveClicked - save task on task creation flow`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val description = "Task description"
        val taskCapture = Capture.slot<Task>()
        val creationDate = Instant.parse("2026-07-21T12:00:00Z")

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns creationDate
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        currentState shouldBe CreateTaskViewData.fixture(
            title = "",
            description = null,
            dueDate = null,
            minDueDateMillis = startOfToday.toEpochMilliseconds(),
            saveButtonEnabled = false,
        )

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.DescriptionChanged(description))
        expectState { copy(description = description) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)

        // the save button gets disabled
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().also { task ->
            task.id shouldBe id
            task.title shouldBe title
            task.description shouldBe description
            task.creationDate shouldBe creationDate
            task.dueDate shouldBe null
        }

        // Navigate back after successful saving
        awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
    }

    @Test
    fun `SaveClicked - should persist selected dueDate as end of day`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val dueDate = Instant.parse("2026-07-21T12:00:00Z")
        val dueDateMillis = dueDate.toEpochMilliseconds()
        val endOfDayDueDate = Instant.parse("2026-07-21T23:59:59.999999999Z")
        val taskCapture = Capture.slot<Task>()
        val creationDate = Instant.parse("2026-07-21T12:00:00Z")

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns creationDate
        every { mockTimeProvider.endOfDay(dueDate) } returns endOfDayDueDate
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.DueDateChanged(dueDateMillis))
        expectState {
            copy(
                title = title,
                dueDate = CreateTaskViewData.DueDateViewData.fixture(
                    selectedDateMillis = dueDateMillis,
                    selectedDateDisplayText = expectedDisplayText,
                ),
                saveButtonEnabled = true,
            )
        }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().dueDate shouldBe endOfDayDueDate

        awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
    }
}
