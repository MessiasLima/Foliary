package dev.appoutlet.foliary.feature.main

import androidx.compose.material3.Text
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.core.annotation.Single

@Single
class MainNavigation : Navigation<MainNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<MainNavKey> { MainScreen() }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(MainNavKey::class, MainNavKey.serializer())
    }
}

@Serializable
data object MainNavKey : NavKey