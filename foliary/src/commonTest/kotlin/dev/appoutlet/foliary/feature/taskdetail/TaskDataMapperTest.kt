package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Instant

class TaskDataMapperTest {
    private val mockTimeProvider = mock<TimeProvider>()
    private val subject = TaskDataMapper(mockTimeProvider)

    @Test
    fun `should map basic fields for a completed task`() {
        val creationDate = Instant.parse("2026-07-20T12:00:00Z")
        val dueDate = Instant.parse("2026-07-29T12:00:00Z")
        val completionDate = Instant.parse("2026-07-25T12:00:00Z")
        val task = Task.fixture(
            creationDate = creationDate,
            dueDate = dueDate,
            completionDate = completionDate,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.displayText(creationDate) } returns "20 Jul 2026"
        every { mockTimeProvider.displayText(dueDate) } returns "29 Jul 2026"
        every { mockTimeProvider.displayText(completionDate) } returns "25 Jul 2026"

        val result = subject(task)

        result.title shouldBe task.title
        result.description shouldBe task.description
        result.isComplete shouldBe true
        result.dueDate shouldBe "29 Jul 2026"
        result.isOverdue shouldBe false
        result.overduePeriodInDays shouldBe null
        result.creationDate shouldBe "20 Jul 2026"
        result.completionDate shouldBe "25 Jul 2026"
        result.priority shouldBe task.priority
    }

    @Test
    fun `should map incomplete task with future due date as not overdue`() {
        val creationDate = Instant.parse("2026-07-20T12:00:00Z")
        val dueDate = Instant.parse("2026-07-25T12:00:00Z")
        val task = Task.fixture(
            creationDate = creationDate,
            dueDate = dueDate,
            completionDate = null,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.displayText(creationDate) } returns "20 Jul 2026"
        every { mockTimeProvider.displayText(dueDate) } returns "25 Jul 2026"

        val result = subject(task)

        result.isComplete shouldBe false
        result.isOverdue shouldBe false
        result.overduePeriodInDays shouldBe null
        result.completionDate shouldBe null
    }

    @Test
    fun `should map incomplete task with past due date as overdue and calculate overdue period`() {
        val creationDate = Instant.parse("2026-07-15T12:00:00Z")
        val dueDate = Instant.parse("2026-07-19T12:00:00Z")
        val now = Instant.parse("2026-07-22T12:00:00Z")
        val task = Task.fixture(
            creationDate = creationDate,
            dueDate = dueDate,
            completionDate = null,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.now() } returns now
        every { mockTimeProvider.displayText(creationDate) } returns "15 Jul 2026"
        every { mockTimeProvider.displayText(dueDate) } returns "19 Jul 2026"

        val result = subject(task)

        result.isComplete shouldBe false
        result.isOverdue shouldBe true
        result.overduePeriodInDays shouldBe 3
    }

    @Test
    fun `should return null overdue period when overdue is less than 2 days`() {
        val creationDate = Instant.parse("2026-07-20T12:00:00Z")
        val dueDate = Instant.parse("2026-07-21T12:00:00Z")
        val now = Instant.parse("2026-07-22T12:00:00Z")
        val task = Task.fixture(
            creationDate = creationDate,
            dueDate = dueDate,
            completionDate = null,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.now() } returns now
        every { mockTimeProvider.displayText(creationDate) } returns "20 Jul 2026"
        every { mockTimeProvider.displayText(dueDate) } returns "21 Jul 2026"

        val result = subject(task)

        result.isOverdue shouldBe true
        result.overduePeriodInDays shouldBe null
    }

    @Test
    fun `should map task with null due date`() {
        val creationDate = Instant.parse("2026-07-20T12:00:00Z")
        val task = Task.fixture(
            creationDate = creationDate,
            dueDate = null,
            completionDate = null,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.displayText(creationDate) } returns "20 Jul 2026"

        val result = subject(task)

        result.dueDate shouldBe null
        result.isOverdue shouldBe false
        result.overduePeriodInDays shouldBe null
    }

    @Test
    fun `should map task with null description`() {
        val creationDate = Instant.parse("2026-07-20T12:00:00Z")
        val task = Task.fixture(
            description = null,
            creationDate = creationDate,
            dueDate = null,
            completionDate = null,
        )

        every { mockTimeProvider.startOfToday() } returns Instant.parse("2026-07-22T00:00:00Z")
        every { mockTimeProvider.displayText(creationDate) } returns "20 Jul 2026"

        val result = subject(task)

        result.description shouldBe null
    }
}
