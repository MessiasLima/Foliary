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
    fun provideUmami(): Umami {
        if (BuildKonfig.umamiBaseUrl.isBlank() || BuildKonfig.umamiWebsiteId.isBlank()) {
            log.w { "Umami configuration missing - analytics will be disabled" }
        }

        return Umami(website = BuildKonfig.umamiWebsiteId) {
            baseUrl(BuildKonfig.umamiBaseUrl)
        }
    }

    @Single
    fun provideAnalytics(umami: Lazy<Umami>): Analytics {
        return if (BuildKonfig.isDebug) {
            DebugAnalytics()
        } else {
            ReleaseAnalytics(umami.value)
        }
    }
}
