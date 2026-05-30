# AGENTS.md – Guidance for Automated Agents

## Overview
Reference for AI agents working on **Foliary**, a Kotlin Multiplatform (KMP) app using Compose Multiplatform, Orbit MVI, Koin, Room, and Kermit.

The project also contains an application website located in the `website/` directory, which is an Angular application built using Tailwind CSS.

Additionally, the project contains email templates for authentication (e.g., sign-up confirmation) in the `emails/` directory. This is a **Maizzle** project that uses Tailwind CSS to build responsive HTML emails.

| Goal | Command (run from `emails/`) |
|------|------------------------------|
| Develop (live reload) | `npm run dev` |
| Build for production | `npm run build` |

**Key conventions:**
- Templates live in `emails/` and use Maizzle components (`<x-main>`, `<x-button>`, `<x-spacer>`, `<x-divider>`).
- Front matter YAML configures `title`, `preheader`, and `bodyClass`.
- Tailwind config is in `tailwind.config.js` and uses a custom Foliary color palette.
- Template variables use Go template syntax (`{{ .VariableName }}`) for integration with the backend auth provider (Supabase).
- The production build outputs optimized, inlined HTML to `build_production/`.

- See the [Supabase email template variables documentation](https://supabase.com/docs/guides/local-development/customizing-email-templates#template-variables) for available variables.

## ⚠️ HARD RULES FOR AGENTS ⚠️
1. **NO NEW LIBRARIES**: NEVER add any new third-party library or dependency without the user's explicit requirement or permission. If you think a library is needed, you MUST ask the user first. This is a HARD RULE.
2. **NO UNNECESSARY COMMENTS**: Focus comments on *why* something is done, rather than *what*. Do not talk to the user via code comments.
3. **MIMIC EXISTING STYLE**: Adhere rigorously to existing project conventions (formatting, structure, typing).
4. **NO GENERIC CATCH**: DO NOT catch generic exceptions like `catch (throwable: Throwable)` or `catch (e: Exception)`. This is an antipattern for coroutines as it can catch `CancellationException`. Catch specific exceptions or re-throw `CancellationException` if you must catch a broad type.
5. **NO PLAIN STRINGS IN UI**: Application screens must NEVER use plain hardcoded strings. Always create a string resource entry in `@foliary/src/commonMain/composeResources/values/strings.xml` and reference it via `stringResource(Res.string.your_string_name)`.

## Build & Test Commands
> Execute from repository root.

**⚠️ VERIFICATION RULES:**
- **FAST ITERATIONS**: NEVER run `build` or all tests during development. It takes too long. Run **ONLY specific tests** related to the code you changed (single class or method). Running a specific test also compiles the code, catching compilation issues. All tests are executed on the CI pipeline anyway.
- **JVM ONLY**: NEVER run Android or iOS tests unless explicitly required by the user. Always run JVM tests (`jvmTest`).
- **NO LINTING**: The `detekt` command runs automatically before every push, so there is no need for the agent to run it.

| Goal | Command |
|------|---------|
| Run single test class | `./gradlew foliary:jvmTest --tests "dev.appoutlet.foliary.data.task.TaskRepositoryImplTest"` |
| Run single test method | `./gradlew foliary:jvmTest --tests "dev.appoutlet.foliary.data.task.TaskRepositoryImplTest.should return todays tasks"` |

### CI & Hooks
- **Pre-push hook**: `config/githooks/pre-push.sh` runs `./gradlew detekt`
- **PR verification**: `detekt` → `jvmTest -Pkover koverVerify` (min coverage: **80%**)

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

## Dependency Injection (Koin)
- **ViewModels**: `@KoinViewModel`
- **Services**: `@Single`
- **Repositories**: Can be `@Factory` or `@Single` depending on how often they are used in the app. Use `@Single` for frequently used repos, and `@Factory` for rarely used ones.

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
