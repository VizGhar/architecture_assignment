# Assignment

This is **demo** app just for demonstration purposes. It is intentionally **overengineered**. Whole app can be squashed under 300 LOC.

## Architecture

There was no exact specification on what **architectural patterns** (techniques) should I use. So I've used **some** of well-known ones.

1. MVVM
  - Model: `ScratchCardProvider` (similar to `Repository`)
  - View: `@Composable Screen...`
  - ViewModel: `...ViewModel`
2. Dependency Injection - no specials techniques shown (except one use of `@Named` annotation)
3. `Retrofit`, `OkHttp` and `Gson` used

Neither `Repository` nor `UseCase` pattern is used since both of them are very verbose and there is no use for them in this assignment.

## Job Cancelation

Job cancelation is not suitable for `ViewModel.onCleared()` function, since every `ViewModel` is bound to its `Activity` (`LifeCycleOwner`).
Creating custom `ViewModelStoreOwner` is also too heavy so I've simply made `ScratchCardViewModel` to handle Job cancelation using `DisposableEffect`.

Using `NavBackStackEntry` would be suitable too, but I've implemented custom navigation. This is one of the things I'd probably change in real application.

## Testing

A few testcases for critical parts of application were implemented showcasing JUnit4, MockK and StandardTestDispatcher.
Since there was no need for parametrized tests JUnit5 is also ommited.
