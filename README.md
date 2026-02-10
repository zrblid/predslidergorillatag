# GorillaTagPredsSetter (Android)

Android app project configured with a standard Gradle + Android Studio layout.

## Android environment setup

1. Install **Android Studio** (Jellyfish / Iguana or newer).
2. In SDK Manager install:
   - **Android SDK Platform 34**
   - **Android SDK Build-Tools** (latest)
   - **Android SDK Platform-Tools**
3. Use **JDK 17** for Gradle.

## Project setup (local machine)

1. Open this folder in Android Studio.
2. Copy `local.properties.example` to `local.properties`.
3. Set your SDK path:

   ```properties
   sdk.dir=/absolute/path/to/Android/Sdk
   ```

4. Let Gradle sync and run the `app` configuration.

## Command-line build (local)

```bash
./gradlew assembleDebug
```

If build dependency downloads fail, verify your proxy/network allows:
- `https://services.gradle.org`
- `https://dl.google.com`
- `https://repo.maven.apache.org`

## Build on Volt.build (APK output)

If Volt reports `UserError: index.html not found`, keep this repository root `index.html` file in place.
It acts as a static-site placeholder while the real output is still the Android APK artifact (`dist/app-release.apk`).

Use these settings in Volt.build:

- **Build command**: `bash scripts/volt-build.sh`
- **Artifact path**: `dist/app-release.apk`
- **Java version**: `17`

What the script does:
- builds a release APK with Gradle (`:app:assembleRelease`)
- copies the APK to `dist/app-release.apk` so Volt can collect it as an artifact

If Volt.build requires manual env vars, set one of:
- `ANDROID_SDK_ROOT`
- `ANDROID_HOME`

Both are handled by the script.

## Create a zip package

To compress this repository into a shareable zip (excluding `.git`, build folders, and IDE cache files):

```bash
bash scripts/package-zip.sh
```

Optional custom output filename:

```bash
bash scripts/package-zip.sh my-app.zip
```
