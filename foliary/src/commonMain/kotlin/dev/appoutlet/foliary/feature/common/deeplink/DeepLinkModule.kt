package dev.appoutlet.foliary.feature.common.deeplink

import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class DeepLinkModule {
    @Single
    fun provideDeepLinkDispatcher(): DeepLinkDispatcher = DeepLinkDispatcher
}
