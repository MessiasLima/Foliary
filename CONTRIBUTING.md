# Contributing to **Foliary**

Thank you for your interest in contributing to Foliary! This repository hosts a Kotlin Multiplatform (KMP) application that runs on Android, iOS, and Desktop (JVM) using Compose Multiplatform. Below are the updated guidelines to help you get started, set up the development environment, run the project, and submit changes.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Clone the Repository](#clone-the-repository)
3. [Project Structure Overview](#project-structure-overview)
4. [Building & Running](#building--running)
5. [Testing](#testing)
6. [Code Style & Formatting](#code-style--formatting)
7. [Dependency Management](#dependency-management)
8. [Issue Reporting & Branch Naming](#issue-reporting--branch-naming)
9. [Pull‑Request Workflow](#pull-request‑workflow)
10. [Continuous Integration (GitHub Actions)](#continuous-integration-github-actions)
11. [License & Code of Conduct](#license--code-of-conduct)
12. [Helpful Resources](#helpful-resources)

---

## 1️⃣ Prerequisites

| Tool | Minimum version | Why it's needed |
|------|------------------|----------------|
| **JDK** | **21** (JetBrains Runtime – JBR – recommended) | Gradle build, Kotlin compilation, KSP code generation, and Desktop CSD features which rely on JBR. |
| **Android Studio** | Arctic Fox (2020.3.1) or newer | Android target development and UI preview. |
| **Xcode** | 15.0 or newer (macOS only) | iOS target compilation and simulator runs. |
| **Gradle** | **9.2.1** (wrapper version) | Newest features and faster builds. |
| **Git** | Any recent version | Version control and contribution workflow. |

> **Note** – The project is configured for JDK 21 with the JetBrains Runtime (JBR). We have not tested the application with older JDK versions, so compatibility cannot be guaranteed.

---

## 2️⃣ Clone the Repository

```bash
# Fork the repository on GitHub first (optional but recommended)
git clone https://github.com/<your‑username>/Foliary.git
cd Foliary
```

The repository uses a **Git‑Town** workflow (`git-town.toml`). If you are unfamiliar with it, you can still use regular Git commands; just avoid rebasing the `main` branch directly.

---

## 3️⃣ Project Structure Overview

```
Foliary/                     # Root of the repo
├─ android/                 # Android‑specific source set & Gradle module
├─ desktop/                 # Desktop (JVM) entry‑point (Main.kt)
├─ ios/                     # Xcode project consuming the shared framework
├─ foliary/                 # Multiplatform shared module
│   ├─ src/commonMain/kotlin   # Business logic, UI (Compose), Koin modules
│   ├─ src/androidMain/kotlin  # Android‑specific implementations
│   ├─ src/iosMain/kotlin      # iOS‑specific implementations
│   └─ src/jvmMain/kotlin      # Desktop‑specific implementations
├─ .run/                    # IDE run configurations for Desktop (hot‑reload) – auto‑appears in JetBrains IDE run menu
├─ build.gradle.kts          # Root Gradle script (plugins, repositories)
├─ settings.gradle.kts       # Module inclusion
└─ gradle/                  # Wrapper & additional scripts
```

---

## 4️⃣ Building & Running

### General commands (run from the repository root)

```bash
# Clean the project
./gradlew clean

# Assemble all targets (Android APK, iOS framework, Desktop JAR)
./gradlew assemble
```

### Desktop (JVM)

The project ships pre‑configured run configurations located in the `.run/` folder. They provide hot‑reload support and appear automatically in the **Run** menu of JetBrains IDEs.

```bash
# You can also launch the app directly via Gradle
./gradlew :desktop:run
```

### Android

1. Open the project in **Android Studio** (`File > Open > foliary/android`).
2. Sync Gradle (the wrapper will download Gradle 9.2.1 automatically).
3. Build & run on a device or emulator:
   ```bash
   ./gradlew :android:assembleDebug   # Generates an APK in android/build/outputs/apk/debug
   ```
   Or click the **Run** button in Android Studio.

### iOS

The iOS integration is fully automated – you **do not** need to invoke any Gradle tasks. Simply open the Xcode workspace at `ios/Foliary.xcworkspace` and press **Run**. Xcode will build the shared `FoliaryShared` framework and launch the app on the selected simulator or device.

---

## 5️⃣ Testing

The project contains unit tests for the shared module and platform‑specific UI tests (Compose Multiplatform testing works on the JVM, no Android emulator required).

```bash
# Run only the shared JVM tests
./gradlew :foliary:jvmTest

# Android unit tests (no instrumentation tests)
./gradlew :foliary:testDebugUnitTest

# iOS simulator tests (Apple Silicon only)
./gradlew :foliary:iosSimulatorArm64Test
```

Test reports are generated under `build/reports/tests/` for each module.

---

## 6️⃣ Code Style & Formatting

* **Kotlin** – The project follows the official Kotlin coding conventions.
  * **Detekt** – static analysis (run automatically via a **pre‑push** git hook).
  * **ktlint** – code formatting (also enforced by the pre‑push hook).
* **Compose** – UI code should follow the Compose style guide (use `Modifier` chaining, avoid deeply nested layouts).
* **XML resources** (Android) – lint is enforced via the Android Gradle plugin.

### Pre‑push Git Hook

A pre‑push hook is provided in the repository that runs `./gradlew detekt` before any push to a remote. This ensures that static analysis always passes locally, and the CI will have a clean check.

---

## 7️⃣ Dependency Management

All dependencies are defined in the version catalog `gradle/libs.versions.toml`. When adding a new library:
1. **Only add a dependency if it is strictly necessary** – avoid unnecessary external libraries.
2. Add the version entry under `[versions]`.
3. Reference it in the appropriate section (`[libraries]`).
4. Use the alias in Gradle, e.g., `implementation(libs.koin.core)`.

Never hard‑code version numbers in `build.gradle.kts`; always go through the catalog to keep the project reproducible across platforms.

---

## 8️⃣ Issue Reporting & Branch Naming

* **Issues** – We will provide issue and pull‑request templates soon, so just open a clear description of the problem or feature.
* **Branch naming** – Use the pattern:
  ```
  <issue-number>-<short-description>
  ```
  For example, `123-fix-padding` for a change that resolves issue #123. This makes it easy to track which PR addresses which issue.

All branches should be based on the latest `main`.

---

## 9️⃣ Pull‑Request Workflow

1. **Fork** the repository (if you haven't already).
2. **Create a branch** from `main` using the naming convention above.
3. **Make your changes** – keep commits atomic and descriptive.
4. **Run the full test suite** locally (`./gradlew allTests`).
5. **Push** the branch to your fork. The pre‑push hook will automatically run Detekt and ktlint; you do not need to run them manually.
6. Open a **Pull Request** against `MessiasLima/Foliary:main`.
   * Provide a concise title.
   * Reference the related issue (`Fixes #123`).
   * Summarize what was changed and why.
7. The CI will automatically:
   * Build all platforms.
   * Run unit and UI tests.
   * Verify that the code passes static analysis (already checked by the pre‑push hook).
8. Address any review comments. When approved, a maintainer will merge the PR using **squash‑merge**.

---

## 🔁 Continuous Integration (GitHub Actions)

The workflow defined at `.github/workflows/pull-request.yml` runs on every PR and performs the following steps:
* **Gradle build** for all targets.
* **Unit & UI tests** (Compose Multiplatform UI tests run on the JVM; no Android instrumented tests are required).
* **Static analysis** – Detekt and ktlint checks (redundant with the pre‑push hook but kept for CI safety).

The workflow no longer checks for dependency updates.

---

## 📜 License & Code of Conduct

Foliary is licensed under the **Elastic License 2.0** – see the `LICENSE` file for full details.

We adopt the **Contributor Covenant Code of Conduct** – see the `CODE_OF_CONDUCT.md` file for details.

---

## 🔍 Analytics Configuration

Foliary uses **Umami** analytics for privacy-focused usage tracking. Analytics are **automatically disabled in debug builds** and fail gracefully if configuration is missing in release builds, so contributors can work without setting up analytics.

### Why We Use Analytics

Analytics help us understand how users interact with Foliary, enabling us to:
- **Improve user experience**: Identify which features are most valuable and which need improvement
- **Guide development priorities**: Focus on features that matter most to users
- **Detect issues**: Identify patterns that might indicate bugs or usability problems
- **Measure impact**: Understand the effectiveness of new features and improvements

All analytics are collected with a strong focus on user privacy (see Privacy section below).

### Setting Up Analytics (Optional for Contributors)

Analytics configuration is **optional** for contributors working on debug builds. If you want to test analytics functionality or work on release builds:

1. **Copy the template**:
   ```bash
   cp local.properties.example local.properties
   ```

2. **Add the configuration** to `local.properties`:
   ```properties
   umami.websiteId=your-website-uuid-here
   umami.baseUrl=https://your-umami-instance.com
   ```
   
   > **Note**: If not configured, the app will automatically use debug mode analytics (logs only, no tracking) even in release builds. This ensures the app never crashes due to missing analytics configuration.

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

### How It Works

- **Automatic screen tracking**: All screens using the `Screen` component are automatically tracked
- **Debug mode**: Debug builds always use `DebugAnalytics` (logs only, no network calls)
- **Release mode**: Release builds use `ReleaseAnalytics` if configured, otherwise fall back to `DebugAnalytics`
- **Event naming**: Use `snake_case` for event names (e.g., `button_clicked`, `task_completed`)
- **Screen naming**: Use `PascalCase` with "Screen" suffix (e.g., `SignInScreen`, `TaskListScreen`)

### Tracking Custom Events in ViewModels

To track user actions, inject the `Analytics` interface into your ViewModel:

```kotlin
@KoinViewModel
class MyViewModel(
    private val analytics: Analytics
) : ViewModel() {
    
    fun onButtonClick() {
        analytics.trackEvent("button_clicked", mapOf("button_id" to "submit"))
        // Your logic here
    }
}
```

### Privacy

Umami is a privacy-focused, open-source analytics platform that:
- **Does not collect personal identifiable information (PII)**: No names, emails, or user data
- **Does not use cookies**: No tracking across sites or persistent identifiers
- **Is GDPR compliant by design**: Built with European privacy standards in mind
- **Respects user privacy**: Only collects anonymous usage patterns
- **Is self-hosted**: Data stays under our control, not sold to third parties

We believe in transparent analytics that benefit users without compromising their privacy.

For more information about Umami, visit: https://umami.is


---

## 💾 Database Configuration

Foliary uses Room for local data persistence with multiplatform support.

### Platform Specifics
- **Android**: Database is stored in the application's internal database directory.
- **iOS**: Database is stored in the `NSDocumentDirectory`.
- **JVM**: Database is stored in the user data directory (managed by `Kotlin-Multiplatform-AppDirs`).

### Schema Export
Room schemas are exported to the `foliary/schemas` directory. To generate/update the schema, run:
```bash
./gradlew :foliary:kspAndroidMain
```

---

## 📚 Helpful Resources

* **Kotlin Multiplatform documentation** – https://kotlinlang.org/docs/multiplatform.html
* **Compose Multiplatform guide** – https://compose-multiplatform.com/docs/overview
* **Orbit MVI** – https://orbit-mvi.org
* **Koin DI** – https://insert-koin.io
* **Detekt** – https://detekt.dev
* **ktlint** – https://ktlint.github.io
* **Git‑Town workflow** – https://github.com/git-town/git-town

---

Thank you for helping make Foliary better! 🎉