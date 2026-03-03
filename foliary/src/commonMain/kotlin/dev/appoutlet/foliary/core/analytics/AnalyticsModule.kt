package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.foliary.BuildKonfig
import dev.appoutlet.foliary.core.logging.logger
import dev.appoutlet.umami.Umami
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class AnalyticsModule {
    private val log by logger()

    @Single
    fun provideAnalytics(): Analytics {
        return if (BuildKonfig.isDebug) {
            provideDebugAnalytics()
        } else {
            provideReleaseAnalytics()
        }
    }

    private fun provideDebugAnalytics(): Analytics {
        return DebugAnalytics()
    }

    private fun provideReleaseAnalytics(): Analytics {
        if (BuildKonfig.umamiBaseUrl.isBlank() || BuildKonfig.umamiWebsiteId.isBlank()) {
            log.w { "Umami configuration missing - analytics will be disabled" }
        }

        val umami = Umami(website = BuildKonfig.umamiWebsiteId) {
            baseUrl(BuildKonfig.umamiBaseUrl)
        }
        return ReleaseAnalytics(umami)
    }
}
