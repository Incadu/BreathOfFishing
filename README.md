FishingApp
===========

Professional Android fishing game built with Jetpack Compose

Overview
--------

https://github.com/user-attachments/assets/6cee567c-1850-4375-9e35-f3572740ff8b

FishingApp is an Android application implemented with Kotlin and Jetpack Compose. The app simulates a fishing experience with dynamic fishing spots, an in-game shop, and interactive UI screens. It is structured to be modular and extensible, with a focus on reusable Compose components.

Highlights
----------
- Modern UI with Jetpack Compose
- Pluggable fishing spot generation logic
- Lure shop and inventory screens
- Designed for easy extension and testability

Repository structure
--------------------
- app/ - Android application module
  - src/main/java/com/fishing/android/screens - Compose screens (examples: LureShopScreen)
  - src/main/res - resources (drawables, strings, layouts)
- build.gradle, settings.gradle, gradle wrapper files

Requirements
------------
- Android Studio Arctic Fox or later (preferably latest stable)
- JDK 11 or newer
- Android SDK targeting the app's compileSdkVersion
- Gradle (the project includes Gradle wrapper; prefer using it)

Build & Run
-----------
Open the project in Android Studio, let Gradle sync, and run on an emulator or device.

From the command line you can build and install the debug APK:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Testing
-------
Run unit tests and instrumentation tests via Gradle or Android Studio's test runner:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

Recommended reusable UI components
----------------------------------
To keep UI consistent and reusable across screens (for example, using the same background image for multiple fishing spots or screens), add a small composable that encapsulates the background image logic and accepts a parameter for the resource or painter. Put this in a shared UI package, for example:

- File: app/src/main/java/com/fishing/android/ui/components/BackgroundImage.kt

Example composable (recommended)

```kotlin
@Composable
fun BackgroundImage(
    painterResourceId: Int,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = painterResourceId),
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale
        )
    }
}
```

## Next steps / Roadmap
------------
- Adding different fish difficult modifiers
- Updating fish movement to feel more fluid and natural.
- Refreshing fish positions when changing locations.
- Creating some sort of in game economy to utilize the shops more naturally.
- Would love to create some hand-drawn art, with some different vectors instead of the placeholders now.

Contributing
------------
- Fork the repo and open a PR for improvements.
- Follow Kotlin and Compose best practices.
- Add unit tests for business logic and small UI tests for critical flows.

Coding style
------------
- Follow Kotlin idioms and Android's official style guides.
- Keep composables small and focused; prefer passing state and actions as parameters.

Contact
-------
For questions, open an issue in the repository or contact the maintainers listed in the project metadata.

