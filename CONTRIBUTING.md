# Contributing to Foliary

Thank you for your interest in contributing to Foliary! This repository hosts a Kotlin Multiplatform (KMP) application that runs on Android, iOS, and Desktop (JVM) using Compose Multiplatform.

---

## How to Contribute

There are two main ways to contribute to Foliary:

### Reporting Bugs

Found a bug? Open an issue at [github.com/MessiasLima/Foliary/issues](https://github.com/MessiasLima/Foliary/issues) with a clear description of the problem, steps to reproduce, and the version you are using.

### Coding

If you want to contribute code, read through the sections below to set up your development environment and follow our workflow.

#### Prerequisites

| Tool | Minimum version | Why it's needed |
|------|------------------|----------------|
| **JDK** | **21** (JetBrains Runtime – JBR – recommended) | Gradle build, Kotlin compilation, KSP code generation, and Desktop CSD features which rely on JBR. |
| **Android Studio** | Arctic Fox (2020.3.1) or newer | Android target development and UI preview. |
| **Xcode** | 15.0 or newer (macOS only) | iOS target compilation and simulator runs. |
| **Gradle** | **9.2.1** (wrapper version) | Newest features and faster builds. |
| **Git** | Any recent version | Version control and contribution workflow. |

> **Note** – The project is configured for JDK 21 with the JetBrains Runtime (JBR). We have not tested the application with older JDK versions, so compatibility cannot be guaranteed.

#### Clone the Repository

```bash
# Fork the repository on GitHub first (optional but recommended)
git clone https://github.com/<your-username>/Foliary.git
cd Foliary
```



#### Project Structure Overview

```
Foliary/                     # Root of the repo
├─ android/                 # Android-specific source set & Gradle module
├─ desktop/                 # Desktop (JVM) entry-point (Main.kt)
├─ ios/                     # Xcode project consuming the shared framework
├─ foliary/                 # Multiplatform shared module
│   ├─ src/commonMain/kotlin   # Business logic, UI (Compose), Koin modules
│   ├─ src/androidMain/kotlin  # Android-specific implementations
│   ├─ src/iosMain/kotlin      # iOS-specific implementations
│   └─ src/jvmMain/kotlin      # Desktop-specific implementations
├─ .run/                    # IDE run configurations for Desktop (hot-reload) – auto-appears in JetBrains IDE run menu
├─ build.gradle.kts          # Root Gradle script (plugins, repositories)
├─ settings.gradle.kts       # Module inclusion
└─ gradle/                  # Wrapper & additional scripts
```

#### Building & Running

**Desktop (JVM)**

The project ships pre-configured run configurations located in the `.run/` folder. They provide hot-reload support and appear automatically in the **Run** menu of JetBrains IDEs.

```bash
# You can also launch the app directly via Gradle
./gradlew :desktop:run
```

**Android**

1. Open the project in **Android Studio** (`File > Open > foliary/android`).
2. Sync Gradle (the wrapper will download Gradle 9.2.1 automatically).
3. Build & run on a device or emulator using the **Run** button in Android Studio.

**iOS**

The iOS integration is fully automated – you **do not** need to invoke any Gradle tasks. Simply open the Xcode workspace at `ios/Foliary.xcworkspace` and press **Run**. Xcode will build the shared `FoliaryShared` framework and launch the app on the selected simulator or device.

#### Testing

The project contains unit tests for the shared module and platform-specific UI tests (Compose Multiplatform testing works on the JVM, no Android emulator required).

```bash
# Run only the shared JVM tests
./gradlew :foliary:jvmTest

# Android unit tests (no instrumentation tests)
./gradlew :foliary:testDebugUnitTest

# iOS simulator tests (Apple Silicon only)
./gradlew :foliary:iosSimulatorArm64Test
```

Test reports are generated under `build/reports/tests/` for each module.

#### Code Style & Formatting

* **Kotlin** – The project follows the official Kotlin coding conventions.
  * **Detekt** – static analysis (run automatically via a **pre-push** git hook).
  * **ktlint** – code formatting (also enforced by the pre-push hook).
* **Compose** – UI code should follow the Compose style guide (use `Modifier` chaining, avoid deeply nested layouts).
* **XML resources** (Android) – lint is enforced via the Android Gradle plugin.

**Pre-push Git Hook**

A pre-push hook is provided in the repository that runs `./gradlew detekt` before any push to a remote. This ensures that static analysis always passes locally, and the CI will have a clean check.

#### Dependency Management

All dependencies are defined in the version catalog `gradle/libs.versions.toml`. When adding a new library:
1. **Only add a dependency if it is strictly necessary** – avoid unnecessary external libraries.
2. Add the version entry under `[versions]`.
3. Reference it in the appropriate section (`[libraries]`).
4. Use the alias in Gradle, e.g., `implementation(libs.koin.core)`.

Never hard-code version numbers in `build.gradle.kts`; always go through the catalog to keep the project reproducible across platforms.

#### Branch Naming

Use the pattern:

```
<issue-number>-<short-description>
```

For example, `123-fix-padding` for a change that resolves issue #123. This makes it easy to track which PR addresses which issue.

All branches should be based on the latest `main`.

#### Pull-Request Workflow

If you are new to contributing on GitHub, check out [Contributing to a project](https://docs.github.com/en/get-started/exploring-projects-on-github/contributing-to-a-project) for a step-by-step guide.

1. **Fork** the repository (if you haven't already).
2. **Create a branch** from `main` using the naming convention above.
3. **Make your changes** – keep commits atomic and descriptive.
4. **Run the full test suite** locally (`./gradlew allTests`).
5. **Push** the branch to your fork. The pre-push hook will automatically run Detekt and ktlint; you do not need to run them manually.
6. Open a **Pull Request** against `MessiasLima/Foliary:main`.
   * Provide a concise title.
   * Reference the related issue (`Fixes #123`).
   * Summarize what was changed and why.
7. The CI will automatically:
   * Build all platforms.
   * Run unit and UI tests.
   * Verify that the code passes static analysis (already checked by the pre-push hook).
8. Address any review comments. When approved, a maintainer will merge the PR using **squash-merge**.

#### Continuous Integration (GitHub Actions)

The workflow defined at `.github/workflows/pull-request.yml` runs on every PR (excluding changes to `website/`) and performs the following steps:
* **Static analysis** – Detekt checks (also enforced locally by the pre-push hook).
* **Unit tests & coverage** – JVM tests run with Kover to verify a minimum of 80% code coverage.

#### Analytics Configuration

Foliary uses **Umami** analytics for privacy-focused usage tracking. This is a production-only concern – analytics are **automatically disabled in debug builds**. Contributors do not need to set up analytics.

**How It Works**

- **Debug builds**: Use `DebugAnalytics` (logs only, no network calls)
- **Release builds**: Use `ReleaseAnalytics` if configured, otherwise fall back to `DebugAnalytics`
- **Event naming**: Use `snake_case` (e.g., `button_clicked`, `task_completed`)
- **Screen naming**: Use `PascalCase` with "Screen" suffix (e.g., `SignInScreen`, `TaskListScreen`)

To track custom events, inject the `Analytics` interface into your ViewModel:

```kotlin
@KoinViewModel
class MyViewModel(
    private val analytics: Analytics
) : ViewModel() {
    fun onButtonClick() {
        analytics.trackEvent("button_clicked", mapOf("button_id" to "submit"))
    }
}
```

For more information about Umami, visit: https://umami.is

#### Error Tracking Configuration

Foliary uses **Sentry** for error tracking and crash reporting. Error tracking is **automatically disabled in debug builds**, so contributors can work without any configuration.

**Setup (Optional for Contributors)**

To test error tracking in release builds, add your Sentry DSN to `local.properties`:

```properties
sentry.dsn=https://your-key@sentry.io/your-project-id
```

If not configured, the app gracefully skips Sentry initialization in release builds.

For complete documentation, visit: https://docs.sentry.io/platforms/kotlin/guides/compose-multiplatform/

#### License & Code of Conduct

Foliary is licensed under the **GNU General Public License 3.0** – see the `LICENSE` file for full details.

We adopt the **Contributor Covenant Code of Conduct** – see the `CODE_OF_CONDUCT.md` file for details.

#### Helpful Resources

* **Kotlin Multiplatform documentation** – https://kotlinlang.org/docs/multiplatform.html
* **Compose Multiplatform guide** – https://compose-multiplatform.com/docs/overview
* **Orbit MVI** – https://orbit-mvi.org
* **Koin DI** – https://insert-koin.io
* **Detekt** – https://detekt.dev
* **ktlint** – https://ktlint.github.io

---

## Questions & Support

Thank you for considering a contribution to Foliary!

If you have any questions or need support, feel free to [open an issue](https://github.com/MessiasLima/Foliary/issues) and I will be happy to help.
