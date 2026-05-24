package dev.appoutlet.foliary.core.encryption

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Configuration
@Module
actual object EncryptionModule {
    @Single
    fun provideKSafe(): KSafe {
        return KSafe()
    }
}
