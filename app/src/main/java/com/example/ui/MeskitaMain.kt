package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Task
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen {
    Welcome,
    Dashboard,
    Tasks,
    AddTask
}

@Composable
fun MeskitaMain(viewModel: TaskViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.Welcome) }
    val tasks by viewModel.tasksState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AcademicBackground)
    ) {
        Crossfade(
            targetState = currentScreen,
            animationSpec = tween(300),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.Welcome -> WelcomeScreen(
                    onStartClicked = { currentScreen = AppScreen.Dashboard }
                )
                AppScreen.Dashboard -> DashboardScreen(
                    tasks = tasks,
                    onNavigateToTasks = { currentScreen = AppScreen.Tasks },
                    onNavigateToAddTask = { currentScreen = AppScreen.AddTask },
                    onToggleTask = { viewModel.toggleTaskCompletion(it) },
                    onBottomNavSelected = { screenSelected ->
                        currentScreen = screenSelected
                    }
                )
                AppScreen.Tasks -> TaskListScreen(
                    tasks = tasks,
                    onToggleTask = { viewModel.toggleTaskCompletion(it) },
                    onDeleteTask = { viewModel.deleteTask(it) },
                    onNavigateToAddTask = { currentScreen = AppScreen.AddTask },
                    onBottomNavSelected = { screenSelected ->
                        currentScreen = screenSelected
                    }
                )
                AppScreen.AddTask -> AddTaskScreen(
                    onBackClicked = { currentScreen = AppScreen.Dashboard },
                    onTaskSaved = { title, course, date, time, priority, notes ->
                        viewModel.addTask(title, course, date, time, priority, notes)
                        currentScreen = AppScreen.Tasks
                    },
                    onBottomNavSelected = { screenSelected ->
                        currentScreen = screenSelected
                    }
                )
            }
        }
    }
}

// ---------------------------------------------------------
// ONBOARDING SCREEN
// ---------------------------------------------------------
@Composable
fun WelcomeScreen(onStartClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo & Title
            Row(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Meskita Logo",
                    tint = BentoPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Meskita",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoPrimary
                    )
                )
            }

            // Custom Gorgeous Illustration Representing Screen 3
            Box(
                modifier = Modifier
                    .size(width = 340.dp, height = 340.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(BentoLightPurple)
                    .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(32.dp))
                    .padding(16.dp)
            ) {
                // Isometric Stack of Books and Coffee cup
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerOffset = Offset(size.width / 2f, size.height / 2f)
                    
                    // Draw decorative ambient circles
                    drawCircle(
                        color = BentoPrimary.copy(alpha = 0.05f),
                        radius = 240f,
                        center = Offset(size.width * 0.8f, size.height * 0.2f)
                    )
                    drawCircle(
                        color = BentoPrimary.copy(alpha = 0.08f),
                        radius = 80f,
                        center = Offset(size.width * 0.2f, size.height * 0.4f)
                    )
                    // Draw table surface
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(20f, size.height * 0.65f)
                        lineTo(size.width - 20f, size.height * 0.5f)
                        lineTo(size.width - 60f, size.height * 0.95f)
                        lineTo(60f, size.height * 0.95f)
                        close()
                    }
                    drawPath(path, BentoGreyBlue.copy(alpha = 0.5f))
                }

                // Floating Tablets & books represented by nested Compose components
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-20).dp, x = (-10).dp)
                        .width(180.dp)
                        .height(200.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Tablet task list simulation
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BentoCoralPink)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.width(100.dp).height(6.dp).background(BentoGreyBlue))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BentoIceBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.width(80.dp).height(6.dp).background(BentoGreyBlue))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BentoSageGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.width(110.dp).height(6.dp).background(BentoGreyBlue))
                        }
                    }
                    
                    // Embedded floating smart pen
                    Box(
                        modifier = Modifier
                            .offset(x = 100.dp, y = 140.dp)
                            .width(6.dp)
                            .height(80.dp)
                            .clip(CircleShape)
                            .background(Brush.verticalGradient(listOf(BentoPrimary, BentoLightPurpleBorder)))
                    )
                }

                // Top-right overlapping chip: "Tugas Selesai!"
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = 12.dp, x = 12.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(BentoSageGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success tick",
                                tint = BentoOnSageGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Tugas Selesai!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoOnBackground
                            )
                            Text(
                                text = "Semua Tersimpan",
                                fontSize = 9.sp,
                                color = BentoOnBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Bottom-Left overlapping banner "Deadline Besok"
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = (-12).dp, x = (-8).dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(BentoDarkCharcoal)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Deadline Besok",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoOnDarkCharcoal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Onboarding Welcome Texts
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selamat Datang di Meskita",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoOnBackground,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Kelola tugas kampusmu dengan lebih teratur dan mudah. Fokus pada belajarmu, biarkan kami menjaga jadwalmu.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = BentoOnBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Get Started Action & Bullet indicator
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "Mulai Sekarang" button
                Button(
                    onClick = onStartClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Mulai Sekarang",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Arrow Forwards",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bullet indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(BentoPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(BentoLightPurpleBorder)
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(BentoLightPurpleBorder)
                    )
                }
            }
        }
    }
}


// ---------------------------------------------------------
// DASHBOARD SCREEN
// ---------------------------------------------------------
@Composable
fun DashboardScreen(
    tasks: List<Task>,
    onNavigateToTasks: () -> Unit,
    onNavigateToAddTask: () -> Unit,
    onToggleTask: (Task) -> Unit,
    onBottomNavSelected: (AppScreen) -> Unit
) {
    val activeTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    // Let's compute statistics dynamically based on current local task counts
    val activeThisWeekCount = activeTasks.size
    val progressPercent = if (tasks.isNotEmpty()) {
        ((completedTasks.size.toFloat() / tasks.size.toFloat()) * 100).toInt()
    } else {
        74 // Default placeholder aligned to design spec
    }

    Scaffold(
        containerColor = BentoBackground,
        bottomBar = {
            MeskitaBottomNavigation(
                currentScreen = AppScreen.Dashboard,
                onSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp) // standard padding for comfortable screen-edge distance
        ) {
            // Header Row: Meskita Logo & Title (Replaced Profile style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Meskita Logo",
                        tint = BentoPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Meskita",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimary
                        )
                    )
                }

                // Notification Bell with red bubble dot badge
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = BentoOnBackground,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .border(2.dp, BentoBackground, CircleShape)
                    )
                }
            }

            // Bento Grid Main Content Container
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // 1. Large Main Card: Energy Level / Semester Progress Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(BentoPrimaryContainer)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "PROGRES AKADEMIK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnPrimaryContainer.copy(alpha = 0.7f),
                                    letterSpacing = 0.1.sp
                                )
                                Text(
                                    text = "$progressPercent%",
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnPrimaryContainer,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.4f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "OPTIMAL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnPrimaryContainer
                                )
                            }
                        }

                        // Bottom part: Pill bars as design spec energy chart visualization
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                listOf(20.dp, 32.dp, 26.dp, 44.dp, 36.dp, 20.dp).forEachIndexed { idx, h ->
                                    val barAlpha = if (idx < 4) 1.0f else 0.4f
                                    Box(
                                        modifier = Modifier
                                            .width(8.dp)
                                            .height(h)
                                            .clip(CircleShape)
                                            .background(BentoOnPrimaryContainer.copy(alpha = barAlpha))
                                    )
                                }
                            }
                            Text(
                                text = "Minggu Ke-8",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoOnPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // 2. Row: Steps & Hydration equivalents for Academic tasks tracker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Small Square: Steps equivalent -> Tasks Active
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(BentoGreyBlue)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📚", fontSize = 18.sp)
                            }
                            Column {
                                Text(
                                    text = "$activeThisWeekCount",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnGreyBlue
                                )
                                Text(
                                    text = "TUGAS AKTIF",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnGreyBlue.copy(alpha = 0.8f),
                                    letterSpacing = 0.05.sp
                                )
                            }
                        }
                    }

                    // Small Square: Hydration equivalent -> Tasks Completed
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(BentoIceBlue)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✅", fontSize = 16.sp)
                            }
                            Column {
                                Text(
                                    text = "${completedTasks.size}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnIceBlue
                                )
                                Text(
                                    text = "SELAI / HISTORI",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnIceBlue.copy(alpha = 0.8f),
                                    letterSpacing = 0.05.sp
                                )
                            }
                        }
                    }
                }

                // 3. Wide Card: Focus Session simulation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(BentoLightPurple)
                        .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(28.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Focus minutes circular timer simulation
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = BentoPrimary.copy(alpha = 0.1f),
                                        style = Stroke(width = 8f)
                                    )
                                    drawArc(
                                        color = BentoPrimary,
                                        startAngle = -90f,
                                        sweepAngle = 270f,
                                        useCenter = false,
                                        style = Stroke(width = 8f)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("45m", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoPrimary)
                        }

                        // Text details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sesi Mandiri / Deep Work",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoOnBackground
                            )
                            Text(
                                text = "Metode Pomodoro Aktif",
                                fontSize = 11.sp,
                                color = BentoOnBackground.copy(alpha = 0.6f)
                            )
                        }

                        // Play circle action button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BentoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play icon",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // 4. Asymmetric Row: Sleep quality (Due tracking) & mood/heart rate (grades & pressure indicators)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Vertical Card: Sleep equivalent (Countdown / Next deadline tracker)
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .height(180.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(BentoDarkCharcoal)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("⏳", fontSize = 24.sp)
                            Column {
                                Text(
                                    text = "Sisa Tenggat",
                                    fontSize = 11.sp,
                                    color = BentoOnDarkCharcoal.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (activeTasks.isNotEmpty()) "8 Jam" else "Aman",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnDarkCharcoal
                                )
                            }
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.75f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(BentoCharcoalAccent)
                                    )
                                }
                                Text(
                                    text = "92% Kesiapan Tugas",
                                    fontSize = 9.sp,
                                    color = BentoOnDarkCharcoal.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Multi-Squares Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Square of Sage Green: Mood / IPK Grade equivalent
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(28.dp))
                                .background(BentoSageGreen)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🎓", fontSize = 18.sp)
                                Text(
                                    text = "3.85",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnSageGreen
                                )
                                Text(
                                    text = "IPK RATING",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnSageGreen.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // Square of Coral Pink: Heart rate / Priority stress indicator equivalent
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(28.dp))
                                .background(BentoCoralPink)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("❤️", fontSize = 18.sp)
                                Text(
                                    text = if (activeTasks.any { it.priority == "High" }) "Tinggi" else "Rendah",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnCoralPink
                                )
                                Text(
                                    text = "PRIORITAS",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnCoralPink.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }

            // Tasks list section header with "Lihat Semua"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tugas Hari Ini",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoOnBackground
                )
                Text(
                    text = "Lihat Semua",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimary,
                    modifier = Modifier.clickable { onNavigateToTasks() }
                )
            }

            // Short scrollable of 3 tasks today matching layout of Screen 1
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada tugas hari ini. Ketuk ikon '+' di bawah/Tasks untuk menambah!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoOnBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Display up to 3 today tasks
                val displayTasks = tasks.take(3)
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    displayTasks.forEach { task ->
                        DashboardTaskItem(
                            task = task,
                            onToggle = { onToggleTask(task) },
                            onItemClick = onNavigateToTasks
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardTaskItem(
    task: Task, 
    onToggle: () -> Unit,
    onItemClick: () -> Unit
) {
    // Generate styling based on course categories & status matching Bento Grid theme
    val colorBar = when (task.course.lowercase()) {
        "matematika diskrit", "advanced mathematics" -> BentoPrimary
        "algoritma & struktur data" -> BentoSageGreen
        "bahasa inggris" -> BentoGreyBlue
        else -> BentoIceBlue
    }

    val badgeBg = if (task.isCompleted) {
        BentoGreyBlue
    } else if (task.priority == "High") {
        BentoCoralPink
    } else if (task.priority == "Mid") {
        BentoPrimaryContainer
    } else {
        BentoLightPurple
    }

    val badgeTextColor = if (task.isCompleted) {
        BentoOnGreyBlue
    } else if (task.priority == "High") {
        BentoOnCoralPink
    } else if (task.priority == "Mid") {
        BentoOnPrimaryContainer
    } else {
        BentoPrimary
    }

    val badgeText = if (task.isCompleted) {
        "Selesai"
    } else if (task.priority == "High") {
        "Prioritas"
    } else {
        "Besok"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Priority Visual strip on the left edge
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorBar)
            )

            // Content Block
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(18.dp)
            ) {
                // Top line: Course Name + Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.course.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoPrimary,
                        letterSpacing = 0.05.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(badgeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Mid Line: Task Title
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) BentoOnBackground.copy(alpha = 0.4f) else BentoOnBackground,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Line: Date and Footer Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed mark",
                            tint = BentoSageGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Terkirim",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnSageGreen
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "Deadline info",
                            tint = BentoOnBackground.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Deadline: ${task.deadlineDate} ${task.deadlineTime}",
                            fontSize = 12.sp,
                            color = BentoOnBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Right check button with subtle styling
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoSageGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onToggle) {
                            Icon(
                                imageVector = Icons.Outlined.DoneAll,
                                contentDescription = "Finished status",
                                tint = BentoOnSageGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoLightPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onToggle) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Edit check action",
                                tint = BentoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


// ---------------------------------------------------------
// TASKS MASTER LIST SCREEN (Active / Completed filters)
// ---------------------------------------------------------
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    onToggleTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onNavigateToAddTask: () -> Unit,
    onBottomNavSelected: (AppScreen) -> Unit
) {
    var filterTabSelected by remember { mutableStateOf(0) } // 0 = Sedang Berjalan, 1 = Selesai

    val filteredTasks = tasks.filter {
        if (filterTabSelected == 0) !it.isCompleted else it.isCompleted
    }

    // Group tasks by Course category for elegant clean UI
    val groupedTasks = filteredTasks.groupBy { it.course }

    Scaffold(
        containerColor = BentoBackground,
        bottomBar = {
            MeskitaBottomNavigation(
                currentScreen = AppScreen.Tasks,
                onSelected = onBottomNavSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = BentoPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambahkan Tugas",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Top Bar Branding Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meskita",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoPrimary
                    )
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = BentoOnBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Dual Tab Buttons "Sedang Berjalan" & "Selesai" matching design Screen 4
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tab "Sedang Berjalan"
                ItemTabButton(
                    label = "Sedang Berjalan",
                    isSelected = filterTabSelected == 0,
                    onClick = { filterTabSelected = 0 },
                    modifier = Modifier.weight(1.5f)
                )

                // Tab "Selesai"
                ItemTabButton(
                    label = "Selesai",
                    isSelected = filterTabSelected == 1,
                    onClick = { filterTabSelected = 1 },
                    modifier = Modifier.weight(1f)
                )
            }

            // Categorized List
            if (groupedTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (filterTabSelected == 0) Icons.Outlined.AssignmentLate else Icons.Outlined.Check,
                            contentDescription = "No tasks",
                            tint = BentoLightPurpleBorder,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (filterTabSelected == 0) "Belum ada tugas terjadwal." else "Belum ada yang diselesaikan.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = BentoOnBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    groupedTasks.forEach { (courseCategory, tasksInGroup) ->
                        item {
                            // Category Row with Left Dot Bar indicator matching Screen 4
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 4.dp, height = 18.dp)
                                        .clip(CircleShape)
                                        .background(BentoPrimary)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = courseCategory,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoOnBackground
                                )
                            }
                        }

                        items(tasksInGroup, key = { it.id }) { task ->
                            TaskRowItem(
                                task = task,
                                onCompletedToggle = { onToggleTask(task) },
                                onDelete = { onDeleteTask(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) BentoPrimary else BentoLightPurple,
            contentColor = if (isSelected) Color.White else BentoOnBackground.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TaskRowItem(
    task: Task,
    onCompletedToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(text = "Hapus Tugas") },
            text = { Text(text = "Apakah Anda yakin ingin menghapus '${task.title}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text(text = "Hapus", color = BentoOnCoralPink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(text = "Batal", color = BentoOnBackground.copy(alpha = 0.6f))
                }
            }
        )
    }

    // Main Card Row of Tasks
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(24.dp))
            .combinedClickable(
                onClick = onCompletedToggle,
                onLongClick = { showDeleteConfirm = true }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Task completed Check Box (or square custom empty button)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        2.dp,
                        if (task.isCompleted) BentoSageGreen else BentoPrimary.copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (task.isCompleted) BentoSageGreen.copy(alpha = 0.15f) else Color.Transparent
                    )
                    .clickable { onCompletedToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Finished",
                        tint = BentoOnSageGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text and descriptions
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) BentoOnBackground.copy(alpha = 0.4f) else BentoOnBackground,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Deadline: ${task.deadlineDate} • ${task.deadlineTime}",
                    fontSize = 12.sp,
                    color = BentoOnBackground.copy(alpha = 0.6f)
                )

                if (task.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.notes,
                        fontSize = 11.sp,
                        color = BentoOnBackground.copy(alpha = 0.5f),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Relative Badge H-2 / Upcoming
            val badgeColorBg = if (task.isCompleted) {
                BentoGreyBlue
            } else if (task.priority == "High") {
                BentoCoralPink
            } else {
                BentoIceBlue
            }

            val badgeColorText = if (task.isCompleted) {
                BentoOnGreyBlue
            } else if (task.priority == "High") {
                BentoOnCoralPink
            } else {
                BentoOnIceBlue
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(badgeColorBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (task.isCompleted) "Selesai" else if (task.priority == "High") "Prioritas" else "Upcoming",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColorText
                )
            }
        }
    }
}


// ---------------------------------------------------------
// ADD TASK SCREEN
// ---------------------------------------------------------
@Composable
fun AddTaskScreen(
    onBackClicked: () -> Unit,
    onTaskSaved: (title: String, course: String, date: String, time: String, priority: String, notes: String) -> Unit,
    onBottomNavSelected: (AppScreen) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("UI/UX Design") }
    var selectedPriority by remember { mutableStateOf("Low") }
    var dateString by remember { mutableStateOf("") }

    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isCourseDropdownVisible by remember { mutableStateOf(false) }

    val courseList = listOf(
        "UI/UX Design",
        "Matematika Diskrit",
        "Algoritma & Struktur Data",
        "Advanced Mathematics",
        "Computer Networks",
        "Bahasa Inggris"
    )

    Scaffold(
        containerColor = BentoBackground,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = "Add Task",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoOnBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = BentoPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BentoBackground)
            )
        },
        bottomBar = {
            MeskitaBottomNavigation(
                currentScreen = AppScreen.AddTask,
                onSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Create New Task Title Headers
            Text(
                text = "Create New Task",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoOnBackground
                )
            )
            Text(
                text = "Keep your academic journey organized and stress-free.",
                fontSize = 14.sp,
                color = BentoOnBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nama Tugas Input Field
            FormLabel(text = "Nama Tugas")
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = { Text(text = "e.g. Analisis Struktur Data", color = BentoOnBackground.copy(alpha = 0.4f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit symbol",
                        tint = BentoOnBackground.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BentoPrimary,
                    unfocusedBorderColor = BentoLightPurpleBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Mata Kuliah Picker (Dropdown overlay)
            FormLabel(text = "Mata Kuliah")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = selectedCourse,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCourseDropdownVisible = true },
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        IconButton(onClick = { isCourseDropdownVisible = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown select",
                                tint = BentoOnBackground.copy(alpha = 0.5f)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BentoPrimary,
                        unfocusedBorderColor = BentoLightPurpleBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                DropdownMenu(
                    expanded = isCourseDropdownVisible,
                    onDismissRequest = { isCourseDropdownVisible = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    courseList.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(text = course) },
                            onClick = {
                                selectedCourse = course
                                isCourseDropdownVisible = false
                            }
                        )
                    }
                }
            }

            // Tenggat Waktu Picker Field
            FormLabel(text = "Tenggat Waktu")
            OutlinedTextField(
                value = if (dateString.isEmpty()) "dd/mm/yyyy" else dateString,
                onValueChange = { dateString = it },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { isDatePickerVisible = true },
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    IconButton(onClick = { isDatePickerVisible = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Calendar Picker",
                            tint = BentoOnBackground.copy(alpha = 0.5f)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BentoPrimary,
                    unfocusedBorderColor = BentoLightPurpleBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Dynamic Priority select Buttons: Low | Mid | High
            FormLabel(text = "Prioritas")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Low", "Mid", "High").forEach { priority ->
                    val isPrioritySelected = selectedPriority == priority
                    val priorityColorBg = when (priority) {
                        "Low" -> BentoSageGreen
                        "Mid" -> BentoIceBlue
                        else -> BentoCoralPink
                    }
                    val priorityColorText = when (priority) {
                        "Low" -> BentoOnSageGreen
                        "Mid" -> BentoOnIceBlue
                        else -> BentoOnCoralPink
                    }
                    Button(
                        onClick = { selectedPriority = priority },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPrioritySelected) priorityColorBg else Color.White,
                            contentColor = if (isPrioritySelected) priorityColorText else BentoOnBackground.copy(alpha = 0.6f)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isPrioritySelected) Color.Transparent else BentoLightPurpleBorder
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = priority,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Catatan Text field
            FormLabel(text = "Catatan")
            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                placeholder = { Text(text = "Tambahkan detail atau instruksi tugas di sini...", color = BentoOnBackground.copy(alpha = 0.4f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BentoPrimary,
                    unfocusedBorderColor = BentoLightPurpleBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Custom Dynamic Book visual banner matching Screen 2 bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(BentoLightPurple)
                    .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Book,
                        contentDescription = "Decorative Book Design",
                        tint = BentoPrimary.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Matrikulasi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BentoPrimary
                        )
                        Text(
                            text = "Isi detail tugas dengan jelas untuk mempermudah pengerjaan saat belajar nanti.",
                            fontSize = 11.sp,
                            color = BentoOnBackground.copy(alpha = 0.7f),
                            lineHeight = 16.sp,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary 'Simpan Tugas' button
            Button(
                onClick = {
                    if (taskName.isNotEmpty()) {
                        val finalDate = if (dateString.isEmpty()) {
                            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
                            sdf.format(Date())
                        } else {
                            dateString
                        }
                        onTaskSaved(
                            taskName,
                            selectedCourse,
                            finalDate,
                            "23:59",
                            selectedPriority,
                            notesText
                        )
                    }
                },
                enabled = taskName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BentoPrimary,
                    disabledContainerColor = BentoLightPurpleBorder
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Simpan",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simpan Tugas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Modal Simple Date Selection Dialog
    if (isDatePickerVisible) {
        SimpleStringDatePickerDialog(
            currentDateStr = dateString,
            onDismiss = { isDatePickerVisible = false },
            onDateSelected = { selected ->
                dateString = selected
                isDatePickerVisible = false
            }
        )
    }
}

@Composable
fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BentoOnBackground,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

// Custom real-time calendar date selector popup
fun getIndonesianMonthName(month: Int): String {
    return when (month) {
        0 -> "Januari"
        1 -> "Februari"
        2 -> "Maret"
        3 -> "April"
        4 -> "Mei"
        5 -> "Juni"
        6 -> "Juli"
        7 -> "Agustus"
        8 -> "September"
        9 -> "Oktober"
        10 -> "November"
        11 -> "Desember"
        else -> ""
    }
}

fun getIndonesianMonthAbbr(month: Int): String {
    return when (month) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "Mei"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Agt"
        8 -> "Sep"
        9 -> "Okt"
        10 -> "Nov"
        11 -> "Des"
        else -> ""
    }
}

fun parseDateStringToCalendar(dateStr: String): Calendar {
    val cal = Calendar.getInstance()
    if (dateStr.isEmpty() || dateStr == "dd/mm/yyyy") return cal
    try {
        val parts = dateStr.trim().split(" ")
        if (parts.size == 3) {
            val day = parts[0].toIntOrNull() ?: return cal
            val monthAbbr = parts[1]
            val year = parts[2].toIntOrNull() ?: return cal
            
            val month = when (monthAbbr.lowercase(Locale.ROOT)) {
                "jan" -> 0
                "feb" -> 1
                "mar" -> 2
                "apr" -> 3
                "mei" -> 4
                "jun" -> 5
                "jul" -> 6
                "agt", "agu" -> 7
                "sep" -> 8
                "okt" -> 9
                "nov" -> 10
                "des" -> 11
                else -> -1
            }
            if (month != -1) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
            }
        }
    } catch (e: Exception) {
        // Fallback to today
    }
    return cal
}

@Composable
fun SimpleStringDatePickerDialog(
    currentDateStr: String,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val initialCalendar = remember(currentDateStr) {
        parseDateStringToCalendar(currentDateStr)
    }

    var selectedDay by remember { mutableStateOf(initialCalendar.get(Calendar.DAY_OF_MONTH)) }
    var selectedMonth by remember { mutableStateOf(initialCalendar.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(initialCalendar.get(Calendar.YEAR)) }

    var viewMonth by remember { mutableStateOf(initialCalendar.get(Calendar.MONTH)) }
    var viewYear by remember { mutableStateOf(initialCalendar.get(Calendar.YEAR)) }

    val todayCalendar = remember { Calendar.getInstance() }
    val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)
    val todayMonth = todayCalendar.get(Calendar.MONTH)
    val todayYear = todayCalendar.get(Calendar.YEAR)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pilih Tenggat Tanggal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BentoPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Header Month / Year Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (viewMonth == 0) {
                                viewMonth = 11
                                viewYear -= 1
                            } else {
                                viewMonth -= 1
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Bulan Sebelumnya",
                            tint = BentoPrimary
                        )
                    }

                    Text(
                        text = "${getIndonesianMonthName(viewMonth)} $viewYear",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoOnBackground
                        )
                    )

                    IconButton(
                        onClick = {
                            if (viewMonth == 11) {
                                viewMonth = 0
                                viewYear += 1
                            } else {
                                viewMonth += 1
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Bulan Selanjutnya",
                            tint = BentoPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Days of week header
                val daysOfWeek = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { dayText ->
                        Text(
                            text = dayText,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoOnBackground.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Monthly Grid
                val helperCal = remember(viewYear, viewMonth) {
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, viewYear)
                        set(Calendar.MONTH, viewMonth)
                        set(Calendar.DAY_OF_MONTH, 1)
                    }
                }
                val maxDays = helperCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                val firstDayOfWeek = helperCal.get(Calendar.DAY_OF_WEEK) // SUNDAY = 1, MONDAY = 2...
                // Adjusting Sunday to be index 6, Monday to be index 0
                val offset = (firstDayOfWeek + 5) % 7

                val daysList = remember(maxDays, offset) {
                    val list = mutableListOf<Int?>()
                    for (i in 0 until offset) {
                        list.add(null)
                    }
                    for (day in 1..maxDays) {
                        list.add(day)
                    }
                    while (list.size % 7 != 0) {
                        list.add(null)
                    }
                    list
                }

                val chunkedDays = remember(daysList) {
                    daysList.chunked(7)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    chunkedDays.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                if (day == null) {
                                    Spacer(modifier = Modifier.weight(1f))
                                } else {
                                    val isSelected = selectedDay == day && selectedMonth == viewMonth && selectedYear == viewYear
                                    val isToday = day == todayDay && viewMonth == todayMonth && viewYear == todayYear

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                when {
                                                    isSelected -> BentoPrimary
                                                    isToday -> BentoLightPurple
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                width = if (isToday && !isSelected) 1.dp else 0.dp,
                                                color = if (isToday && !isSelected) BentoLightPurpleBorder else Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                selectedDay = day
                                                selectedMonth = viewMonth
                                                selectedYear = viewYear
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                color = when {
                                                    isSelected -> Color.White
                                                    else -> BentoOnBackground
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Highlight of the selected date
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BentoLightPurple)
                        .border(1.dp, BentoLightPurpleBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Selected Date",
                            tint = BentoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val formattedSelectStr = "$selectedDay ${getIndonesianMonthName(selectedMonth)} $selectedYear"
                        Text(
                            text = "Terpilih: $formattedSelectStr",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoOnBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Batal", color = BentoOnBackground.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val resultStr = "$selectedDay ${getIndonesianMonthAbbr(selectedMonth)} $selectedYear"
                            onDateSelected(resultStr)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Pilih", color = Color.White)
                    }
                }
            }
        }
    }
}


// ---------------------------------------------------------
// CUSTOM BOTTOM NAVIGATION BAR
// ---------------------------------------------------------
@Composable
fun MeskitaBottomNavigation(
    currentScreen: AppScreen,
    onSelected: (AppScreen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(BorderStroke(1.dp, BentoLightPurpleBorder.copy(alpha = 0.5f)))
    ) {
        // Items: 1. Dashboard
        NavigationBarItem(
            selected = currentScreen == AppScreen.Dashboard,
            onClick = { onSelected(AppScreen.Dashboard) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.Dashboard) Icons.Default.GridView else Icons.Outlined.GridView,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Dashboard", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = BentoPrimary,
                unselectedIconColor = BentoOnBackground.copy(alpha = 0.4f),
                unselectedTextColor = BentoOnBackground.copy(alpha = 0.4f),
                indicatorColor = BentoPrimary
            )
        )

        // Items: 2. Tasks
        NavigationBarItem(
            selected = currentScreen == AppScreen.Tasks,
            onClick = { onSelected(AppScreen.Tasks) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == AppScreen.Tasks) Icons.Default.Assignment else Icons.Outlined.Assignment,
                    contentDescription = "Tasks"
                )
            },
            label = { Text("Tasks", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = BentoPrimary,
                unselectedIconColor = BentoOnBackground.copy(alpha = 0.4f),
                unselectedTextColor = BentoOnBackground.copy(alpha = 0.4f),
                indicatorColor = BentoPrimary
            )
        )

        // Items: 3. Add Task
        NavigationBarItem(
            selected = currentScreen == AppScreen.AddTask,
            onClick = { onSelected(AppScreen.AddTask) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BentoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = { Text("Add", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = BentoPrimary,
                unselectedIconColor = BentoOnBackground.copy(alpha = 0.4f),
                unselectedTextColor = BentoOnBackground.copy(alpha = 0.4f),
                indicatorColor = Color.Transparent
            )
        )
    }
}
