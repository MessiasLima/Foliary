package dev.appoutlet.foliary.core.testing

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.appoutlet.foliary.core.database.FoliaryDatabase
import dev.appoutlet.foliary.core.database.inMemoryDatabaseBuilder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class DaoTest {
    protected lateinit var database: FoliaryDatabase

    @BeforeTest
    fun setup() {
        database = inMemoryDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }
}