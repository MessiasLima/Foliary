package dev.appoutlet.foliary.data.authentication.database

import dev.appoutlet.foliary.core.testing.DaoTest
import dev.appoutlet.foliary.data.authentication.model.Session
import dev.appoutlet.foliary.data.authentication.model.fixture
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SessionDaoTest : DaoTest() {
    private val dao by lazy { database.sessionDao() }

    @Test
    fun `should insert and load session`() = runTest {
        val session = Session.fixture("access_token_1")

        dao.insert(session)
        val loadedSession = dao.load()

        loadedSession shouldBe session
    }

    @Test
    fun `should delete all sessions`() = runTest {
        val session = Session.fixture("access_token_1")
        dao.insert(session)

        dao.deleteAll()
        val loadedSession = dao.load()

        loadedSession shouldBe null
    }

    @Test
    fun `should return null when no session exists`() = runTest {
        val loadedSession = dao.load()

        loadedSession shouldBe null
    }
}
