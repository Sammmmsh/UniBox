# Build and release checklist

## Local builds

Use JDK 17 and Android SDK 34. Android Studio's bundled JDK works on this machine.
The repository includes the Gradle 8.6 wrapper and a pinned distribution SHA-256.
The first build needs network access to download dependencies; cached builds can use `--offline`.

PowerShell, from the repository root:

```powershell
./gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleDebugAndroidTest
./gradlew.bat :app:assembleReleasePreview :app:assembleRelease :app:bundleRelease
```

On macOS/Linux use `./gradlew` instead. Do not commit `local.properties`, signing keys,
personal API keys, or generated `app/build` output. Some build output is historically tracked;
stage source files explicitly until that legacy repository cleanup is separately approved.

## Build variants

| Variant | Application ID | Purpose |
| --- | --- | --- |
| debug | `com.example.unibox` | Development and instrumentation tests; locally debug-signed |
| releasePreview | `com.example.unibox.preview` | Non-debuggable, minified, resource-shrunk release smoke test; locally debug-signed |
| release | `com.example.unibox` | Minified and resource-shrunk; intentionally unsigned without a production signing configuration |

`releasePreview` is labelled **UniBox Preview**, uses separate storage, and can be installed
alongside the main app. It is not a production release. Do not use its debug signing key
for distribution. No production keystore is created or checked into this repository.

Artifacts are under `app/build/outputs/`:

- `apk/debug/app-debug.apk`
- `apk/releasePreview/app-releasePreview.apk`
- `apk/release/app-release-unsigned.apk`
- `bundle/release/app-release.aab` (unsigned)
- `mapping/release/mapping.txt` (keep with the corresponding production build for crash deobfuscation)

## Test without deleting an existing library

Use a disposable emulator for the standard `connectedDebugAndroidTest` task. It can reinstall
or remove the target app. On an emulator holding notes you want to keep, use an in-place update:

```powershell
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r -t app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w com.example.unibox.test/androidx.test.runner.AndroidJUnitRunner
```

Only update installations signed with the same local key. If Android reports a signature
mismatch, stop; do not uninstall or clear app data. Tests use isolated databases and media
fixtures, not the user's library. The test-media provider is debug-only and non-exported.

For the optimized smoke test:

```powershell
adb install -r app/build/outputs/apk/releasePreview/app-releasePreview.apk
adb shell am start -W -n com.example.unibox.preview/com.example.unibox.MainActivity
```

Check cold launch, text capture, shared text/images, search, detail, both themes, and JSON
export through the system picker. Test a failed image import and cancel the export picker.
Reopen the app to confirm saved content persists. Check logcat for app crashes.

## Data handling

- JSON export uses Android's [Storage Access Framework](https://developer.android.com/training/data-storage/shared/documents-files#create-file),
  writes on an IO dispatcher, and needs no broad storage permission. It exports records,
  not settings or credentials. Local image paths in the JSON are references, not embedded images.
- JSON export is readable data, not a complete backup; in-app import/restore is not implemented.
- Shared images must arrive via a readable `content://` URI. Temporary imports are removed
  when the batch or database save fails. Cleanup only touches UniBox's owned media directory.
- OCR and organization run on-device. Basic web previews contact the saved page; enhanced
  extraction sends saved/refreshed URLs to Firecrawl only when enabled. Account usage and
  any personal-key charges depend on the user's Firecrawl account.
- Firecrawl preferences are excluded from Android cloud/device-transfer backup. API keys
  are never part of the JSON exporter. Do not include real keys in bug reports or screenshots.

## Before any public/store release

This build is ready for local review, not an assertion of store eligibility. These steps
require explicit release decisions and further verification:

- Choose the final application ID, versioning, and production signing/upload-key ownership.
- Update the SDK/toolchain as required by the current [Google Play target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878).
  This project still targets API 34; no claim of current Play eligibility is made.
- Audit bundled native libraries (including ML Kit) and validate 16 KB page-size compatibility
  on the required emulator/device configurations before submission.
- Review requested location/background-location and notification permissions against actual
  feature needs; test permission denial, location reminders, and notification navigation on devices.
- Complete privacy/data-safety disclosures, content policy checks, accessibility/device coverage,
  and current store screenshots. Verify Firecrawl behavior and limits using a test account.
- Test backup/restore and migrations on more API levels and physical devices. Verify image
  restore behavior; absolute local media URIs are not portable backup references.
- Sign and verify the final bundle with an owner-controlled key, retain its mapping file,
  and run a closed test. Nothing is uploaded or published by the build commands above.

The Gradle checksum is published by [Gradle](https://services.gradle.org/distributions/gradle-8.6-bin.zip.sha256).

## Verification record: 2026-08-28

- Gradle wrapper build: debug APK, test APK, optimized preview APK, unsigned release APK and AAB passed.
- 29 JVM tests and 39 Android instrumentation tests passed on the Pixel 5 / API 34 emulator.
- Debug lint: 0 errors, 21 warnings (19 dependency-version notices and 2 existing obsolete-SDK checks).
- Real system-picker export and cancellation passed; the exported file contained all 8 demo records.
- Settings was visually checked in light/dark modes; the original System theme setting was restored.
- The non-debuggable preview passed cold launch, shared-text capture, search, detail, failed-image
  recovery, and persistence after restart. No AndroidRuntime crash was logged during these checks.
- A read-only before/after database comparison confirmed all 8 main-app demo records were unchanged.
- The regenerated wrapper JAR's SHA-256 matched Gradle's published wrapper checksum.

These checks do not replace the public-release checklist or multi-device testing above.
