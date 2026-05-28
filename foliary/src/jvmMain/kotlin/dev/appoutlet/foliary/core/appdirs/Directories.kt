package dev.appoutlet.foliary.core.appdirs

import ca.gosyer.appdirs.AppDirs
import java.io.File


object Directories {
    val appDirs = AppDirs {
        appName = "Foliary"
        appAuthor = "AppOutlet"
    }

    val userDirectory by lazy {
        val folder = appDirs.getUserDataDir(roaming = true)
        createFolderIfItDoesntExists(folder)
    }

    private fun createFolderIfItDoesntExists(databaseFolder: String): File {
        val file = File(databaseFolder)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }
}

