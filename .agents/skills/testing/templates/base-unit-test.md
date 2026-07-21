# Base Unit Test Pattern

Use this shape for repository tests and other unit tests that mock collaborators.

```kotlin
private val mockTaskDao = mock<TaskDao>()
private val mockTimeProvider = mock<TimeProvider>()
private val subject = TaskRepositoryImpl(mockTaskDao, mockTimeProvider)

@Test
fun `should return tasks due today and overdue tasks`() = runTest {
    val endOfToday = Instant.parse("2026-07-22T23:59:59.999999999Z")
    val fixtureTasks = listOf(Task.fixture())

    every { mockTimeProvider.endOfToday() } returns endOfToday
    every { mockTaskDao.findTodayTasks(endOfToday) } returns flowOf(fixtureTasks)

    subject.findTodayTasks().first() shouldBe fixtureTasks
}
```

## Notes

1. Use Mokkery for collaborators.
2. Prefer fixtures for returned objects.
3. For `Flow`-based APIs, stub with `flowOf(...)` and assert with `.first()`.
4. Keep the test focused on the unit boundary instead of persistence details.
