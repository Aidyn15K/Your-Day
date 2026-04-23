# Your Day

Android widget project that shows how many seconds of the current local day have already passed out of 86,400.

## What it does

- Shows the current second of the day.
- Uses 24-hour local time.
- Displays time passed, time remaining, and completion percentage.
- Updates the widget every second while at least one widget instance is on the home screen.

## Build requirements

- Android Studio Jellyfish or newer.
- Android SDK Platform 34.
- JDK 17.

## Build

Open the project in Android Studio and run the `app` configuration, or use Gradle after installing the Android SDK and JDK 17.

## GitHub APK build

This project includes GitHub Actions in `.github/workflows/android-apk.yml`.

After you push the repository to GitHub:

1. Open the repository on GitHub.
2. Go to the `Actions` tab.
3. Open the latest `Build Android APK` run.
4. Download the `your-day-debug-apk` artifact.
5. Extract it and install `app-debug.apk` on your Android phone.

After installation, add the `Your Day` widget from the home screen widget picker.
