---
name: create-screen
description: How to create a new UI screen in Foliary. Make sure to use this skill whenever the user asks to create a new screen, UI page, or feature screen. It covers creating the ViewModel, ViewState, Events, Actions, the Composable Screen itself, and the Navigation setup.
---
# Creating a Screen in Foliary

When asked to create a new screen in Foliary, you must follow this strict pattern based on MVI architecture and Compose Multiplatform.
A feature typically requires three main files inside a dedicated feature package (e.g., `feature/featurename/`):
1. `[Feature]ViewModel.kt` (ViewModel, State, Event, Action)
2. `[Feature]Screen.kt` (Composable UI)
3. `[Feature]Navigation.kt` (Navigation route and key)

## 1. ViewModel and Contracts (`[Feature]ViewModel.kt`)

The ViewModel must inherit from `MviViewModel<State, Action>` and be annotated with `@KoinViewModel`.

```kotlin
package dev.appoutlet.foliary.feature.featurename

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class FeatureNameViewModel : MviViewModel<FeatureNameViewData, FeatureNameAction>() {
    override val container = container(FeatureNameViewData.Idle) {
        // Init block (optional)
    }

    fun onEvent(event: FeatureNameEvent) {
        when (event) {
            FeatureNameEvent.OnDoSomething -> handleDoSomething()
        }
    }
    
    fun onTryAgain() = intent {
        // handle try again logic if needed
    }

    private fun handleDoSomething() = intent {
        // Logic goes here
        reduce { FeatureNameViewData.Loading }
        // postSideEffect(FeatureNameAction.NavigateToNext)
    }
}

// State
sealed interface FeatureNameViewData {
    data object Idle : FeatureNameViewData
    data object Loading : FeatureNameViewData
    // data class Success(val data: String) : FeatureNameViewData
}

// Events (from UI to ViewModel)
sealed interface FeatureNameEvent {
    data object OnDoSomething : FeatureNameEvent
}

// Actions (Side effects like navigation)
sealed interface FeatureNameAction : Action {
    data object NavigateToNext : FeatureNameAction
}
```

## 2. Composable Screen (`[Feature]Screen.kt`)

Use the custom `Screen` layout to wrap your content. It handles analytics tracking, error state visualization, and side effect collection.

```kotlin
package dev.appoutlet.foliary.feature.featurename

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeatureNameScreen() {
    val viewModel = koinViewModel<FeatureNameViewModel>()
    
    Screen(
        screenName = "FeatureNameScreen", // Exactly match the composable name
        viewModelProvider = { viewModel },
        onAction = ::onAction,
        onTryAgain = viewModel::onTryAgain // optional if error states have try again
    ) { viewData: FeatureNameViewData ->
        // UI implementation using Scaffold, AnimatedContent, etc.
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // UI Content based on viewData
            }
        }
    }
}

// Side Effect Handler
private fun onAction(action: FeatureNameAction, navigator: Navigator) {
    when (action) {
        FeatureNameAction.NavigateToNext -> {
            // navigator.push(NextNavKey)
        }
    }
}
```

## 3. Navigation Setup (`[Feature]Navigation.kt`)

Every screen needs to be registered in the navigation graph using the `androidx.navigation3` runtime with Koin.
The navigation class must be annotated with `@Single` and implement `Navigation<Key>`.
The key must implement `NavKey` and be `@Serializable`.

```kotlin
package dev.appoutlet.foliary.feature.featurename

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.core.annotation.Single

@Single
class FeatureNameNavigation : Navigation<FeatureNameNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<FeatureNameNavKey> { FeatureNameScreen() }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(FeatureNameNavKey::class, FeatureNameNavKey.serializer())
    }
}

@Serializable
data object FeatureNameNavKey : NavKey // Add parameters if the screen takes arguments
```

### Important rules
1. Group the ViewState/ViewData, Event, and Action interfaces in the same file as the ViewModel.
2. Keep the `onAction` side-effect handler function outside the main composable (e.g. as a private file-level function) to avoid lambda reallocation and keep code clean.
3. Don't add unnecessary comments. Mimic the existing structure cleanly.
