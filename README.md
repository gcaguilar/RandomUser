# Random User App

The application retrieves user data from the RandomUser.me API and displays it. It allows users to browse through random user profiles, view detailed information, and delete unwanted profiles from the list.

## Architecture
The application follows a modular architecture with different modules handling specific responsibilities:

- **app**: Main module that coordinates the application flow
- **feature/user**: Contains the user listing and detail functionality
- **network**: Contains the network API client(real and mock) setup and configuration for connecting to the RandomUser.me API
- **database**: Handles local data storage
- **user-localstorage-api**: Interface for local storage operations, abstracting the feature or different features from the data source.
- **shared/ui**: Contains shared UI components
- **shared/test**: Contains common rules for test

## Build Flavors

Due to encountering potential instability issues with the public RandomUser.me API during development, the project utilizes product flavors to manage different build configurations:

- **mock**: This flavor uses mock api, allowing for development and testing without relying on the actual API. 
- **production**: This flavor uses the real RandomUser.me API endpoint.

You can select the desired build variant (e.g., `mockDebug` or `productionDebug`) in Android Studio or via Gradle commands.

## Implementation details

### Model-View-Inent
The application uses MVI architecture with Jetpack Compose for the UI layer. StateFlow is used to implement unidirectional data flow, simplifying state management across the application.

### Error Handling
Error handling is implemented at multiple levels:

1. **Network Layer**: The API client wraps responses in Result objects, making error handling explicit and type-safe
2. **Repository Layer**: Errors are properly propagated and transformed into domain-specific errors when needed
3. **UI Layer**: Error states are displayed to the user in a friendly manner via Snackbar messages

### Local Storage
The app uses Room database for local caching of user data, allowing the application to work offline once data has been loaded. The local storage implementation is abstracted behind interfaces, making it easy to swap out the implementation if needed.

## Tech Stack
- Jetpack Compose with Material 3 for modern UI
- [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous operations
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for JSON parsing
- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Ktor Client](https://ktor.io/clients/index.html) for network requests
- [Room](https://developer.android.com/training/data-storage/room) for local database storage
- [Coil](https://coil-kt.github.io/coil/) for image loading
- [MockK](https://mockk.io/) for mocking in tests
- [Turbine](https://github.com/cashapp/turbine) for testing Kotlin Flow
- [Roborazzi](https://github.com/takahirom/roborazzi) for screenshot testing
- [Android navigation](https://developer.android.com/jetpack/androidx/releases/navigation) for navigation between screens

## Running Tests
To run unit tests:
```sh
./gradlew testProductionReleaseUnitTest
```

## Possible Improvements
- Improve UI.
- Replace the mocks in the tests by a version that uses only the network layer mocks and the rest are real implementations.
- Improve pagination as well as keep the page you are on after closing/opening the app.
- Support for multiple themes (dark/light mode)
- Improve error handling with more addocs to the fault or the layer in which they are found.
- **Testing Strategy Evolution**: Initially, the testing strategy aimed for more integrated unit tests, particularly for Use Cases, attempting to use Ktor's `MockEngine` for the network layer and an in-memory Room database. However, challenges were encountered in reliably configuring and verifying behavior in this setup. Consequently, the approach pivoted towards more traditional unit tests using mocks (MockK) to isolate components. This evolution and the initial integration attempts are visible in the Git history. Revisiting the integrated testing approach remains a potential future improvement.

## Features
- Browse random users from RandomUser.me API
- Filter user by name, surname, email.
- Delete users from the list
- Pagination support for loading more users
- View detailed information for each user