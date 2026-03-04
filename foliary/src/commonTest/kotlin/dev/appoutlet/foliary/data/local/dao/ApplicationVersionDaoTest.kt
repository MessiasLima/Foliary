package dev.appoutlet.foliary.data.local.dao

import dev.appoutlet.foliary.core.testing.DaoTest
import dev.appoutlet.foliary.data.applicationversion.model.ApplicationVersion
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ApplicationVersionDaoTest : DaoTest() {
    private val dao by lazy { database.applicationVersionDao() }

    @Test
    fun `should insert and get application version`() = runTest {
        val version = ApplicationVersion(
            buildNumber = 1,
            versionName = "1.0.0",
            wasReleaseNotesShown = false
        )

        dao.insert(version)
        val result = dao.findByBuildNumber(1)

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
        dao.insert(updatedVersion)

        val result = dao.findByBuildNumber(1)
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
