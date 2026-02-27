package dev.appoutlet.foliary.core.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun getSavedStateConfiguration(navigation: List<Navigation<*>>) = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            for (navigation in navigation) {
                navigation.setupPolymorphism(this)
            }
        }
    }
}