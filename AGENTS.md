# AGENTS.md – Guidance for Automated Agents

---

## 📖 Overview
This document provides a concise yet exhaustive reference for any AI‑driven coding agents (or human contributors) working on **Foliary**, a Kotlin Multiplatform (KMP) application using Compose Multiplatform, Orbit MVI, Koin, Room, and Kermit.

It covers:
1. **Build / Lint / Test commands** – including how to run a single test.
2. **Code‑style guidelines** – imports, formatting, naming, error handling, logging, DI, UI, testing, etc.
3. **Cursor / Copilot rules** – currently none, but the section explains where they would live.

The intent is to make agents deterministic, keep the codebase consistent, and minimise review friction.

---

## 🔧 Build, Lint & Test Commands
> All commands are to be executed from the repository root.

| Goal | Command | Description |
|------|---------|-------------|
| **Build all platforms** | `./gradlew build` | Compiles Android, iOS, JVM & desktop targets.
| **Run desktop app** | `./gradlew run` | Launches the JVM‑desktop entry point.
| **Run all tests** | `./gradlew allTests` | Executes unit tests for every source set.
| **Run JVM tests only** | `./gradlew foliary:jvmTest` | Fast feedback for desktop logic.
| **Run Android unit tests** | `./gradlew foliary:testDebugUnitTest` | Runs tests on the Android JVM target.
| **Run a single test class** | `./gradlew foliary:jvmTest --tests "<fully‑qualified‑class>"` | Replace `<fully‑qualified‑class>` with something like `dev.appoutlet.foliary.feature.signin.SignInViewModelTest`.
| **Run a single test method** | `./gradlew foliary:jvmTest --tests "<fully‑qualified‑class>.<method>"` | Example: `... --tests "dev.appoutlet.foliary.feature.signin.SignInViewModelTest.shouldHandleSignIn"`.
| **Run iOS simulator tests** | `./gradlew foliary:iosSimulatorArm64Test` |
| **Static analysis (detekt)** | `./gradlew detekt` | Enforces style & architecture rules (pre‑push hook runs this automatically). |
| **Check (lint + tests)** | `./gradlew check` | Runs `detekt`, `test`, `lint`, and other verification tasks.

### 🛑 Pre‑push Hook
A Git hook (`config/githooks/pre-push.sh`) automatically runs `./gradlew detekt` before any push. Agents should respect this and ensure the repository is clean locally before pushing.

---

## 🎨 Code‑Style Guidelines
The repository is governed primarily by **Detekt** (`config/detekt/detekt.yml`) and **Kotlin official style**. Below are the essential rules agents must obey.

### 1️⃣ Formatting & Layout
- **Maximum line length**: 120 characters (detekt `MaxLineLength`). Tests are exempt.
- **Indentation**: 4 spaces, no tabs.
- **Trailing whitespace**: none.
- **Blank lines**: separate logical sections, top‑level declarations, and after imports.
- **File header**: No mandatory license header, but keep the top‑most package declaration.

### 2️⃣ Imports
- **Order**:
  1. `kotlin`/`kotlinx` imports
  2. `androidx` / platform‑specific imports
  3. Third‑party libraries (e.g., `co.touchlab.kermit`, `io.insert-koin`)
  4. Project‑internal packages (`dev.appoutlet.foliary.*`)
- **Grouping**: a blank line between groups.
- **Wildcard imports** are forbidden.
- **Unused imports** are flagged by Detekt (`UnusedImport`).
- **Forbidden imports**: `co.touchlab.kermit.Logger` – use the `logger()` delegate instead (see Logging section).

### 3️⃣ Naming Conventions
| Element | Pattern | Notes |
|---------|---------|-------|
| **Classes / Objects** | `PascalCase` (e.g., `SignInViewModel`) | Use noun or noun‑phrase. |
| **Interfaces** | `PascalCase` (often prefixed with `I` is **not** used) |
| **Enums / Sealed interfaces** | `PascalCase` |
| **Functions** | `camelCase` (e.g., `onEvent`) | Must start with a lower‑case letter. |
| **Properties** | `camelCase` for mutable, `val` for immutable |
| **Constants** | `UPPER_SNAKE_CASE` (detekt `TopLevelPropertyNaming`) |
| **Test classes** | `PascalCase` ending with `Test` |
| **Test methods** | `should<Behavior>` or `test<Behavior>` – descriptive name. |
| **Parameters** | `camelCase`, avoid single‑letter names except `i`, `j` in loops |
| **Kotlin file names** | Match the primary class or object (`SignInViewModel.kt`). |
| **Long parameter lists**: max **10** (detekt `LongParameterList`). |
| **Long methods**: keep under 30 lines where practical (detekt `LongMethod`). |

### 4️⃣ Types & Immutability
- Prefer `val` over `var` wherever possible.
- Use **data classes** for plain data holders; mark them `data class` and make properties `val` unless mutation is required.
- Use **sealed classes / interfaces** for state & actions (already used throughout the codebase). Example: `sealed interface SignInAction : Action`.
- **Generic type parameters** should be named `T`, `R`, `V` unless a more descriptive name adds clarity.
- **Nullable types**: use explicit `?` and handle with `?.let`, `?:`, or `requireNotNull`.

### 5️⃣ Error Handling
- **Never catch `Exception`** generically (detekt `TooGenericExceptionCaught` disabled but discourage).
- Catch specific exceptions (`IOException`, `IllegalStateException`, etc.).
- Prefer **Result** or **Either**‑style wrappers for error propagation.
- Use `runCatching { … }` for simple try/catch when converting to `Result`.
- Log errors via **Kermit** (`logger().e { "Message" }`).

### 6️⃣ Logging
- Use the provided **Kermit** delegate:
  ```kotlin
  import dev.appoutlet.foliary.core.logging.logger

  private val log = logger()
  log.d { "Debug info" }
  ```
- Direct import of `co.touchlab.kermit.Logger` is forbidden (see Detekt `ForbiddenImport`).
- Include a tag or class name – the delegate infers it automatically from the enclosing class.

### 7️⃣ Dependency Injection (Koin)
- Annotate view‑models with `@KoinViewModel`.
- Modules should be defined in a single `AppModule.kt` file per platform.
- Prefer **interface‑to‑implementation** bindings.
- Use `get()` or `inject()` lazily where possible; avoid Service Locator anti‑pattern.
- Scope definitions should be placed in `appModule` and referenced via `by inject()`.

### 8️⃣ UI – Compose Multiplatform
- **Stateless composables** receive all state via parameters; avoid holding mutable state internally unless necessary.
- Naming: `PascalCase` for composable functions (e.g., `SignInScreen`).
- Use **Material3** components; prefer them over Material2 (enforced by detekt).
- Follow the **Compose style guide**:
  - Keep composable bodies short (< 30 lines).
  - Extract reusable UI pieces into their own composable files.
  - Use `Modifier` as the last parameter.
  - Provide preview functions annotated with `@Preview` for desktop and Android.
- **Accessibility**: always supply `contentDescription` for images/icons.

### 9️⃣ Testing Guidelines
- **Frameworks**: Kotest (`io.kotest:kotest-assertions-core`) + coroutine test (`kotlinx-coroutines-test`).
- Place tests under `src/commonTest/kotlin` for shared logic; platform‑specific tests under `androidTest` / `jvmTest` as needed.
- Use **behavior‑driven naming**: `shouldDoSomethingWhenCondition`. Example:
  ```kotlin
  class SignInViewModelTest : ShouldSpec({
      should("emit SignInSuccess when credentials are valid") {
          // test body
      }
  })
  ```
- Keep test methods **focused** – avoid long setup; use `BeforeTest` for repeated fixtures.
- Mock dependencies with **Mokkery** (`dev.mokkery`).
- Run a single test via the command in the Build table.

### 🔟 Documentation
- Use **KDoc** for public APIs, classes, and functions.
- Keep comments concise; avoid `TODO` and `FIXME` – they are prohibited (detekt `ForbiddenComment`).
- For complex logic, add a brief explanation before the code block.

### 1️⃣1️⃣ MVI Screen Architecture
Foliary uses a custom MVI implementation built on top of **Orbit MVI**.

#### 1. Components
- **ViewData**: An interface for the screen's state data.
- **Action**: An interface for side effects (navigation, toasts, etc.).
- **State**: A sealed interface (`Idle`, `Loading`, `Success<ViewData>`, `Error`).
- **ViewModel**: Implements `ContainerHost<SideEffect : Action>`.
- **Screen**: A Composable function using the `Screen` wrapper.

#### 2. Implementation Pattern
- **ViewModel**:
  ```kotlin
  @KoinViewModel
  class MyViewModel : ViewModel(), ContainerHost<MyAction> {
      override val container = container<MyAction> {
          loadInitialData()
      }

      private suspend fun loadInitialData() {
          // fetch data and emitState { State.Success(MyViewData(...)) }
      }
      
      fun onEvent(event: MyEvent) { /* handle events */ }
  }
  ```
- **Screen**:
  ```kotlin
  @Composable
  fun MyScreen() {
      val viewModel = koinViewModel<MyViewModel>()
      Screen(
          viewModelProvider = { viewModel },
          onAction = { action, navigator -> /* handle side effects */ }
      ) { viewData: MyViewData ->
          // Render UI using viewData
      }
  }
  ```

#### 3. Navigation
Each feature must have a `Navigation` implementation registered as a `@Single` in Koin.
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

---

## 📂 Cursor / Copilot Rules
The repository currently does **not** contain a `.cursor/` directory nor a `.github/copilot-instructions.md` file. If such files are added in the future, they should be referenced here and integrated into the agents’ workflow.

---

## 🤝 Contribution Workflow (for agents)
1. **Branch**: Create a short‑descriptive branch (`feature/add‑dark‑mode`).
2. **Local lint**: Run `./gradlew detekt` before committing.
3. **Commit style**: Use imperative present‑tense, e.g., `Add dark‑mode toggle`. Keep messages ≤ 72 characters.
4. **Push**: The pre‑push hook will run detekt; fix any failures.
5. **Pull request**: Target `main`; include a concise description and reference any related tickets.
6. **CI**: GitHub Actions will run `detekt`, `check`, and platform‑specific tests.

---

## 📚 Additional Resources
- **Detekt docs**: https://detekt.dev
- **Kotlin coding conventions**: https://kotlinlang.org/docs/coding-conventions.html
- **Compose Multiplatform guide**: https://compose-multiplatform.com
- **Orbit MVI**: https://orbit-mvi.org
- **Koin DI**: https://insert-koin.io
- **Kermit logging**: https://github.com/touchlab/Kermit

---

*This file is intentionally verbose (~150 lines) to give agents a complete, self‑contained reference. Keep it up‑to‑date as the codebase evolves.*