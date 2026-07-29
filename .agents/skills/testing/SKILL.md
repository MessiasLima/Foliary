---
name: testing
description: How to write tests in Foliary. You MUST this skill whenever the user asks to add, update, fix, refactor, or review any kind of test such as unit tests, UI tests, DAO tests, repository tests, ViewModel tests, or test fixtures.
---
# Writing Tests in Foliary

Every rule in this skill is a HARD RULE and MUST be followed.

MUST follow the existing `commonTest` conventions already used in Foliary. MUST keep tests small, deterministic, and aligned with the production abstraction being exercised.

## General rules

### Fixtures First

NEVER manually build domain or entity objects inside tests when the object can be represented by a fixture.

1. MUST reuse an existing `fixture` helper first.
2. If no fixture exists yet, MUST create one in `commonTest` near the tested type. Before creating or updating fixture functions, you MUST read [`references/fixture.md`](./references/fixture.md).

### General testing rules

1. MUST put tests in the `commonTest` source set unless there is a clear reason to use another source set.
2. MUST use Kotest assertions such as `shouldBe`. MUST NOT use plain `assert()`.
3. MUST use `runTest` for suspend and flow-based tests.
4. MUST name test methods with backticks and sentence-style names.
5. Before writing or updating this kind of test, you MUST read `templates/base-unit-test.md`.
6. MUST mock collaborators instead of standing up integration infrastructure unless the test is explicitly integration-oriented.
7. MUST use Mokkery with patterns like `mock<T>()`, `every { ... } returns ...`, and `verifySuspend { ... }` when verification is REQUIRED.
8. Mokkery CANNOT mock final classes, and Kotlin classes are final by default, so regular classes that tests mock MUST be annotated with `@Open`.

## DAO Tests

1. DAO tests MUST extend the shared `DaoTest` base class.
2. MUST use the real in-memory database and obtain the DAO from `database`.
3. Before writing or updating this kind of test, you MUST read `templates/dao-unit-test.md`.

## ViewModel Tests

1. ViewModel tests MUST extend the shared `ViewModelTest` base class.
2. MUST implement `createViewModel()` and build the subject there.
3. MUST use the inherited `test { ... }` helper rather than calling Orbit test APIs directly.
4. MUST assert states with `expectState(...)` and side effects with `expectSideEffect(...)`.
5. MUST use `advanceTimeBy(...)` or `advanceUntilIdle()` when the ViewModel relies on delays or queued coroutines.
6. Before writing or updating this kind of test, you MUST read `templates/view-model-unit-test.md`.

## UI Tests

MUST follow the existing Compose Multiplatform UI tests in `commonTest`. UI tests MUST exercise composables as state renderers by passing explicit `viewData` and capturing `onEvent`. MUST NOT use full app, Koin, real ViewModels, or navigation setup in UI tests.

1. MUST use `setContent { ... }` to render the smallest composable that proves the behavior. MUST pass explicit `viewData` and `onEvent`; NEVER construct a real ViewModel.
2. MUST keep each UI test focused on one behavior or user journey. MUST test render-only states directly, but when a state change and emitted event belong to the same user flow, MUST cover them together in one compact journey test. For events, MUST capture the callback in a nullable variable or `mutableListOf<Event>()`, perform the UI action, and assert with Kotest, for example `event shouldBe TodayEvent.OnAddTaskClick`.
3. MUST write compact user-journey tests when multiple states and actions are part of the same user flow. MUST update the supplied `viewData`, wait for Compose to settle, perform the user action, and assert the resulting UI or event in one test instead of splitting every intermediate state into separate expensive UI tests.
4. MUST use fixtures for view data and component data whenever possible, overriding only the fields REQUIRED for the test case.
5. MUST assert production strings through resources with `getString(Res.string.some_key)` and explicit generated resource imports. MUST NOT use hardcoded production copy in assertions; local hardcoded strings are acceptable only for test-only content or fixture data.
6. MUST query nodes by test tag first. MUST add a test tag to the production composable when the tested node does not have a stable tag yet.
7. MUST use user-facing text or content description only when that is the behavior being verified. MUST use `onNodeWithContentDescription(...)` for icon-only actions and accessibility labels.
8. MUST follow the existing tag shape: root tags use `ScreenName`; child tags use `ScreenName:ElementName` or `ComponentName:ElementName`.
9. MUST pass `useUnmergedTree = true` only when the test REQUIRES internal semantics, such as text fields, placeholder visibility, or nested component tags.
10. MUST use Compose test assertions such as `assertIsDisplayed`, `assertDoesNotExist`, `assertIsEnabled`, `assertIsNotEnabled`, `assertTextEquals`, `assertIsOn`, and `assertIsOff`; MUST keep assertions focused on visible behavior, enabled state, text value, or callback output.
11. MUST use `waitForIdle()` after changing Compose state from the test and `mainClock.advanceTimeBy(...)` plus `waitForIdle()` for UI timers.
12. When a test REQUIRES custom semantics assertions, MUST add or reuse a shared helper in `core/testing` instead of repeating raw `SemanticsMatcher` details in the test.

### Debugging UI Tests

1. CAN use `ComposeUiTest.printTree(...)` or `SemanticsNodeInteraction.printTree(...)` from `core/testing` to inspect the semantics tree while investigating a failing or unclear UI test.
2. MUST remove all `printTree` calls before finishing the change. They MUST NOT be committed.
3. MUST rely on stable test tags and focused assertions for the final test; `printTree` is only a temporary investigation aid.

## Implementation Checklist

1. MUST check whether a shared base class already exists for this test type.
2. MUST check whether a fixture already exists for every object you need.
3. MUST create or extend fixtures before manually constructing objects.
4. MUST keep assertions focused on the behavior under test.
5. For UI tests, MUST verify the behavior through a pure composable rendered with explicit view data.
6. For UI tests, MUST query by stable test tags first; MUST use resource-backed text or content descriptions only when the displayed copy or accessibility label is the behavior under test.
