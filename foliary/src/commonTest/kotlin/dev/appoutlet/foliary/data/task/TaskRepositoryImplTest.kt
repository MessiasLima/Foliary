package dev.appoutlet.foliary.data.task

import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TaskRepositoryImplTest {
    private val subject = TaskRepositoryImpl()

    @Test
    fun `should return todays tasks`() = runTest {
        val result = subject.findTodaysTasks()

        result shouldHaveSize 0
    }
}
