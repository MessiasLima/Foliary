package dev.appoutlet.foliary.core.ui.component.task

import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class FoliaryTaskCardViewDataMapperTest {
    private val subject = FoliaryTaskCardViewDataMapper(DefaultTimeProvider())

    @Test
    fun `should map task fields and completed state`() {
        val fixture = Task.fixture()

        val result = subject(fixture)

        result.id shouldBe fixture.id.toString()
        result.title shouldBe fixture.title
        result.description shouldBe fixture.description
        result.isCompleted shouldBe true
        result.isOverdue shouldBe false
    }

    @Test
    fun `should map task as not completed when completion date is null`() {
        val task = Task.fixture(completionDate = null)
        val result = subject(task)
        result.isCompleted shouldBe false
    }

    @Test
    fun `should map task as overdue when due date is before now`() {
        val task = Task.fixture(
            dueDate = Clock.System.now().minus(1.days),
            completionDate = null
        )

        val result = subject(task)

        result.isOverdue shouldBe true
    }

    @Test
    fun `should not map task as overdue when the task was completed`() {
        val task = Task.fixture(
            dueDate = Clock.System.now().minus(1.days)
        )

        val result = subject(task)

        result.isOverdue shouldBe false
    }

    @Test
    fun `should map task as not overdue when due date is null`() {
        val task = Task.fixture(
            dueDate = null,
            completionDate = null
        )

        val result = subject(task)

        result.isOverdue shouldBe false
    }
}
