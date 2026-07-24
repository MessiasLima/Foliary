package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.provider.uuid.UuidProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.capture.Capture
import dev.mokkery.matcher.capture.capture
import dev.mokkery.matcher.capture.get
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Instant
import kotlin.uuid.Uuid

class CreateTaskViewModelTest :
    ViewModelTest<CreateTaskViewModel, CreateTaskViewData, CreateTaskAction>() {
    private val mockTaskRepository = mock<TaskRepository>(mode = MockMode.autoUnit)
    private val mockTimeProvider = mock<TimeProvider>()
    private val mockUuidProvider = mock<UuidProvider>()

    override fun createViewModel() = CreateTaskViewModel(
        taskRepository = mockTaskRepository,
        timeProvider = mockTimeProvider,
        uuidProvider = mockUuidProvider
    )

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
    fun `SaveClicked - save task on task creation flow`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val description = "Task description"
        val taskCapture = Capture.slot<Task>()
        val creationDate = Instant.parse("2026-07-21T12:00:00Z")

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns creationDate
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        currentState shouldBe CreateTaskViewData()

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
        }

        // Navigate back after successful saving
        awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
    }
}
