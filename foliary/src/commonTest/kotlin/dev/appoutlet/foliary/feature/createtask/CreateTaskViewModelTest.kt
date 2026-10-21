package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.provider.uuid.UuidProvider
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
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
            taskId = null,
            taskRepository = mockTaskRepository,
            timeProvider = mockTimeProvider,
            uuidProvider = mockUuidProvider
        )
    }

    @Test
    fun `TitleChanged - should update title and save button enabled state`() = test {
        val title = "Task"

        viewModel.onEvent(CreateTaskEvent.TitleChanged(""))
        currentState.saveButtonEnabled shouldBe false

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
    fun `UrlChanged - should update url`() = test {
        val url = "example.com"

        currentState.url.value shouldBe ""

        viewModel.onEvent(CreateTaskEvent.UrlChanged(url))

        awaitState().url.value shouldBe url
    }

    @Test
    fun `BackClicked - should navigate back`() = test {
        viewModel.onEvent(CreateTaskEvent.BackClicked)

        expectSideEffect(CreateTaskAction.NavigateBack)
    }

    @Test
    fun `default state should have expected values`() = test {
        currentState shouldBe CreateTaskViewData(
            id = null,
            title = "",
            description = null,
            url = CreateTaskViewData.Url.fixture(),
            priority = Priority.MEDIUM,
            dueDate = null,
            minDueDateMillis = startOfToday.toEpochMilliseconds(),
            saveButtonEnabled = false,
        )
    }

    @Test
    fun `PriorityChanged - should update priority`() = test {
        viewModel.onEvent(CreateTaskEvent.PriorityChanged(Priority.HIGH))
        expectState { copy(priority = Priority.HIGH) }
    }

    @Test
    fun `should load saved task on init`() {
        val taskId = Uuid.random()
        val savedTask = Task.fixture(id = taskId, priority = Priority.HIGHEST)
        val taskCapture = Capture.slot<Task>()
        val endOfDayDueDate = Instant.parse("2026-08-21T23:59:59.999999999Z")

        everySuspend { mockTaskRepository.findById(taskId) } returns savedTask
        every { mockTimeProvider.endOfDay(any()) } returns endOfDayDueDate
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel = CreateTaskViewModel(
            taskId = taskId.toString(),
            taskRepository = mockTaskRepository,
            timeProvider = mockTimeProvider,
            uuidProvider = mockUuidProvider,
        )

        test {
            expectState {
                copy(
                    id = taskId.toString(),
                    title = savedTask.title,
                    description = savedTask.description,
                    url = CreateTaskViewData.Url(value = savedTask.url.orEmpty()),
                    priority = savedTask.priority,
                    dueDate = savedTask.dueDate?.let { dueDate ->
                        CreateTaskViewData.DueDateViewData.fixture(
                            selectedDateMillis = dueDate.toEpochMilliseconds(),
                            selectedDateDisplayText = expectedDisplayText,
                        )
                    },
                    minDueDateMillis = startOfToday.toEpochMilliseconds(),
                    saveButtonEnabled = true,
                )
            }

            viewModel.onEvent(CreateTaskEvent.SaveClicked)
            awaitState().saveButtonEnabled shouldBe false

            taskCapture.get().also { task ->
                task.priority shouldBe Priority.HIGHEST
                task.dueDate shouldBe endOfDayDueDate
            }

            awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
        }
    }

    @Test
    fun `DueDateChanged - should update and clear dueDate`() = test {
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
    fun `SaveClicked - should persist task with default values`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val creationDate = Instant.parse("2026-07-21T12:00:00Z")
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns creationDate
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get() shouldBe Task(
            id = id,
            title = title,
            description = null,
            creationDate = creationDate,
            dueDate = null,
            completionDate = null,
            priority = Priority.MEDIUM,
            url = null,
            location = null,
        )
    }

    @Test
    fun `SaveClicked - should persist selected priority`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.PriorityChanged(Priority.BLOCKER))
        expectState { copy(title = title, priority = Priority.BLOCKER, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().priority shouldBe Priority.BLOCKER
    }

    @Test
    fun `SaveClicked - should keep creationDate when editing an existing task`() {
        val taskId = Uuid.random()
        val creationDate = Instant.parse("2026-07-10T08:00:00Z")
        val editedTitle = "Edited title"
        val now = Instant.parse("2026-07-22T12:00:00Z")
        val savedTask = Task.fixture(id = taskId, title = "Task title", creationDate = creationDate, dueDate = null)
        val taskCapture = Capture.slot<Task>()

        everySuspend { mockTaskRepository.findById(taskId) } returns savedTask
        every(mockTimeProvider::now) returns now
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel = CreateTaskViewModel(
            taskId = taskId.toString(),
            taskRepository = mockTaskRepository,
            timeProvider = mockTimeProvider,
            uuidProvider = mockUuidProvider,
        )

        test {
            expectState {
                copy(
                    id = taskId.toString(),
                    title = savedTask.title,
                    description = savedTask.description,
                    url = CreateTaskViewData.Url(value = savedTask.url.orEmpty()),
                    dueDate = null,
                    saveButtonEnabled = true,
                )
            }

            viewModel.onEvent(CreateTaskEvent.TitleChanged(editedTitle))
            expectState { copy(title = editedTitle, saveButtonEnabled = true) }

            viewModel.onEvent(CreateTaskEvent.SaveClicked)

            // the save button gets disabled
            awaitState().saveButtonEnabled shouldBe false

            taskCapture.get().also { task ->
                task.id shouldBe taskId
                task.title shouldBe editedTitle
                task.creationDate shouldBe creationDate
            }

            awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
        }
    }

    @Test
    fun `SaveClicked - should persist url when valid`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val url = "https://example.com"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(url))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = url), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe url
    }

    @Test
    fun `SaveClicked - should persist url with query and no path`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val url = "https://google.com?q=AppOutlet"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(url))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = url), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe url
    }

    @Test
    fun `SaveClicked - should persist localhost url with port`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val url = "http://localhost:3000/tasks"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(url))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = url), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe url
    }

    @Test
    fun `SaveClicked - should prefix https when scheme is missing`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val inputUrl = "example.com"
        val expectedUrl = "https://example.com"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(inputUrl))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = inputUrl), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe expectedUrl
    }

    @Test
    fun `SaveClicked - should show error and not save when url is invalid`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val invalidUrl = "not a valid url"
        val validUrl = "example.com"
        val expectedUrl = "https://example.com"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(invalidUrl))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = invalidUrl), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        expectState { copy(url = CreateTaskViewData.Url(value = invalidUrl, isInvalid = true)) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(validUrl))
        expectState { copy(url = CreateTaskViewData.Url(value = validUrl)) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe expectedUrl
        awaitSideEffect() shouldBe CreateTaskAction.NavigateBack
    }

    @Test
    fun `SaveClicked - should trim surrounding whitespace`() = test {
        val id = Uuid.random()
        val title = "Task title"
        val inputUrl = "  example.com  "
        val expectedUrl = "https://example.com"
        val taskCapture = Capture.slot<Task>()

        every(mockUuidProvider::random) returns id
        every(mockTimeProvider::now) returns Instant.parse("2026-07-21T12:00:00Z")
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(CreateTaskEvent.TitleChanged(title))
        expectState { copy(title = title, saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.UrlChanged(inputUrl))
        expectState { copy(title = title, url = CreateTaskViewData.Url(value = inputUrl), saveButtonEnabled = true) }

        viewModel.onEvent(CreateTaskEvent.SaveClicked)
        awaitState().saveButtonEnabled shouldBe false

        taskCapture.get().url shouldBe expectedUrl
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
