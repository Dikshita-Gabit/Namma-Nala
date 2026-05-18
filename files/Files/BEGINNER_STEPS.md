# Namma-Nala Android Studio Steps

This project is already created for you in Kotlin with Jetpack Compose.

Project folder to open:

```text
C:\Users\Dikshita Gabit\Documents\Codex\2026-05-18\files-mentioned-by-the-user-whatsapp
```

## Step 1: Open The Project

1. Open Android Studio.
2. Click File.
3. Click Open.
4. Select this folder:

```text
C:\Users\Dikshita Gabit\Documents\Codex\2026-05-18\files-mentioned-by-the-user-whatsapp
```

5. Click OK or Open.
6. Wait until Gradle Sync finishes.

## Step 2: Let Android Studio Download Needed Files

If Android Studio shows messages like:

```text
Install missing SDK
Download Gradle
Download Android Gradle Plugin
Sync Now
```

Click the suggested button and allow it.

Use these versions:

```text
Compile SDK: 35
Minimum SDK: 23
Kotlin: 1.9.24
Android Gradle Plugin: 8.5.2
Compose Compiler: 1.5.14
```

## Step 3: Open The Main Kotlin Code

In Android Studio, open this file:

```text
app > src > main > java > com > example > nammanala > MainActivity.kt
```

This file contains all app screens.

Important functions inside `MainActivity.kt`:

```text
NammaNalaApp               Main app controller
SplashScreen               Screen 1
LoginScreen                Screen 2
HomeScreen                 Screen 3
BreachPhotoScreen          Screen 4
BreachLocationScreen       Screen 5
BreachDetailsScreen        Screen 6
SubmittedScreen            Screen 7
MapScreen                  Screen 8
WaterStatusScreen          Screen 9
MaintenanceScreen          Screen 10
SiltAlertScreen            Screen 11
AlertsScreen               Screen 12
ProfileScreen              Screen 13
```

## Step 4: Understand The Navigation

The app uses this enum for screens:

```kotlin
private enum class AppScreen {
    Splash, Login, Home, BreachPhoto, BreachLocation, BreachDetails, Submitted,
    Map, WaterStatus, Maintenance, SiltAlert, Alerts, Profile
}
```

When a button is clicked, the screen changes like this:

```kotlin
go(AppScreen.Home)
```

Example:

```kotlin
PrimaryButton("Login", onLogin)
```

After login, it opens the Home screen.

## Step 5: Run The App

1. Start an emulator from Device Manager.
2. Or connect your Android phone using USB debugging.
3. At the top of Android Studio, select the device.
4. Click the green Run button.
5. Wait for the app to install.
6. The app name will show as Namma-Nala.

## Step 6: What Each File Does

```text
settings.gradle
```

This tells Gradle that the project has one app module.

```text
build.gradle
```

This declares the Android and Kotlin Gradle plugin versions.

```text
app/build.gradle
```

This enables Jetpack Compose and adds Compose dependencies.

```text
app/src/main/AndroidManifest.xml
```

This declares app permissions and the launcher activity.

```text
app/src/main/java/com/example/nammanala/MainActivity.kt
```

This is the main app code. All UI screens are here.

```text
app/src/main/res/values/strings.xml
```

This stores the app name.

```text
app/src/main/res/values/colors.xml
```

This stores color values.

```text
app/src/main/res/values/styles.xml
```

This stores the Android app theme.

## Step 7: How To Edit Text In The App

Open:

```text
MainActivity.kt
```

Search for the text you want to change.

Example:

```kotlin
Text("Hello, Ramesh!")
```

Change it to:

```kotlin
Text("Hello, Your Name!")
```

Then click Run again.

## Step 8: How To Change Colors

Open:

```text
MainActivity.kt
```

Find these lines near the top:

```kotlin
private val Green = Color(0xFF078B3E)
private val GreenDark = Color(0xFF05652F)
private val Blue = Color(0xFF1565C0)
```

Change the hex color code if needed.

## Step 9: How To Add Real Features Later

This project currently uses mock data so it runs easily.

For a real app, add:

```text
Firebase Authentication for login
CameraX for real photos
Google Maps SDK for real maps
Location APIs for real latitude and longitude
Firebase Firestore for saved reports
Push notifications for alerts
```

## Step 10: If Gradle Sync Fails

Try this:

1. Make sure internet is working.
2. Open Tools > SDK Manager.
3. Install Android SDK Platform 35.
4. Open Settings > Build, Execution, Deployment > Gradle.
5. Set Gradle JDK to Android Studio bundled JDK.
6. Click File > Sync Project with Gradle Files.

## Step 11: If Run Button Is Disabled

1. Wait for Gradle Sync to finish.
2. Make sure the project folder was opened, not only one Kotlin file.
3. Open `settings.gradle`.
4. Confirm it contains:

```gradle
include ":app"
```

5. Click Sync Now.

## Step 12: If The App Opens But Looks Small Or Different

Use a phone/emulator close to this size:

```text
Pixel 6
Pixel 7
Pixel 8
```

The UI is responsive, but the mockup was designed for a phone screen.

