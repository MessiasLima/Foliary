package dev.appoutlet.foliary.data.local.dao

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.appoutlet.foliary.data.local.FoliaryDatabase
import dev.appoutlet.foliary.data.local.entity.ApplicationVersion
import dev.appoutlet.foliary.data.local.getInMemoryDatabaseBuilder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

abstract class DaoTest {
    protected lateinit var database: FoliaryDatabase

    @BeforeTest
    fun setup() {
        val builder = getInMemoryDatabaseBuilder()
        if (builder != null) {
            database = builder
                .setDriver(BundledSQLiteDriver())
                .build()
        }
    }

    @AfterTest
    fun tearDown() {
        if (::database.isInitialized) {
            database.close()
        }
    }
}

class ApplicationVersionDaoTest : DaoTest() {
    private val dao by lazy { database.applicationVersionDao() }

    @Test
    fun `should insert and get application version`() = runTest {
        if (getInMemoryDatabaseBuilder() == null) return@runTest
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
        if (getInMemoryDatabaseBuilder() == null) return@runTest
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
        if (getInMemoryDatabaseBuilder() == null) return@runTest
        dao.insert(ApplicationVersion(1, "1.0.0", false))
        dao.insert(ApplicationVersion(2, "1.1.0", false))

        val result = dao.getAll().first()
        result shouldHaveSize 2
    }
}
