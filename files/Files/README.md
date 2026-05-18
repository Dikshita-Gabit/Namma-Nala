# Namma-Nala - Canal Health Monitor

This is a complete Android Studio project written in **Kotlin** using **Jetpack Compose**. It recreates the screens from the supplied UI reference for a canal health monitoring app.

## Screens Included

1. Splash Screen
2. Login / Register
3. Home Dashboard
4. Breach Report - Step 1: Capture Photo
5. Breach Report - Step 2: Location Captured
6. Breach Report - Step 3: Details
7. Report Submitted
8. Map Overview
9. Water Status
10. Maintenance Tracker
11. Silt Alert
12. Alerts
13. Profile / Menu

## Project Folder

Open this folder in Android Studio:

```text
C:\Users\Dikshita Gabit\Documents\Codex\2026-05-18\files-mentioned-by-the-user-whatsapp
```

## Android Studio Steps

1. Open **Android Studio**.
2. Click **File > Open**.
3. Select the project folder shown above.
4. Click **OK** or **Open**.
5. Wait for **Gradle Sync** to finish.
6. If Android Studio asks to install SDK 35, Kotlin, Gradle, or Android Gradle Plugin, click **Install / Download**.
7. Open an Android Emulator, or connect your Android phone with USB debugging enabled.
8. Click the green **Run** button.
9. Select your device.
10. The app will install and open as **Namma-Nala**.

## Recommended Versions

```text
Android Studio: Koala / Ladybug / newer
JDK: Android Studio bundled JDK
Language: Kotlin
UI: Jetpack Compose
Compile SDK: 35
Minimum SDK: 23
Android Gradle Plugin: 8.5.2
Kotlin: 1.9.24
Compose Compiler: 1.5.14
```

## Important Files

```text
settings.gradle
build.gradle
app/build.gradle
app/src/main/AndroidManifest.xml
app/src/main/java/com/example/nammanala/MainActivity.kt
app/src/main/res/values/strings.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/styles.xml
```

## Main Kotlin File

All screens are implemented in:

```text
app/src/main/java/com/example/nammanala/MainActivity.kt
```

Important composable functions:

```text
NammaNalaApp()
SplashScreen()
LoginScreen()
HomeScreen()
BreachPhotoScreen()
BreachLocationScreen()
BreachDetailsScreen()
SubmittedScreen()
MapScreen()
WaterStatusScreen()
MaintenanceScreen()
SiltAlertScreen()
AlertsScreen()
ProfileScreen()
```

## How Navigation Works

The app uses a simple Compose state variable:

```kotlin
var screen by remember { mutableStateOf(AppScreen.Splash) }
```

Buttons update that state and show the next screen. This keeps the project easy for beginners to understand. You can later replace it with Navigation Compose if you want a production navigation graph.

## Current App Behavior

- Splash screen automatically opens Login.
- Login button opens the Home Dashboard.
- Home cards open Breach Report, Water Status, Maintenance Tracker, and Silt Alert.
- The bottom navigation opens Home, Map, Alerts, and Profile.
- The center plus button starts the Breach Report flow.
- The report flow moves from Photo, to Location, to Details, to Submitted.
- Map, photos, canal scenes, and markers are drawn using Compose Canvas, so no extra image assets are needed.

## Future Improvements

This project currently uses mock data so it can run immediately. For a real app, connect:

- Firebase Authentication or phone OTP login
- Firebase Firestore / Realtime Database
- CameraX for real photo capture
- Google Maps SDK or OpenStreetMap
- Android Location APIs
- Push notifications for alerts
- Kannada translations with Android string resources

## Troubleshooting

If Gradle Sync fails:

1. Make sure Android Studio is connected to the internet.
2. Open **Tools > SDK Manager**.
3. Install **Android SDK Platform 35**.
4. Open **Settings > Build, Execution, Deployment > Gradle**.
5. Select the **Gradle JDK** as Android Studio's bundled JDK.
6. Click **File > Sync Project with Gradle Files**.

If the app does not install:

1. Check that the emulator is fully started.
2. If using a phone, enable **Developer Options** and **USB debugging**.
3. Click **Run** again.
