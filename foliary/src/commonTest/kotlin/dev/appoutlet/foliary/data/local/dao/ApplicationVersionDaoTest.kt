package dev.appoutlet.foliary.data.local.dao

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.appoutlet.foliary.data.local.FoliaryDatabase
import dev.appoutlet.foliary.data.local.entity.ApplicationVersion
import dev.appoutlet.foliary.data.local.getInMemoryDatabaseBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class ApplicationVersionDaoTest {
    private lateinit var database: FoliaryDatabase
    private lateinit var dao: ApplicationVersionDao

    @BeforeTest
    fun setup() {
        database = getInMemoryDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = database.applicationVersionDao()
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `should insert and get application version`() = runTest {
        val version = ApplicationVersion(
            buildNumber = 1,
            versionName = "1.0.0",
            wasReleaseNotesShown = false
        )

        dao.insert(version)
        val result = dao.getByBuildNumber(1)

        result shouldBe version
    }

    @Test
    fun `should update application version`() = runTest {
        val version = ApplicationVersion(
            buildNumber = 1,
            versionName = "1.0.0",
            wasReleaseNotesShown = false
        )
        dao.insert(version)

        val updatedVersion = version.copy(wasReleaseNotesShown = true)
        dao.update(updatedVersion)

        val result = dao.getByBuildNumber(1)
        result?.wasReleaseNotesShown shouldBe true
    }

    @Test
    fun `should get all versions`() = runTest {
        dao.insert(ApplicationVersion(1, "1.0.0", false))
        dao.insert(ApplicationVersion(2, "1.1.0", false))

        val result = dao.getAll().first()
        result shouldHaveSize 2
    }
}
