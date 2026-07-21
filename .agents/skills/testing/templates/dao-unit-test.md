# DAO Unit Test Pattern

Use this shape for DAO tests against the real in-memory Room database.

```kotlin
class TaskDaoTest : DaoTest() {
    private val dao by lazy { database.taskDao() }

    @Test
    fun `should return overdue tasks and tasks due today`() = runTest {
        val endOfToday = Instant.parse("2026-07-21T23:59:59.999999999Z")
        val overdueTask = Task.fixture(
            dueDate = Instant.parse("2026-07-20T22:00:00Z"),
            completionDate = null,
        )

        dao.save(overdueTask)

        val result = dao.findTodayTasks(endOfToday).first()
        result.map { it.id } shouldBe listOf(overdueTask.id)
    }
}
```

## Notes

1. Extend `DaoTest`.
2. Use fixtures for inserted entities.
3. Read flow results with `.first()`.
4. When full entity equality is noisy because of persistence normalization, compare stable fields such as IDs.
