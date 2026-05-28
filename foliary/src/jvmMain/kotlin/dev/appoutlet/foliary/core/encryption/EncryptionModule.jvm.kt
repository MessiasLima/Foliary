package dev.appoutlet.foliary.core.encryption

import dev.appoutlet.foliary.core.appdirs.Directories
import eu.anifantakis.lib.ksafe.KSafe
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Configuration
@Module
actual object EncryptionModule {
    @Single
    fun provideKSafe(): KSafe = KSafeWrapper.ksafe
}

private object KSafeWrapper{
    val ksafe by lazy {
        KSafe(
            fileName = "foliary",
            lazyLoad = true,
            baseDir = Directories.userDirectory
        )
    }
}
