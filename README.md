# GorillaTagPredsSetter (Android)

Android app project configured with a standard Gradle + Android Studio layout.

## Android environment setup

1. Install **Android Studio** (Jellyfish / Iguana or newer).
2. In SDK Manager install:
   - **Android SDK Platform 34**
   - **Android SDK Build-Tools** (latest)
   - **Android SDK Platform-Tools**
3. Use **JDK 17** for Gradle.

## Project setup

1. Open this folder in Android Studio.
2. Copy `local.properties.example` to `local.properties`.
3. Set your SDK path:

   ```properties
   sdk.dir=/absolute/path/to/Android/Sdk
   ```

4. Let Gradle sync and run the `app` configuration.

## Command-line build

```bash
./gradlew assembleDebug
```

If build dependency downloads fail, verify your proxy/network allows:
- `https://services.gradle.org`
- `https://dl.google.com`
- `https://repo.maven.apache.org`
