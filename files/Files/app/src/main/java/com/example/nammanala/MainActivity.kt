package com.example.nammanala

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NammaNalaApp() }
    }
}

private enum class AppScreen {
    Splash, Login, Register, VerifyEmail, Home, BreachPhoto, BreachLocation, BreachDetails, Submitted,
    Map, WaterStatus, Maintenance, SiltAlert, Alerts, Profile, ReportHistory
}

private data class UserProfile(
    val name: String,
    val phone: String,
    val email: String,
    val occupation: String,
    val village: String
)

private data class WaterStatusItem(
    val id: Int,
    val village: String,
    val status: String,
    val time: String,
    val badge: String,
    val color: Color
)

private val Green = Color(0xFF078B3E)
private val GreenDark = Color(0xFF05652F)
private val Blue = Color(0xFF1565C0)
private val Red = Color(0xFFE53935)
private val Orange = Color(0xFFF59E0B)
private val Brown = Color(0xFF8D5420)
private val Bg = Color(0xFFF7FAF8)
private val Line = Color(0xFFE1E7E2)
private val Ink = Color(0xFF172126)
private val Muted = Color(0xFF66727A)

@Composable
private fun NammaNalaApp() {
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf(loadUserProfile(context)) }
    var pendingProfile by remember { mutableStateOf<UserProfile?>(null) }
    var screen by remember { mutableStateOf(AppScreen.Splash) }
    val go: (AppScreen) -> Unit = { screen = it }

    val waterStatuses = remember {
        mutableStateListOf(
            WaterStatusItem(1, "Hulikere", "Water reached", "Today, 08:30 AM", "Water Reached", Blue),
            WaterStatusItem(2, "Kudur", "Water not reached", "Today, 07:15 AM", "Not Reached", Orange),
            WaterStatusItem(3, "Vehwala", "Water reached", "Yesterday, 06:45 PM", "Water Reached", Blue),
            WaterStatusItem(4, "Mallasandra", "Water reached", "Yesterday, 05:30 PM", "Water Reached", Blue)
        )
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Green,
            secondary = Blue,
            background = Bg,
            surface = Color.White,
            error = Red
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            BackHandler(screen !in listOf(AppScreen.Splash, AppScreen.Login, AppScreen.Home)) {
                screen = when (screen) {
                    AppScreen.Register -> AppScreen.Login
                    AppScreen.VerifyEmail -> AppScreen.Register
                    else -> AppScreen.Home
                }
            }
            when (screen) {
                AppScreen.Splash -> SplashScreen {
                    go(if (userProfile == null) AppScreen.Login else AppScreen.Home)
                }
                AppScreen.Login -> LoginScreen(
                    onLogin = { profile ->
                        saveUserProfile(context, profile)
                        userProfile = profile
                        go(AppScreen.Home)
                    },
                    onRegister = { go(AppScreen.Register) }
                )
                AppScreen.Register -> RegisterScreen(
                    onBack = { go(AppScreen.Login) },
                    onContinue = { profile ->
                        pendingProfile = profile
                        go(AppScreen.VerifyEmail)
                    }
                )
                AppScreen.VerifyEmail -> VerifyEmailScreen(
                    email = pendingProfile?.email.orEmpty(),
                    onBack = { go(AppScreen.Register) },
                    onVerified = {
                        val verifiedProfile = pendingProfile ?: defaultUserProfile()
                        saveUserProfile(context, verifiedProfile)
                        userProfile = verifiedProfile
                        pendingProfile = null
                        go(AppScreen.Home)
                    }
                )
                AppScreen.Home -> HomeScreen(go, userProfile ?: defaultUserProfile())
                AppScreen.BreachPhoto -> BreachPhotoScreen(go)
                AppScreen.BreachLocation -> BreachLocationScreen(go)
                AppScreen.BreachDetails -> BreachDetailsScreen(go)
                AppScreen.Submitted -> SubmittedScreen(go)
                AppScreen.Map -> MapScreen(go)
                AppScreen.WaterStatus -> WaterStatusScreen(go, waterStatuses)
                AppScreen.Maintenance -> MaintenanceScreen(go)
                AppScreen.SiltAlert -> SiltAlertScreen(go)
                AppScreen.Alerts -> AlertsScreen(go)
                AppScreen.ReportHistory -> ReportHistoryScreen(go)
                AppScreen.Profile -> ProfileScreen(go, userProfile ?: defaultUserProfile()) {
                    clearUserProfile(context)
                    userProfile = null
                    go(AppScreen.Login)
                }
            }
        }
    }
}

@Composable
private fun SplashScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1300)
        onDone()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF8DD2FF), GreenDark))),
        contentAlignment = Alignment.Center
    ) {
        CanalScene(Modifier.fillMaxSize())
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color.White)) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SmallCanalIcon()
                    Text("NAMMA", color = GreenDark, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text("NALA", color = Blue, fontSize = 24.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("Canal Health Monitor", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(120.dp))
            Text("Empowering Farmers", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Protecting Water.", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Building Future.", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LoginScreen(onLogin: (UserProfile) -> Unit, onRegister: () -> Unit) {
    var phone by remember { mutableStateOf("+91 9876543210") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }
    val context = LocalContext.current

    ScreenColumn {
        Spacer(Modifier.height(20.dp))
        Text("Welcome Back!", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        Text("Login to continue", color = Muted, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (error.isNotBlank()) {
            Text(error, color = Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        TextButton(onClick = { Toast.makeText(context, "Password reset link sent to your phone", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) {
            Text("Forgot Password?", color = GreenDark)
        }
        PrimaryButton("Login") {
            if (phone.trim().length < 10 || password.length < 4) {
                error = "Enter a valid phone number and password."
            } else {
                onLogin(defaultUserProfile(phone = phone.trim()))
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth().clickable(onClick = onRegister), horizontalArrangement = Arrangement.Center) {
            Text("Don't have an account? ")
            Text("Register", color = GreenDark, fontWeight = FontWeight.Bold)
        }
        Divider(Modifier.padding(vertical = 20.dp))
        OutlinedButton(onClick = { Toast.makeText(context, "Google Login not implemented", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) {
            Text("G", color = Blue, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(12.dp))
            Text("Login with Google", color = Ink)
        }
        OutlinedButton(onClick = { Toast.makeText(context, "Phone Login not implemented", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Phone, null, tint = Ink)
            Spacer(Modifier.width(12.dp))
            Text("Login with Phone", color = Ink)
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.dp, Line, RoundedCornerShape(28.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("English", fontWeight = if (selectedLanguage == "English") FontWeight.Bold else FontWeight.Normal, modifier = Modifier.clickable { selectedLanguage = "English" })
            Text("Kannada", color = if (selectedLanguage == "Kannada") GreenDark else Muted, fontWeight = if (selectedLanguage == "Kannada") FontWeight.Bold else FontWeight.Normal, modifier = Modifier.clickable { selectedLanguage = "Kannada" })
        }
    }
}

@Composable
private fun RegisterScreen(onBack: () -> Unit, onContinue: (UserProfile) -> Unit) {
    var name by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
            Text("Create Account", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Text("Step 1: Enter your user information", color = Muted)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full name") }, leadingIcon = { Icon(Icons.Default.AccountCircle, null) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Occupation") }, leadingIcon = { Icon(Icons.Default.Description, null) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone number") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email address") }, leadingIcon = { Icon(Icons.Default.Email, null) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = village, onValueChange = { village = it }, label = { Text("Village / Area") }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Create password") }, leadingIcon = { Icon(Icons.Default.Lock, null) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        if (error.isNotBlank()) {
            Text(error, color = Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Continue to Email Verification") {
            error = when {
                name.isBlank() -> "Enter your full name."
                occupation.isBlank() -> "Enter your occupation."
                phone.trim().length < 10 -> "Enter a valid phone number."
                !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email address."
                village.isBlank() -> "Enter your village or area."
                password.length < 4 -> "Password must be at least 4 characters."
                else -> ""
            }
            if (error.isBlank()) {
                onContinue(UserProfile(name.trim(), phone.trim(), email.trim(), occupation.trim(), village.trim()))
            }
        }
    }
}

@Composable
private fun VerifyEmailScreen(email: String, onBack: () -> Unit, onVerified: () -> Unit) {
    var code by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    ScreenColumn {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
            Text("Verify Email", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Text("Step 2: Enter the verification code sent to", color = Muted)
        Text(email.ifBlank { "your email" }, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))
        InfoCard {
            Text("Demo verification code", color = Muted, fontWeight = FontWeight.Bold)
            Text("123456", fontSize = 26.sp, fontWeight = FontWeight.Black, color = GreenDark)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Verification code") },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        if (error.isNotBlank()) {
            Text(error, color = Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Verify and Open App") {
            if (code.trim() == "123456") {
                onVerified()
            } else {
                error = "Enter the correct verification code."
            }
        }
    }
}

@Composable
private fun HomeScreen(go: (AppScreen) -> Unit, user: UserProfile) {
    AppScaffold(active = AppScreen.Home, go = go) {
        GreenHeader("Namma-Nala", trailing = "28 C", onNotificationClick = { go(AppScreen.Alerts) })
        ScreenColumn(noTopPadding = true) {
            Text("Hello, ${user.name}!", fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text(user.village, color = Muted)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                DashboardCard("Breach\nReport", Icons.Outlined.Warning, Red, Modifier.weight(1f)) { go(AppScreen.BreachPhoto) }
                DashboardCard("Water\nStatus", Icons.Default.WaterDrop, Blue, Modifier.weight(1f)) { go(AppScreen.WaterStatus) }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                DashboardCard("Maintenance\nTracker", Icons.Default.Construction, Green, Modifier.weight(1f)) { go(AppScreen.Maintenance) }
                DashboardCard("Silt\nAlert", Icons.Default.Cloud, Brown, Modifier.weight(1f)) { go(AppScreen.SiltAlert) }
            }
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Recent Updates", fontWeight = FontWeight.Bold)
                Text("View All", color = GreenDark, fontSize = 13.sp, modifier = Modifier.clickable { go(AppScreen.Alerts) })
            }
            UpdateRow("Water reached Hulikere", "Today, 08:30 AM", Green)
            UpdateRow("Silt alert near Milestone 12", "Today, 07:15 AM", Red)
            UpdateRow("Maintenance scheduled in Section 1-5", "Yesterday, 06:30 PM", Orange)
        }
    }
}

@Composable
private fun BreachPhotoScreen(go: (AppScreen) -> Unit) {
    ReportShell(step = 1, go = go) {
        Text("Capture Photo", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        PhotoCard(kind = "breach")
        Tip("Capture clear photo of the issue")
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Next") { go(AppScreen.BreachLocation) }
    }
}

@Composable
private fun BreachLocationScreen(go: (AppScreen) -> Unit) {
    ReportShell(step = 2, go = go) {
        Text("Location Captured", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        MapDrawing(Modifier.fillMaxWidth().height(210.dp))
        InfoCard {
            Text("Place Name  : Hulikere Canal Section A", fontWeight = FontWeight.Bold, color = GreenDark)
            Text("Latitude     : 12.985123")
            Text("Longitude  : 77.345678")
            Text("Accuracy   : 8.5 m")
        }
        Text("Nearest Milestone", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
        Text("Milestone 13 (1.25 km away)")
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Next") { go(AppScreen.BreachDetails) }
    }
}

@Composable
private fun BreachDetailsScreen(go: (AppScreen) -> Unit) {
    var selectedSeverity by remember { mutableStateOf("High") }
    var description by remember { mutableStateOf("Water is leaking from the canal wall near milestone 13. Flow is reduced.") }

    ReportShell(step = 3, go = go) {
        Text("Issue Type", fontWeight = FontWeight.Bold)
        Selector("Leak / Breach")
        Spacer(Modifier.height(12.dp))
        Text("Description", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Text("Severity", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            SeverityChip("Low", selectedSeverity == "Low", Modifier.weight(1f)) { selectedSeverity = "Low" }
            SeverityChip("Medium", selectedSeverity == "Medium", Modifier.weight(1f)) { selectedSeverity = "Medium" }
            SeverityChip("High", selectedSeverity == "High", Modifier.weight(1f)) { selectedSeverity = "High" }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Submit Report") { go(AppScreen.Submitted) }
    }
}

@Composable
private fun SubmittedScreen(go: (AppScreen) -> Unit) {
    ScreenColumn(horizontal = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(70.dp))
        Box(
            modifier = Modifier.size(104.dp).clip(CircleShape).background(Green),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(70.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Report Submitted!", color = GreenDark, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Thank you for helping us", textAlign = TextAlign.Center)
        Text("protect our water.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Text("Report ID", color = Muted, fontWeight = FontWeight.Bold)
        Text("BR2024051478", fontSize = 22.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(32.dp))
        PrimaryButton("View Report History") { go(AppScreen.ReportHistory) }
        OutlinedButton(
            onClick = { go(AppScreen.Home) },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Green)
        ) {
            Text("Back to Home", color = GreenDark)
        }
    }
}

@Composable
private fun MapScreen(go: (AppScreen) -> Unit) {
    var activeFilter by remember { mutableStateOf("All") }
    AppScaffold(active = AppScreen.Map, go = go) {
        TitleBar("Canal Map")
        ScreenColumn(noTopPadding = true) {
            FilterRow(listOf("All", "Breach", "Silt Alert", "Maintenance"), activeFilter) { activeFilter = it }
            MapDrawing(Modifier.fillMaxWidth().height(420.dp), overview = true)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Legend("Breach", Red)
                Legend("Silt Alert", Brown)
                Legend("Maintenance", Green)
                Legend("Milestone", Blue)
            }
        }
    }
}

@Composable
private fun WaterStatusScreen(go: (AppScreen) -> Unit, statuses: MutableList<WaterStatusItem>) {
    val context = LocalContext.current
    AppScaffold(active = AppScreen.Home, go = go) {
        TitleBar("Water Status")
        ScreenColumn(noTopPadding = true) {
            Selector("Search village") { Toast.makeText(context, "Search functionality not implemented", Toast.LENGTH_SHORT).show() }
            statuses.forEach { item ->
                StatusRow(item.village, item.status, item.time, item.badge, item.color) {
                    statuses.remove(item)
                    Toast.makeText(context, "Status deleted", Toast.LENGTH_SHORT).show()
                }
            }
            if (statuses.isEmpty()) {
                Text("No water status reports", modifier = Modifier.fillMaxWidth().padding(20.dp), textAlign = TextAlign.Center, color = Muted)
            }
            Spacer(Modifier.height(12.dp))
            PrimaryButton("Add Water Status") { Toast.makeText(context, "Feature coming soon", Toast.LENGTH_SHORT).show() }
        }
    }
}

@Composable
private fun MaintenanceScreen(go: (AppScreen) -> Unit) {
    var activeTab by remember { mutableStateOf("Upcoming") }
    AppScaffold(active = AppScreen.Home, go = go) {
        TitleBar("Maintenance Tracker")
        ScreenColumn(noTopPadding = true) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                FilledTab("Upcoming", activeTab == "Upcoming", Modifier.weight(1f)) { activeTab = "Upcoming" }
                FilledTab("Completed", activeTab == "Completed", Modifier.weight(1f)) { activeTab = "Completed" }
            }
            if (activeTab == "Upcoming") {
                MaintenanceItem("Section 1 (Milestone 1-5)", "Desilting", "25 May 2024")
                MaintenanceItem("Section 5 (Milestone 21-25)", "Weed Removal", "30 May 2024")
                MaintenanceItem("Section 8 (Milestone 36-40)", "Desilting", "05 June 2024")
            } else {
                MaintenanceItem("Section 2 (Milestone 6-10)", "Gate Repair", "10 May 2024", isCompleted = true)
                MaintenanceItem("Section 3 (Milestone 11-15)", "Desilting", "05 May 2024", isCompleted = true)
            }
        }
    }
}

@Composable
private fun SiltAlertScreen(go: (AppScreen) -> Unit) {
    var description by remember { mutableStateOf("Heavy silt accumulation reducing water flow.") }
    AppScaffold(active = AppScreen.Home, go = go) {
        TitleBar("Silt Alert")
        ScreenColumn(noTopPadding = true) {
            Text("Capture Photo", fontWeight = FontWeight.Bold)
            PhotoCard(kind = "silt")
            Text("Location Captured", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            InfoCard {
                Text("Lat : 12.901234")
                Text("Long : 77.331234")
                Text("Accuracy : 7.2 m")
            }
            Text("Description", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(18.dp))
            PrimaryButton("Submit Alert") { go(AppScreen.Alerts) }
        }
    }
}

@Composable
private fun AlertsScreen(go: (AppScreen) -> Unit) {
    var activeFilter by remember { mutableStateOf("All") }
    AppScaffold(active = AppScreen.Alerts, go = go) {
        TitleBar("Alerts & Updates")
        ScreenColumn(noTopPadding = true) {
            FilterRow(listOf("All", "Reports", "Updates", "System"), activeFilter) { activeFilter = it }
            AlertRow(Icons.Outlined.Warning, Green, "Your breach report BR2024051478 is under review", "Today, 09:15 AM") { go(AppScreen.ReportHistory) }
            AlertRow(Icons.Default.LocationOn, Blue, "Maintenance scheduled in your section (Milestone 1-5)", "Today, 08:00 AM") { go(AppScreen.ReportHistory) }
            AlertRow(Icons.Default.WaterDrop, Blue, "Water reached Hulikere", "Today, 06:30 AM") { go(AppScreen.ReportHistory) }
            AlertRow(Icons.Default.Cloud, Brown, "Silt alert reported near Milestone 22", "Yesterday, 05:40 PM") { go(AppScreen.ReportHistory) }
        }
    }
}

@Composable
private fun ReportHistoryScreen(go: (AppScreen) -> Unit) {
    AppScaffold(active = AppScreen.Alerts, go = go) {
        TitleBar("Report History")
        ScreenColumn(noTopPadding = true) {
            Text("Your Submitted Reports", fontWeight = FontWeight.Bold, color = Muted, modifier = Modifier.padding(bottom = 12.dp))
            ReportHistoryItem("BR2024051478", "Breach Report", "Hulikere Canal Section A", "Under Review", Green)
            ReportHistoryItem("SL2024051205", "Silt Alert", "Milestone 22 area", "Resolved", Blue)
            ReportHistoryItem("BR2024050812", "Breach Report", "Section 5 - Kudur", "Action Taken", Orange)
            Spacer(Modifier.height(20.dp))
            OutlinedButton(onClick = { go(AppScreen.Home) }, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Dashboard", color = GreenDark)
            }
        }
    }
}

@Composable
private fun ReportHistoryItem(id: String, type: String, loc: String, status: String, color: Color) {
    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp), colors = CardDefaults.cardColors(Color.White), border = BorderStroke(1.dp, Line)) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(type, fontWeight = FontWeight.Bold)
                Text(id, fontSize = 12.sp, color = Muted)
            }
            Text(loc, color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.clip(RoundedCornerShape(4.dp)).background(color.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(status, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileScreen(go: (AppScreen) -> Unit, user: UserProfile, onLogout: () -> Unit) {
    val context = LocalContext.current
    AppScaffold(active = AppScreen.Profile, go = go) {
        TitleBar("Profile")
        ScreenColumn(noTopPadding = true) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, null, tint = Blue, modifier = Modifier.size(72.dp))
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(user.name, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text(user.occupation, color = Muted)
                    Text(user.village, color = Muted)
                }
            }
            Divider(Modifier.padding(vertical = 16.dp))
            InfoCard {
                Text("Phone: ${user.phone}")
                Text("Email: ${user.email}")
                Text("Email status: Verified", color = GreenDark, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            MenuRow(Icons.Default.Description, "My Reports") { go(AppScreen.ReportHistory) }
            MenuRow(Icons.Default.Notifications, "My Alerts") { go(AppScreen.Alerts) }
            MenuRow(Icons.Default.WaterDrop, "Water Status History") { go(AppScreen.WaterStatus) }
            MenuRow(Icons.Default.Settings, "Settings") { Toast.makeText(context, "Settings coming soon", Toast.LENGTH_SHORT).show() }
            MenuRow(Icons.Outlined.Info, "Help & Support") { Toast.makeText(context, "Support desk opening soon", Toast.LENGTH_SHORT).show() }
            MenuRow(Icons.Outlined.Info, "About Us") { Toast.makeText(context, "Namma Nala v1.0", Toast.LENGTH_SHORT).show() }
            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Logout, null, tint = Red)
                Spacer(Modifier.width(8.dp))
                Text("Logout", color = Red)
            }
        }
    }
}

@Composable
private fun ReportShell(step: Int, go: (AppScreen) -> Unit, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { go(AppScreen.Home) }) { Icon(Icons.Default.ArrowBack, null) }
            Text("Breach Report", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(Modifier.width(48.dp))
        }
        Stepper(step)
        ScreenColumn(noTopPadding = true, content = content)
    }
}

@Composable
private fun AppScaffold(active: AppScreen, go: (AppScreen) -> Unit, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxSize().background(Bg)) {
        Column(Modifier.weight(1f)){
            content()
        }
        BottomNav(active, go)
    }
}

@Composable
private fun ScreenColumn(
    noTopPadding: Boolean = false,
    horizontal: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = if (noTopPadding) 8.dp else 18.dp, bottom = 18.dp),
        horizontalAlignment = horizontal,
        content = { content()}
    )
}

@Composable
private fun GreenHeader(title: String, trailing: String, onNotificationClick: () -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(GreenDark, Green))).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        IconButton(onClick = onNotificationClick) {
            Icon(Icons.Default.Notifications, null, tint = Color.White)
        }
        Spacer(Modifier.width(10.dp))
        Text(trailing, color = Color.White)
    }
}

@Composable
private fun TitleBar(title: String) {
    Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)
    )
}

@Composable
private fun BottomNav(active: AppScreen, go: (AppScreen) -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(68.dp).background(Color.White).border(1.dp, Line),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("Home", Icons.Default.Home, active == AppScreen.Home) { go(AppScreen.Home) }
        NavItem("Map", Icons.Default.Map, active == AppScreen.Map) { go(AppScreen.Map) }
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(Green).clickable { go(AppScreen.BreachPhoto) },
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.Add, null, tint = Color.White) }
        NavItem("Alerts", Icons.Default.Notifications, active == AppScreen.Alerts) { go(AppScreen.Alerts) }
        NavItem("Profile", Icons.Default.AccountCircle, active == AppScreen.Profile) { go(AppScreen.Profile) }
    }
}

@Composable
private fun NavItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Icon(icon, null, tint = if (selected) Green else Ink, modifier = Modifier.size(22.dp))
        Text(label, color = if (selected) Green else Ink, fontSize = 11.sp)
    }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Green),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
private fun DashboardCard(text: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(128.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(42.dp).clip(CircleShape).background(color).padding(9.dp))
            Spacer(Modifier.height(10.dp))
            Text(text, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun Stepper(step: Int) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Step("Photo", 1, step)
        Divider(Modifier.weight(1f), color = if (step >= 2) Green else Line)
        Step("Location", 2, step)
        Divider(Modifier.weight(1f), color = if (step >= 3) Green else Line)
        Step("Details", 3, step)
    }
}

@Composable
private fun Step(label: String, number: Int, step: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(28.dp).clip(CircleShape).background(if (step >= number) Green else Color.White).border(1.dp, Green, CircleShape),
            contentAlignment = Alignment.Center
        ) { Text("$number", color = if (step >= number) Color.White else Green, fontWeight = FontWeight.Bold) }
        Text(label, fontSize = 11.sp)
    }
}

@Composable
private fun InfoCard(content: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(Color.White)) {
        Column(Modifier.padding(14.dp)) {
            content()
        }
    }
}

@Composable
private fun Tip(text: String) {
    Row(
        Modifier.fillMaxWidth().padding(top = 12.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Line, RoundedCornerShape(8.dp)).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LocationOn, null, tint = Blue)
        Spacer(Modifier.width(10.dp))
        Text("Tip: $text")
    }
}

@Composable
private fun Selector(text: String, onClick: () -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Line, RoundedCornerShape(8.dp)).clickable(onClick = onClick).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = if (text == "Search village") Muted else Ink, modifier = Modifier.weight(1f))
        Text("v", color = Muted)
    }
}

@Composable
private fun SeverityChip(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(42.dp).clip(RoundedCornerShape(7.dp)).background(if (selected) Red else Color.White).border(1.dp, if (selected) Red else Line, RoundedCornerShape(7.dp)).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Text(text, color = if (selected) Color.White else Ink, fontWeight = FontWeight.Bold) }
}

@Composable
private fun FilledTab(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(44.dp).clip(RoundedCornerShape(7.dp)).background(if (selected) Green else Color.White).border(1.dp, if (selected) Green else Line, RoundedCornerShape(7.dp)).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Text(text, color = if (selected) Color.White else Ink, fontWeight = FontWeight.Bold) }
}

@Composable
private fun FilterRow(items: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        items.forEach { item ->
            val isSelected = item == selected
            Box(
                Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(7.dp)).background(if (isSelected) Green else Color.White).border(1.dp, if (isSelected) Green else Line, RoundedCornerShape(7.dp)).clickable { onSelect(item) },
                contentAlignment = Alignment.Center
            ) { Text(item, color = if (isSelected) Color.White else Ink, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun StatusRow(village: String, status: String, time: String, badge: String, color: Color, onDelete: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(village, fontWeight = FontWeight.Bold)
            Text(status)
            Text(time, color = Muted, fontSize = 12.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.clip(RoundedCornerShape(7.dp)).background(color).padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(badge, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = Red.copy(alpha = 0.7f))
            }
        }
    }
    Divider(color = Line)
}

@Composable
private fun MaintenanceItem(section: String, work: String, date: String, isCompleted: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(section, fontWeight = FontWeight.Bold)
            Text(work)
            Text(date)
        }
        Box(Modifier.clip(RoundedCornerShape(8.dp)).background(if (isCompleted) Green.copy(alpha = 0.1f) else Color(0xFFFFF5D6)).padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(if (isCompleted) "Completed" else "Upcoming", color = if (isCompleted) Green else Brown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
    Divider(color = Line)
}

@Composable
private fun UpdateRow(title: String, time: String, color: Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(28.dp).clip(CircleShape).background(color.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Warning, null, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold)
            Text(time, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AlertRow(icon: ImageVector, color: Color, title: String, time: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 13.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(color.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(time, color = Muted, fontSize = 12.sp)
        }
    }
    Divider(color = Line)
}

@Composable
private fun MenuRow(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(Modifier.fillMaxWidth().height(52.dp).clickable(onClick = onClick), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Ink)
        Spacer(Modifier.width(14.dp))
        Text(text, modifier = Modifier.weight(1f))
        Text(">", fontSize = 24.sp, color = Muted)
    }
    Divider(color = Line)
}

@Composable
private fun Legend(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 10.sp)
    }
}

@Composable
private fun PhotoCard(kind: String) {
    val context = LocalContext.current
    Box(
        Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Line, RoundedCornerShape(8.dp)).clickable { Toast.makeText(context, "Opening Camera...", Toast.LENGTH_SHORT).show() }
    ) {
        CanalProblemDrawing(kind, Modifier.fillMaxSize())
        Box(
            Modifier.align(Alignment.BottomEnd).padding(12.dp).size(44.dp).clip(RoundedCornerShape(8.dp)).background(Color.White),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.PhotoCamera, null, tint = Ink) }
    }
}

@Composable
private fun CanalScene(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawRect(Brush.verticalGradient(listOf(Color(0xFF9DD9FF), Color(0xFF0E7A3D))))
        drawOval(Color(0xFF2F9D55), topLeft = Offset(-size.width * .25f, size.height * .48f), size = Size(size.width * .8f, size.height * .35f))
        drawOval(Color(0xFF74B86A), topLeft = Offset(size.width * .45f, size.height * .44f), size = Size(size.width * .8f, size.height * .28f))
        val canal = Path().apply {
            moveTo(size.width * .42f, size.height)
            lineTo(size.width * .50f, size.height * .48f)
            lineTo(size.width * .58f, size.height)
            close()
        }
        drawPath(canal, Color(0xFF4DB7E5))
        drawPath(canal, Color.White.copy(alpha = .45f), style = Stroke(4.dp.toPx()))
    }
}

@Composable
private fun SmallCanalIcon() {
    Canvas(Modifier.size(92.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE7F7FF))) {
        drawOval(Color(0xFF7FC96F), topLeft = Offset(-20f, 45f), size = Size(90f, 40f))
        drawOval(Color(0xFFBBD96D), topLeft = Offset(48f, 35f), size = Size(90f, 50f))
        drawCircle(Green, radius = 12f, center = Offset(35f, 35f))
        drawRect(GreenDark, topLeft = Offset(33f, 43f), size = Size(4f, 22f))
        val canal = Path().apply {
            moveTo(20f, 86f)
            lineTo(48f, 48f)
            lineTo(78f, 86f)
            close()
        }
        drawPath(canal, Color(0xFF45B4E8))
    }
}

@Composable
private fun CanalProblemDrawing(kind: String, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawRect(if (kind == "silt") Color(0xFFE3C39A) else Color(0xFF8EC7D7))
        drawOval(Color(0xFF8A5A2E), topLeft = Offset(-40f, size.height * .45f), size = Size(size.width * .7f, size.height * .55f))
        drawOval(Color(0xFFC48A4B), topLeft = Offset(size.width * .4f, size.height * .42f), size = Size(size.width * .8f, size.height * .5f))
        val water = Path().apply {
            moveTo(0f, size.height * .65f)
            cubicTo(size.width * .25f, size.height * .50f, size.width * .55f, size.height * .75f, size.width, size.height * .55f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(water, if (kind == "silt") Color(0xFF94723F) else Color(0xFF3FA7CE))
        if (kind == "breach") {
            drawCircle(Color.White.copy(alpha = .85f), radius = 30f, center = Offset(size.width * .55f, size.height * .62f))
            drawCircle(Color.White.copy(alpha = .65f), radius = 48f, center = Offset(size.width * .58f, size.height * .66f), style = Stroke(8f))
        }
    }
}

@Composable
private fun MapDrawing(modifier: Modifier = Modifier, overview: Boolean = false) {
    Canvas(modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFEFE6D6))) {
        repeat(if (overview) 7 else 6) { i ->
            val y = size.height * (i + 1) / 8f
            drawLine(Color.White.copy(alpha = .8f), Offset(0f, y), Offset(size.width, y + 30f), strokeWidth = 2f)
        }
        val river = Path().apply {
            moveTo(size.width * .05f, size.height * .82f)
            cubicTo(size.width * .25f, size.height * .70f, size.width * .22f, size.height * .45f, size.width * .48f, size.height * .45f)
            cubicTo(size.width * .70f, size.height * .45f, size.width * .65f, size.height * .20f, size.width * .90f, size.height * .12f)
        }
        drawPath(river, Blue, style = Stroke(6.dp.toPx(), cap = StrokeCap.Round))

        // Draw area labels
        val paint = android.graphics.Paint().apply {
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 12.dp.toPx()
            color = android.graphics.Color.BLACK
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }
        drawContext.canvas.nativeCanvas.drawText("Hulikere", size.width * 0.3f, size.height * 0.75f, paint)
        drawContext.canvas.nativeCanvas.drawText("Kudur", size.width * 0.7f, size.height * 0.35f, paint)

        val pins = if (overview) {
            listOf(
                Triple(.20f, .52f, Red), Triple(.35f, .70f, Blue), Triple(.52f, .45f, Green),
                Triple(.64f, .30f, Brown), Triple(.78f, .18f, Red), Triple(.86f, .55f, Green)
            )
        } else {
            listOf(Triple(.58f, .45f, Red), Triple(.70f, .25f, Blue), Triple(.43f, .66f, Blue))
        }
        pins.forEach { (x, y, color) ->
            drawCircle(color, radius = 12.dp.toPx(), center = Offset(size.width * x, size.height * y))
            drawCircle(Color.White, radius = 4.dp.toPx(), center = Offset(size.width * x, size.height * y))
        }
    }
}

private fun defaultUserProfile(phone: String = "+91 9876543210") = UserProfile(
    name = "Ramesh",
    phone = phone,
    email = "ramesh@example.com",
    occupation = "Farmer",
    village = "Hulikere Village"
)

private fun saveUserProfile(context: Context, profile: UserProfile) {
    context.getSharedPreferences("namma_nala_user", Context.MODE_PRIVATE).edit {
        putString("name", profile.name)
        putString("phone", profile.phone)
        putString("email", profile.email)
        putString("occupation", profile.occupation)
        putString("village", profile.village)
        putBoolean("logged_in", true)
    }
}

private fun loadUserProfile(context: Context): UserProfile? {
    val prefs = context.getSharedPreferences("namma_nala_user", Context.MODE_PRIVATE)
    if (!prefs.getBoolean("logged_in", false)) return null
    return UserProfile(
        name = prefs.getString("name", null) ?: return null,
        phone = prefs.getString("phone", "") ?: "",
        email = prefs.getString("email", "") ?: "",
        occupation = prefs.getString("occupation", "") ?: "",
        village = prefs.getString("village", "") ?: ""
    )
}

private fun clearUserProfile(context: Context) {
    context.getSharedPreferences("namma_nala_user", Context.MODE_PRIVATE).edit {
        clear()
    }
}
