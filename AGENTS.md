# Copilot Instructions for Foliary

## Project Overview

Foliary is a Kotlin Multiplatform (KMP) application targeting Android, iOS, and Desktop (JVM). It uses Compose Multiplatform for the UI layer across all platforms.

## Build Commands

```bash
# Build all platforms
./gradlew build

# Run desktop application
./gradlew run

# Run all tests
./gradlew allTests

# Run JVM tests only
./gradlew foliary:jvmTest

# Run Android unit tests
./gradlew foliary:testDebugUnitTest

# Run a single test class
./gradlew foliary:jvmTest --tests "dev.appoutlet.foliary.SomeTest"

# Run iOS simulator tests
./gradlew foliary:iosSimulatorArm64Test

# Check (lint + tests)
./gradlew check
```

## Architecture

### Module Structure
- **`:foliary`** - Shared KMP module containing all business logic and UI (commonMain, androidMain, iosMain, jvmMain)
- **`:android`** - Android app shell (just Activity setup, delegates to :foliary)
- **`:desktop`** - Desktop app shell (just main function, delegates to :foliary)
- **`ios/`** - Xcode project that consumes the FoliaryShared framework

### Key Patterns
- **MVI Architecture**: Uses Orbit MVI for state management in ViewModels
- **Dependency Injection**: Koin for DI across all platforms
- **Navigation**: Jetpack Navigation3 (Nav3) for Compose Multiplatform
- **Logging**: Kermit for multiplatform logging
- **Database**: Room with KSP code generation for all platforms
- **Theming**: Material 3 with custom FoliaryTheme

### Source Set Organization
```
foliary/src/
├── commonMain/kotlin/    # Shared code (UI, business logic)
├── commonTest/kotlin/    # Shared tests
├── androidMain/kotlin/   # Android-specific implementations
├── iosMain/kotlin/       # iOS-specific implementations
└── jvmMain/kotlin/       # Desktop-specific implementations
```

## Conventions

### Package Structure
All code lives under `dev.appoutlet.foliary.*`

### Dependencies
- Use version catalog (`gradle/libs.versions.toml`) for all dependencies
- Reference as `libs.plugins.*` for plugins and `libs.*` for libraries

### Room Database
- Schema exports to `foliary/schemas/` directory
- KSP generates code for all platforms (kspAndroid, kspJvm, kspIosArm64, kspIosSimulatorArm64)

### Compose Resources
- Shared resources in `foliary/src/commonMain/composeResources/`
- Access via generated `Res` object (e.g., `Res.string.app_name`, `Res.drawable.icon`)

### iOS Framework
- Exports as `FoliaryShared` static framework
- Bundle ID: `dev.appoutlet.foliary`

## Ticket Creation

This repo uses structured ticket templates in `.gemini/commands/ticket/`:
- **task.toml** - Operational/maintenance tasks
- **feature.toml** - New features (uses GIVEN-WHEN-THEN acceptance criteria)
- **bug.toml** - Bug reports
- **spike.toml** - Investigation tasks

When creating tickets, follow the template structure and use GitHub issue dependencies to show blocking relationships.
