package dev.appoutlet.foliary.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface Navigation<T : NavKey> {
    fun setupRoute(scope: EntryProviderScope<NavKey>)
    fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>)
}
