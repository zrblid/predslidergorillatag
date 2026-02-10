#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

chmod +x ./gradlew

# Volt.build/CI environments usually expose one of these SDK vars.
if [[ -n "${ANDROID_SDK_ROOT:-}" ]]; then
  export ANDROID_HOME="$ANDROID_SDK_ROOT"
elif [[ -n "${ANDROID_HOME:-}" ]]; then
  export ANDROID_SDK_ROOT="$ANDROID_HOME"
fi

./gradlew --no-daemon clean :app:assembleRelease

mkdir -p dist
cp app/build/outputs/apk/release/app-release.apk dist/app-release.apk

echo "APK ready at: dist/app-release.apk"
