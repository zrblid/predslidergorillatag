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


## Requested behavior (exact)

"trying to make a standalone Android app (APK) that runs on the Quest 2 itself and acts as a control panel for Gorilla Tag–related system tweaks, specifically:

1️⃣ A predictions (ms) slider

The slider represents network prediction latency (for example 20–60 ms).

Moving the slider changes a stored value the app uses when applying settings.

The slider itself does nothing to FPS directly — it only sets numbers.

2️⃣ Automatic refresh-rate (Hz) handling

The app forces the Quest 2 to 70 Hz minimum.

Predictions are set relative to Hz (example: 70 Hz → ~40 ms).

This avoids FPS drops by never exceeding what the headset can sustain.

3️⃣ Runs entirely from the headset

No PC required after setup.

No Termux.

No Tasker.

No external scripts.

Just: open app → tap → done.

4️⃣ Uses ADB-over-Wi-Fi internally

The app talks to the Quest’s Android system using shell-level commands.

That’s how refresh rate and prediction values are applied.

This is why permissions and setup matter so much.

5️⃣ Remembers the Quest’s IP address

First launch: you enter or confirm the Quest IP.

The app saves it locally.

Future launches reuse it automatically.

6️⃣ Optional Wi-Fi confirmation step

The app asks something like:

“Connect to example wifi?”

This ensures the Quest is on the correct network before applying settings.

This avoids failed ADB connections."
