package dev.appoutlet.foliary.core.encryption

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual object PlatformEncryptionModule {
    @Single
    fun provideKSafe() = KSafe()
}
