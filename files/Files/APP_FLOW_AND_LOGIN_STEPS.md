# Namma-Nala App Flow And Login Steps

This app is a canal health monitoring app for farmers, local users, and field workers. Users can report canal problems, check water status, view alerts, and see their profile.

## Step 1: Open The App

1. User taps the Namma-Nala app icon.
2. Splash screen opens.
3. App checks whether the user is already logged in.
4. If the user is already logged in, the app opens Home automatically.
5. If the user is not logged in, the app opens Login.

## Step 2: New User Registration

1. User taps Register.
2. User enters:
   - Full name
   - Occupation
   - Phone number
   - Email address
   - Village or area
   - Password
3. App checks that the required information is valid.
4. App moves to Email Verification.

## Step 3: Email Verification

1. App shows the email address entered by the user.
2. User enters the verification code.
3. In this demo app, the code is `123456`.
4. After correct verification, the app saves the user profile locally.
5. App opens the Home screen.

## Step 4: Returning User Login

1. User enters phone number and password.
2. App checks basic input rules.
3. App saves the user as logged in.
4. App opens Home.
5. Next time the app opens, Login is skipped automatically.

## Step 5: Home Dashboard

Home shows:

1. User name and village.
2. Breach Report.
3. Water Status.
4. Maintenance Tracker.
5. Silt Alert.
6. Recent canal updates.

## Step 6: Breach Report Flow

1. Step 1: Capture Photo.
2. Step 2: Capture Location.
3. Step 3: Enter issue details and severity.
4. Submit Report.
5. App shows report submitted with a report ID.

## Step 7: Other Main Screens

1. Map: shows canal markers for breach, silt, maintenance, and milestones.
2. Water Status: shows whether water has reached each village.
3. Maintenance Tracker: shows upcoming and completed maintenance work.
4. Silt Alert: allows reporting heavy silt accumulation.
5. Alerts: shows reports, updates, and system messages.
6. Profile: shows name, occupation, village, phone, email, and email status.

## Step 8: Logout

1. User opens Profile.
2. User taps Logout.
3. App clears the saved login.
4. App returns to Login.

## What Is Working Now

1. Step-by-step login, register, and email verification screens.
2. Saved local login session.
3. Automatic Home opening when the user is already logged in.
4. Profile screen shows saved user information.
5. Logout clears the session.

## What To Add For A Real Public App

1. Firebase Authentication or phone OTP for real login.
2. Real email verification instead of the demo code.
3. Firestore or another backend database for all users.
4. CameraX for real photo capture.
5. Android Location APIs for real GPS location.
6. Google Maps SDK or OpenStreetMap for real maps.
7. Push notifications for alerts.
8. Admin dashboard for officers to check reports and update status.
9. Privacy policy, terms, and secure data handling.

