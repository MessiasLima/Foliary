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
Custom implementation on **Orbit MVI**.

### Components
- **State**: `Idle`, `Loading`, `Success<ViewData>`, `Error`
- **ViewData**: Interface for screen state data
- **Action**: Interface for side effects

### ViewModel Pattern
```kotlin
@KoinViewModel
class MyViewModel : ViewModel(), ContainerHost<MyAction> {
    override val container = container<MyAction> { loadInitialData() }
    fun onEvent(event: MyEvent) { /* handle */ }
}
```

### Screen Pattern
```kotlin
@Composable
fun MyScreen() {
    val viewModel = koinViewModel<MyViewModel>()
    Screen(
        viewModelProvider = { viewModel },
        onAction = { action, navigator -> /* handle */ }
    ) { viewData: MyViewData -> /* UI */ }
}
```

### Navigation
```kotlin
@Single
class MyNavigation : Navigation<MyNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<MyNavKey> { MyScreen() }
    }
    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(MyNavKey::class, MyNavKey.serializer())
    }
}

@Serializable
data object MyNavKey : NavKey
```

## Testing
**Frameworks**: `kotlin.test`, Kotest assertions, `kotlinx-coroutines-test`

**Important**: Always use Kotest assertions (e.g., `result shouldBe expected`, `list shouldHaveSize 0`) instead of standard `assert()` or `assertEquals()`.

```kotlin
class TaskRepositoryImplTest {
    @Test
    fun `should return todays tasks`() = runTest {
        val result = subject.findTodaysTasks()
        result shouldHaveSize 0
    }
}
```

**UI Tests**:
```kotlin
@OptIn(ExperimentalTestApi::class)
class AppTest {
    @Test
    fun `the application should start`() = runComposeUiTest {
        setContent { App() }
        onNodeWithTag("SignInScreen").assertIsDisplayed()
    }
}
```

> **Tip**: Add `modifier.testTag("TagName")` to composables to easily reference them in tests when finding by text is not possible (e.g. Icons, Containers).


## Contribution Workflow
1. **Branch**: `feature/short-description`
2. **Lint**: `./gradlew detekt` before commit
3. **Commit**: Imperative present-tense, ≤72 chars
4. **Push**: Pre-push hook runs detekt
5. **PR**: Target `main`, reference tickets

## Resources
- [Detekt](https://detekt.dev) | [Kotlin Style](https://kotlinlang.org/docs/coding-conventions.html)
- [Compose Multiplatform](https://compose-multiplatform.com) | [Orbit MVI](https://orbit-mvi.org)
- [Koin](https://insert-koin.io) | [Kermit](https://github.com/touchlab/Kermit)
