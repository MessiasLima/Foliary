# AGENTS.md – Guidance for Automated Agents

## Overview
Reference for AI agents working on **Foliary**, a Kotlin Multiplatform (KMP) app using Compose Multiplatform, Orbit MVI, Koin, Room, and Kermit.

## Build, Lint & Test Commands
> Execute from repository root.

| Goal | Command |
|------|---------|
| Build all platforms | `./gradlew build` |
| Run desktop app | `./gradlew run` |
| Run all tests | `./gradlew allTests` |
| Run JVM tests | `./gradlew foliary:jvmTest` |
| Run Android tests | `./gradlew foliary:testDebugUnitTest` |
| Run iOS tests | `./gradlew foliary:iosSimulatorArm64Test` |
| Run single test class | `./gradlew foliary:jvmTest --tests "dev.appoutlet.foliary.data.task.TaskRepositoryImplTest"` |
| Run single test method | `./gradlew foliary:jvmTest --tests "dev.appoutlet.foliary.data.task.TaskRepositoryImplTest.should return todays tasks"` |
| Static analysis | `./gradlew detekt` |
| Tests with coverage | `./gradlew jvmTest -Pkover koverVerify` |
| Full check | `./gradlew check` |

### CI & Hooks
- **Pre-push hook**: `config/githooks/pre-push.sh` runs `./gradlew detekt`
- **PR verification**: `detekt` → `jvmTest -Pkover koverVerify` (min coverage: **80%**)

## Code Style Guidelines
Governed by **Detekt** (`config/detekt/detekt.yml`) and Kotlin conventions.

### Formatting
- **Max line length**: 120 chars (tests exempt)
- **Indentation**: 4 spaces, no tabs

### Imports
**Order** (blank line between groups):
1. `kotlin`/`kotlinx` → 2. `androidx`/platform → 3. Third-party → 4. `dev.appoutlet.foliary.*`

**Rules**: No wildcard imports, no unused imports.
**Forbidden**: `co.touchlab.kermit.Logger` – use `logger()` delegate.

### Naming Conventions
| Element | Pattern | Example |
|---------|---------|---------|
| Classes/Objects | `PascalCase` | `SignInViewModel` |
| Functions/Properties | `camelCase` | `onEvent`, `userName` |
| Constants | `PascalCase` | `DefaultTimeout` |
| Test classes | `PascalCase` + `Test` | `TaskRepositoryImplTest` |
| Test methods | Backticks | `` `should return todays tasks` `` |

### Types & Immutability
- Prefer `val` over `var`; use `data class` with `val` properties
- Use `sealed interface` for state/actions
- Handle nulls with `?.let`, `?:`, or `requireNotNull`

### Error Handling & Logging
- Catch specific exceptions; use `Result` or `runCatching { }`
- Logging: `private val log by logger()` then `log.d { }` / `log.e { }`

### Dependency Injection (Koin)
- ViewModels: `@KoinViewModel`; Services: `@Single`

### UI – Compose Multiplatform
- Stateless composables; **Material3 only** (Material2 forbidden)
- `Modifier` as last parameter; always set `contentDescription`

### Documentation
- Use **KDoc** for public APIs; **TODO/FIXME forbidden**

## MVI Architecture
Foliary uses a custom MVI implementation built on top of **Orbit MVI**.

### Core Components
- **MviViewModel<State, Action>**: Base class for all ViewModels. Automatically manages `errorState` and provides a `container` with a default `CoroutineExceptionHandler`.
- **Action**: Interface for side effects (e.g., navigation, showing toasts).
- **Screen**: A layout wrapper that connects the ViewModel to the UI. It handles:
    - Analytics tracking (via `screenName`).
    - Side effect collection (`onAction`).
    - Automatic error state UI (using `ErrorIndicator`).

### Implementation Pattern
```kotlin
@KoinViewModel
class FeatureViewModel : MviViewModel<FeatureState, FeatureAction>() {
    override val container = container(initialState = FeatureState()) {
        // Optional: load initial data
    }

    fun onEvent(event: FeatureEvent) = intent {
        // Logic goes here
    }
}

@Composable
fun FeatureScreen() {
    val viewModel = koinViewModel<FeatureViewModel>()
    Screen(
        screenName = "FeatureName",
        viewModelProvider = { viewModel },
        onAction = { action, navigator -> /* handle side effects */ }
    ) { state ->
        // Main UI content. Errors are handled automatically.
    }
}
```

## Testing
**Frameworks**: `kotlin.test`, Kotest assertions, `kotlinx-coroutines-test`

**Important**: Always use Kotest assertions (e.g., `result shouldBe expected`) instead of standard `assert()`.

```kotlin
class FeatureTest {
    @Test
    fun `should do something`() = runTest {
        val result = subject.doSomething()
        result shouldBe expectedValue
    }
}
```

### UI Tests
Use `runComposeUiTest` and `onNodeWithTag` (via `modifier.testTag("TagName")`).

## Contribution Workflow
1. **Branch**: `feature/short-description`
2. **Commit**: Imperative present-tense, ≤72 chars.
3. **PR**: Target `main`. Must pass `detekt` and tests (min 80% coverage).
