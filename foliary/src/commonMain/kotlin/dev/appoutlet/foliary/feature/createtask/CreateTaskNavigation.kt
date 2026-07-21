package dev.appoutlet.foliary.feature.createtask

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.core.annotation.Single

@Single
class CreateTaskNavigation : Navigation<CreateTaskNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<CreateTaskNavKey> { CreateTaskScreen() }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(CreateTaskNavKey::class, CreateTaskNavKey.serializer())
    }
}

@Serializable
data object CreateTaskNavKey : NavKey
