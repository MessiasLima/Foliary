# ViewModel Unit Test Pattern

Use this shape for Orbit-based ViewModel tests.

```kotlin
class SignInViewModelTest : ViewModelTest<SignInViewModel, SignInViewData, SignInAction>() {
    private val sessionStatusFlow = MutableStateFlow<SessionStatus>(SessionStatus.Initializing)
    private val mockAuthenticationRepository = mock<AuthenticationRepository>(MockMode.autoUnit)
        .apply { every { sessionStatus() } returns sessionStatusFlow }

    override fun createViewModel() = SignInViewModel(
        authenticationRepository = mockAuthenticationRepository,
        deeplinkDispatcher = DeepLinkDispatcher,
    )

    @Test
    fun `should show loading when session is initializing`() = test {
        expectState(SignInViewData.Loading)
    }

    @Test
    fun `should request magic link`() {
        val email = "user@example.com"
        sessionStatusFlow.value = SessionStatus.NotAuthenticated()

        test {
            expectState(SignInViewData.NotAuthenticated())

            viewModel.onEvent(SignInEvent.OnSendMagicLink(email))

            expectState(SignInViewData.NotAuthenticated(requestingMagicLink = true))
            verifySuspend { mockAuthenticationRepository.requestMagicLink(email) }
            expectState(SignInViewData.MagicLinkSent(email))
        }
    }
}
```

## Notes

1. Extend `ViewModelTest`.
2. Build the subject in `createViewModel()`.
3. Use the inherited `test { ... }` helper.
4. Assert states and side effects through the provided Orbit helpers.
5. Set initial collaborator state before entering the `test { ... }` block when startup behavior depends on it.
