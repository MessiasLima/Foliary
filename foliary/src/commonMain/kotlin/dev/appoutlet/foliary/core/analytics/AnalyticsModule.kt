package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.foliary.BuildKonfig
import dev.appoutlet.umami.Umami
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class AnalyticsModule {

    @Single
    fun provideAnalytics(): Analytics {
        return if (BuildKonfig.isDebug) {
            DebugAnalytics()
        } else {
            provideUmami()?.let { umami ->
                ReleaseAnalytics(umami)
            } ?: DebugAnalytics()
        }
    }

    fun provideUmami(): Umami? {
        if (BuildKonfig.umamiBaseUrl.isBlank() || BuildKonfig.umamiWebsiteId.isBlank()) {
            return null
        }

        return Umami(website = BuildKonfig.umamiWebsiteId) {
            baseUrl(BuildKonfig.umamiBaseUrl)
        }
    }
}
