---
name: testing
description: How to write tests in Foliary. Make sure to use this skill whenever the user asks to add, update, fix, refactor, or review unit tests, DAO tests, repository tests, ViewModel tests, or test fixtures. Use it even when the request only mentions a production file if the work clearly requires test coverage.
---
# Writing Tests in Foliary

Follow the existing `commonTest` conventions already used in Foliary. Keep tests small, deterministic, and aligned with the production abstraction being exercised.

## General rules

### Fixtures First

Never manually build domain or entity objects inside tests when the object can be represented by a fixture.

1. Reuse an existing `fixture` helper first.
2. If no fixture exists yet, create one in `commonTest` near the tested type. Before creating or updating fixture functions you MUST read [`references/fixture.md`](./references/fixture.md).

### General testing rules

1. Put tests in the `commonTest` source set unless there is a clear reason to use another source set.
2. Use Kotest assertions such as `shouldBe`. Do not use plain `assert()`.
3. Use `runTest` for suspend and flow-based tests.
4. Name test methods with backticks and sentence-style names.
5. Before writing or updating this kind of test, read `templates/base-unit-test.md`.
6. Mock collaborators instead of standing up integration infrastructure unless the test is explicitly integration-oriented.
7. Use Mokkery with patterns like `mock<T>()`, `every { ... } returns ...`, and `verifySuspend { ... }` when verification is needed.
8. Mokkery cannot mock final classes, and Kotlin classes are final by default, so regular classes that need to be mocked in tests must be annotated with `@Open`.

## DAO Tests

1. DAO tests must extend the shared `DaoTest` base class.
2. Use the real in-memory database and obtain the DAO from `database`.
3. Before writing or updating this kind of test, read `templates/dao-unit-test.md`.

## ViewModel Tests

1. ViewModel tests must extend the shared `ViewModelTest` base class.
2. Implement `createViewModel()` and build the subject there.
3. Use the inherited `test { ... }` helper rather than calling Orbit test APIs directly.
4. Assert states with `expectState(...)` and side effects with `expectSideEffect(...)`.
5. Use `advanceTimeBy(...)` or `advanceUntilIdle()` when the ViewModel relies on delays or queued coroutines.
6. Before writing or updating this kind of test, read `templates/view-model-unit-test.md`.

## Implementation Checklist

1. Check whether a shared base class already exists for this test type.
2. Check whether a fixture already exists for every object you need.
3. Create or extend fixtures before manually constructing objects.
4. Keep assertions focused on the behavior under test.
