package dev.appoutlet.foliary.feature.signin

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.core.annotation.Single

@Single
class SignInNavigation : Navigation<SignInNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<SignInNavKey> { SignInScreen() }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(SignInNavKey::class, SignInNavKey.serializer())
    }
}

@Serializable
data object SignInNavKey : NavKey
