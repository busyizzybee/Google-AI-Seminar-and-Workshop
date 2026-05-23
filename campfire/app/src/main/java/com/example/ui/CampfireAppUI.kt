package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CampfireAppUI(viewModel: CampfireViewModel) {
    val activeSection by viewModel.activeSection.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            // Elegant M3 Navigation Bar with proper system safe areas
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("app_navigation_bar"),
                containerColor = Color(0xFFF5ECE9),
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = activeSection == "Admin Board",
                    onClick = { viewModel.activeSection.value = "Admin Board" },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Operating System") },
                    label = { Text("Admin OS", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CampfireOnBg,
                        selectedTextColor = CampfireOnBg,
                        indicatorColor = CampfireAccentGlow,
                        unselectedIconColor = CampfireMuted,
                        unselectedTextColor = CampfireMuted
                    )
                )
                NavigationBarItem(
                    selected = activeSection == "Member Hub",
                    onClick = { viewModel.activeSection.value = "Member Hub" },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Member Companion") },
                    label = { Text("Member Hub", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CampfireOnBg,
                        selectedTextColor = CampfireOnBg,
                        indicatorColor = CampfireAccentGlow,
                        unselectedIconColor = CampfireMuted,
                        unselectedTextColor = CampfireMuted
                    )
                )
            }
        },
        containerColor = CampfireDarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CampfireDarkBackground,
                            Color(0xFFF5ECE9)
                        )
                    )
                )
        ) {
            // Beautiful Abstract Glowing Campfire Brush in the background top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CampfirePrimary.copy(alpha = 0.12f),
                                    Color.Transparent
                                )
                            ),
                            radius = 600f,
                            center = Offset(size.width / 2f, 0f)
                        )
                    }
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Header Block with adaptive branding & notifications
                HeaderBlock(viewModel = viewModel, notifications = notifications)

                // Main Navigation Swapper
                AnimatedContent(
                    targetState = activeSection,
                    transitionSpec = {
                        slideInHorizontally { width -> if (targetState == "Member Hub") width else -width } + fadeIn() with
                                slideOutHorizontally { width -> if (targetState == "Member Hub") -width else width } + fadeOut()
                    },
                    modifier = Modifier.weight(1f)
                ) { targetSection ->
                    when (targetSection) {
                        "Admin Board" -> AdminOSLayout(viewModel = viewModel)
                        "Member Hub" -> MemberHubLayout(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// COMPONENTS: HEADER & INTEGRATION METRIC BARS
// ----------------------------------------------------

@Composable
fun HeaderBlock(
    viewModel: CampfireViewModel,
    notifications: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campfire Generated Rounded Icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CampfireDarkSurface)
                    .border(1.dp, CampfireCardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.campfire_logo),
                    contentDescription = "Campfire Logo",
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "CAMPFIRE",
                    style = MaterialTheme.typography.titleMedium,
                    color = CampfireOnBg,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "THE ORG OPERATING SYSTEM",
                    style = MaterialTheme.typography.labelSmall,
                    color = CampfirePrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Connection Badge reflecting "Live Firebase Unified Sync" status
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2E7D32).copy(alpha = 0.08f))
                    .border(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2E7D32))
                    )
                    Text(
                        text = "SYNCED",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Live alert/trigger strip presenting cross-committee triggers and dynamic notifications
        if (notifications.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CampfirePrimary.copy(alpha = 0.08f))
                    .border(1.dp, CampfirePrimary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = "Notification alert",
                        tint = CampfirePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = notifications.first(),
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireOnBg,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// ====================================================
// VIEW: ADMIN OPERATING SYSTEM (5 MAIN MODULES)
// ====================================================

@Composable
fun AdminOSLayout(viewModel: CampfireViewModel) {
    var adminTab by remember { mutableStateOf("Induction") } // Induction, Vault, Ledger, Pulse, Handoff
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Thumb-friendly scrollable horizontal options for sections
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                "Induction" to Icons.Default.HowToReg,
                "Vault" to Icons.Default.FolderZip,
                "Ledger" to Icons.Default.AccountBalanceWallet,
                "Pulse" to Icons.Default.CompassCalibration,
                "Handoff" to Icons.Default.AccountTree
            )
            items(tabs) { (name, icon) ->
                val isSelected = adminTab == name
                FilterChip(
                    selected = isSelected,
                    onClick = { adminTab = name },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(icon, contentDescription = name, modifier = Modifier.size(16.dp))
                            Text(name, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CampfirePrimary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = CampfireDarkSurface,
                        labelColor = CampfireOnBg
                    ),
                    // Let border use default for maximum stability across M3 compiles
                )
            }
        }

        // Subpages Render
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when (adminTab) {
                "Induction" -> InductionSection(viewModel = viewModel)
                "Vault" -> VaultSection(viewModel = viewModel)
                "Ledger" -> LedgerSection(viewModel = viewModel)
                "Pulse" -> PulseSection(viewModel = viewModel)
                "Handoff" -> HandoffSection(viewModel = viewModel)
            }
        }
    }
}

// ----------------------------------------------------
// 1. INDUCTION PIPELINE (Recruitment & Onboarding)
// ----------------------------------------------------
@Composable
fun InductionSection(viewModel: CampfireViewModel) {
    val applicants by viewModel.applicants.collectAsState()
    var showForm by remember { mutableStateOf(false) }

    // Form inputs
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var interestInput by remember { mutableStateOf("UI/UX Design") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "THE INDUCTION PIPELINE",
                        style = MaterialTheme.typography.titleSmall,
                        color = CampfirePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Transition applicants dynamically into organization members. Accepting creates their onboarding game plan automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showForm = !showForm },
                        colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (showForm) "Close Application Sheet" else "Submit New Application", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                    border = BorderStroke(1.dp, CampfirePrimary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("New Recruitment Registration", color = CampfireOnBg, fontWeight = FontWeight.Bold)
                        TextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("add_applicant_name")
                        )
                        TextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Student Email") },
                            modifier = Modifier.fillMaxWidth().testTag("add_applicant_email")
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Interests / Tracks: ", style = MaterialTheme.typography.bodySmall, color = CampfireOnBg)
                            Spacer(modifier = Modifier.weight(1f))
                            val interestsList = listOf("UI/UX Design", "Tech & Kotlin", "Marketing", "Finance")
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                TextButton(onClick = { expanded = true }) {
                                    Text(interestInput, color = CampfirePrimary)
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    interestsList.forEach { track ->
                                        DropdownMenuItem(
                                            text = { Text(track) },
                                            onClick = {
                                                interestInput = track
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = {
                                if (nameInput.isNotBlank() && emailInput.isNotBlank()) {
                                    viewModel.addNewApplicant(
                                        name = nameInput,
                                        email = emailInput,
                                        interests = interestInput
                                    )
                                    nameInput = ""
                                    emailInput = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Register Candidate", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        items(applicants) { applicant ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(
                    1.dp,
                    when (applicant.status) {
                        "Accepted" -> Color(0xFF2E7D32).copy(alpha = 0.3f)
                        "Rejected" -> Color(0xFFBA1A1A).copy(alpha = 0.2f)
                        else -> CampfireCardBorder
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(applicant.name, style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                            Text(applicant.email, style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                        }
                        
                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (applicant.status) {
                                        "Accepted" -> Color(0xFF2E7D32).copy(alpha = 0.08f)
                                        "Rejected" -> Color(0xFFBA1A1A).copy(alpha = 0.08f)
                                        else -> CampfirePrimary.copy(alpha = 0.08f)
                                    }
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = applicant.status.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (applicant.status) {
                                    "Accepted" -> Color(0xFF2E7D32)
                                    "Rejected" -> Color(0xFFBA1A1A)
                                    else -> CampfirePrimary
                                }
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp), color = CampfireCardBorder)

                    Text(
                        text = "Mapped Interest: ${applicant.interests}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireOnBg
                    )

                    if (applicant.status == "Pending") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            var committeeSelection by remember { mutableStateOf("Marketing") }
                            var showSelect by remember { mutableStateOf(false) }

                            if (!showSelect) {
                                Button(
                                    onClick = { showSelect = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept...", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.toggleApplicantStatus(applicant, "Rejected") },
                                    border = BorderStroke(1.dp, Color(0xFFBA1A1A)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA1A1A)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                // Dynamic committee assignment picker inline
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Select Committee Assignment:", style = MaterialTheme.typography.labelSmall, color = CampfirePrimary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf("Tech", "Marketing", "Finance").forEach { com ->
                                            Box(
                                                modifier = Modifier
                                                    .border(
                                                        1.dp,
                                                        if (committeeSelection == com) CampfirePrimary else CampfireCardBorder,
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .clickable { committeeSelection = com }
                                                    .background(if (committeeSelection == com) CampfirePrimary.copy(alpha = 0.08f) else Color.Transparent)
                                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                            ) {
                                                Text(com, color = if (committeeSelection == com) CampfirePrimary else CampfireMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF2E7D32))
                                        .clickable { viewModel.toggleApplicantStatus(applicant, "Accepted", committeeSelection) }
                                        .padding(12.dp)
                                ) {
                                    Icon(Icons.Default.Check, "Confirm", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    } else if (applicant.status == "Accepted") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Assigned Department: ${applicant.assignedCommittee.uppercase()} Committee",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampfirePrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎮 GAMIFIED ONBOARDING CHECKLIST:",
                            style = MaterialTheme.typography.labelSmall,
                            color = CampfireOnBg,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        applicant.onboardingState.split(", ").forEachIndexed { i, task ->
                            val label = task.substringBefore(" [")
                            val isDone = task.contains("[Done]")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleOnboardingTask(applicant, i) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = "checkbox",
                                    tint = if (isDone) Color(0xFF2E7D32) else CampfireMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDone) Color(0xFF2E7D32) else CampfireOnBg
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2. LIVING TURNOVERS & THE VAULT (Knowledge & Documentation)
// ----------------------------------------------------
@Composable
fun VaultSection(viewModel: CampfireViewModel) {
    val documents by viewModel.documents.collectAsState()
    val activeRole by viewModel.selectedVaultRole.collectAsState()
    var showAddDoc by remember { mutableStateOf(false) }

    var inputTitle by remember { mutableStateOf("") }
    var inputContent by remember { mutableStateOf("") }
    var inputCategory by remember { mutableStateOf("Template") } // Budget Ledger, Template, Terminal Report

    val roles = listOf("VP of Finance", "President", "VP of Education", "Logistics Lead")

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Explanatory Intro Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
            border = BorderStroke(1.dp, CampfireCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    "LIVING TURNOVERS & THE VAULT",
                    style = MaterialTheme.typography.titleSmall,
                    color = CampfirePrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Role-centric, permanent knowledge storage to bridge institutional memory during graduation transitions.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CampfireMuted
                )
            }
        }

        // Horizontal Role selector
        Text("FILTER SECURE REPOSITORY BY OFFICER ROLE:", style = MaterialTheme.typography.labelSmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(roles) { role ->
                val isSel = activeRole == role
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) CampfirePrimary else CampfireDarkSurface)
                        .clickable { viewModel.selectedVaultRole.value = role }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .border(1.dp, if (isSel) CampfirePrimary else CampfireCardBorder, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        role,
                        color = if (isSel) Color.White else CampfireOnBg,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Divider(color = CampfireCardBorder)

        Button(
            onClick = { showAddDoc = !showAddDoc },
            colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.UploadFile, "Upload File Icon", modifier = Modifier.size(16.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (showAddDoc) "Cancel Catalog Entry" else "Register File in the Vault", fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (showAddDoc) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfirePrimary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Secure Document Cataloging", color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    TextField(value = inputTitle, onValueChange = { inputTitle = it }, label = { Text("Document Title") }, modifier = Modifier.fillMaxWidth())
                    
                    Text("Select Ledger Category:", style = MaterialTheme.typography.labelSmall, color = CampfireOnBg)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Budget Ledger", "Template", "Terminal Report").forEach { c ->
                            val isSel = inputCategory == c
                            Box(
                                modifier = Modifier
                                    .background(if (isSel) CampfirePrimary.copy(alpha = 0.08f) else Color.Transparent, RoundedCornerShape(6.dp))
                                    .border(1.dp, if (isSel) CampfirePrimary else CampfireCardBorder, RoundedCornerShape(6.dp))
                                    .clickable { inputCategory = c }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(c, color = if (isSel) CampfirePrimary else CampfireMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    TextField(
                        value = inputContent,
                        onValueChange = { inputContent = it },
                        label = { Text("Document Contents / Description / Links") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )

                    Button(
                        onClick = {
                            if (inputTitle.isNotBlank() && inputContent.isNotBlank()) {
                                viewModel.createVaultDocument(
                                    title = inputTitle,
                                    role = activeRole,
                                    category = inputCategory,
                                    content = inputContent
                                )
                                inputTitle = ""
                                inputContent = ""
                                showAddDoc = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Document To Role Ledger", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Documents list
        val filteredDocs = documents.filter { it.role == activeRole }
        if (filteredDocs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.FolderOpen, "Empty docs", tint = CampfireMuted, modifier = Modifier.size(48.dp))
                    Text("No records found in this Vault role.", color = CampfireMuted, textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredDocs) { doc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                        border = BorderStroke(1.dp, CampfireCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CampfirePrimary.copy(alpha = 0.08f))
                                        .padding(6.dp)
                                ) {
                                    Icon(Icons.Default.Article, "Doc Icon", tint = CampfirePrimary, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(doc.title, color = CampfireOnBg, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(doc.category, color = CampfirePrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                doc.content,
                                color = CampfireMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. THE LEDGER (Compliance, Finance & Partnerships)
// ----------------------------------------------------
@Composable
fun LedgerSection(viewModel: CampfireViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val crmDeals by viewModel.crmDeals.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var descIn by remember { mutableStateOf("") }
    var amountIn by remember { mutableStateOf("") }
    var typeIn by remember { mutableStateOf("Expense") }
    var categoryIn by remember { mutableStateOf("Logistics Flow") }

    var ledgerSubTab by remember { mutableStateOf("Transactions") } // Transactions, CRM Pipeline

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Header Switcher
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Transactions", "CRM Pipeline").forEach { sub ->
                val isSel = ledgerSubTab == sub
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSel) CampfirePrimary else CampfireDarkSurface)
                        .clickable { ledgerSubTab = sub }
                        .padding(vertical = 10.dp)
                        .border(1.dp, if (isSel) CampfirePrimary else CampfireCardBorder),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        sub,
                        color = if (isSel) Color.White else CampfireOnBg,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        if (ledgerSubTab == "Transactions") {
            // Ledger summary block
            val totalIn = transactions.filter { it.type == "Income" }.sumOf { it.amount }
            val totalOut = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
            val net = totalIn - totalOut

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("TOTAL FUNDS", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                        Text("$${"%.2f".format(net)}", style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("COMPLIANCE RATE", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                        Text("100% SECURE", style = MaterialTheme.typography.titleMedium, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Liquidation receipt uploader / adding row
            Button(
                onClick = { showForm = !showForm },
                colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ReceiptLong, "Receipt", modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (showForm) "Close Liquidation Panel" else "File Liquidation / Receipt", fontWeight = FontWeight.Bold, color = Color.White)
            }

            if (showForm) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                    border = BorderStroke(1.dp, CampfirePrimary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Drag & Drop Receipt Upload Simulation", fontWeight = FontWeight.Bold, color = CampfireOnBg)
                        
                        // Fake Drag Area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CampfireDarkBackground)
                                .border(1.dp, CampfirePrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .clickable {
                                    descIn = "Liquidating Receipt #GA-${(1000..9999).random()}"
                                    amountIn = "45.00"
                                    categoryIn = "Assembly logistics"
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.CheckCircle, "Uploaded!", tint = Color(0xFF2E7D32))
                                Text("Vignette.png attached automatically! Tap to Autofill", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                            }
                        }

                        TextField(value = descIn, onValueChange = { descIn = it }, label = { Text("Expense Title / Desc") }, modifier = Modifier.fillMaxWidth())
                        TextField(value = amountIn, onValueChange = { amountIn = it }, label = { Text("Disbursement Value ($)") }, modifier = Modifier.fillMaxWidth())
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Expense", "Income").forEach { t ->
                                val active = typeIn == t
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, if (active) CampfirePrimary else CampfireCardBorder, RoundedCornerShape(6.dp))
                                        .clickable { typeIn = t }
                                        .background(if (active) CampfirePrimary.copy(alpha = 0.08f) else Color.Transparent)
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(t, color = if (active) CampfirePrimary else CampfireMuted, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val valFloat = amountIn.toDoubleOrNull() ?: 0.0
                                if (descIn.isNotBlank() && valFloat > 0.0) {
                                    viewModel.addLedgerTransaction(
                                        description = descIn,
                                        amount = valFloat,
                                        type = typeIn,
                                        category = categoryIn,
                                        receiptPath = "Vignette.png"
                                    )
                                    descIn = ""
                                    amountIn = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Post To Compliance Ledger", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Ledger Tables List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(transactions) { tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CampfireDarkSurface)
                            .border(1.dp, CampfireCardBorder)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tx.description, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(tx.category, style = MaterialTheme.typography.labelSmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                                if (tx.receiptPath != null) {
                                    Icon(Icons.Default.AttachFile, "Attached File", tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                                    Text("Receipt Locked", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32))
                                }
                            }
                        }
                        Text(
                            text = "${if (tx.type == "Expense") "-" else "+"}$${"%.2f".format(tx.amount)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (tx.type == "Expense") Color(0xFFBA1A1A) else Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        } else {
            // CRM Partnerships Column
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("SPONSORSHIP PIPELINE CRM", style = MaterialTheme.typography.titleSmall, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    Text("Promote deals instantly along the compliance lifecycle toward signed Memorandums of Agreement (MOA).", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(crmDeals) { deal ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                        border = BorderStroke(1.dp, when (deal.status) {
                            "MOA Signed" -> Color(0xFF2E7D32).copy(alpha = 0.5f)
                            "Negotiating" -> CampfirePrimary.copy(alpha = 0.5f)
                            else -> CampfireCardBorder
                        })
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(deal.partnerName, color = CampfireOnBg, fontWeight = FontWeight.Black)
                                    Text("POC: ${deal.contactPerson}", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                                 }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when (deal.status) {
                                                "MOA Signed" -> Color(0xFF2E7D32).copy(alpha = 0.08f)
                                                "Negotiating" -> CampfirePrimary.copy(alpha = 0.08f)
                                                else -> CampfireMuted.copy(alpha = 0.08f)
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                 ) {
                                    Text(
                                        deal.status.uppercase(),
                                        color = when (deal.status) {
                                            "MOA Signed" -> Color(0xFF2E7D32)
                                            "Negotiating" -> CampfirePrimary
                                            else -> CampfireOnBg
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Est. Value: $${"%.0f".format(deal.value)}", color = CampfirePrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = { viewModel.advanceCRMDealStatus(deal) },
                                    colors = ButtonDefaults.textButtonColors(contentColor = CampfirePrimary)
                                 ) {
                                    Text("Promote Pipeline Status")
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Promote", modifier = Modifier.size(16.dp))
                                }
                            }
                            if (deal.remarks.isNotBlank()) {
                                Divider(color = CampfireCardBorder, modifier = Modifier.padding(vertical = 4.dp))
                                Text(deal.remarks, style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 4. THE PULSE (Member Engagement & Events)
// ----------------------------------------------------
@Composable
fun PulseSection(viewModel: CampfireViewModel) {
    val events by viewModel.events.collectAsState()
    var announcementTitle by remember { mutableStateOf("") }
    var announcementContent by remember { mutableStateOf("") }
    var pulseTab by remember { mutableStateOf("Events") } // Events, Broadcast

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Events", "Broadcast").forEach { sub ->
                val active = pulseTab == sub
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (active) CampfirePrimary else CampfireDarkSurface)
                        .clickable { pulseTab = sub }
                        .padding(vertical = 10.dp)
                        .border(1.dp, if (active) CampfirePrimary else CampfireCardBorder),
                    contentAlignment = Alignment.Center
                ) {
                    Text(sub, color = if (active) Color.White else CampfireOnBg, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (pulseTab == "Events") {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("EVENT BLUEPRINTS ACTIVATION", style = MaterialTheme.typography.titleSmall, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    Text("Select a pre-made template schema to auto-populate tasks, rosters, outline descriptions & ticket checking channels instantly.", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("EXECUTE BLUEPRINT WORKFLOW:", style = MaterialTheme.typography.labelSmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Webinar", "Seminar", "General Assembly").forEach { blue ->
                            Button(
                                onClick = { viewModel.selectEventBlueprint(blue) },
                                colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary.copy(alpha = 0.08f)),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(blue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CampfirePrimary)
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(events) { ev ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                        border = BorderStroke(1.dp, CampfireCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ev.title, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                                    Text("Blueprint: ${ev.blueprintType} | Location: ${ev.location}", style = MaterialTheme.typography.bodySmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CampfirePrimary.copy(alpha = 0.08f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("CODE: ${ev.attendanceCode}", color = CampfirePrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(ev.description, color = CampfireMuted, style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(ev.dateTime, style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                                Spacer(modifier = Modifier.weight(1f))
                                
                                // Simulating scan checkpoint
                                Button(
                                    onClick = { viewModel.checkInWithQRCode(ev.attendanceCode) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                ) {
                                    Icon(Icons.Default.QrCodeScanner, null, modifier = Modifier.size(16.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Simulate Door Scan", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Post general news for non-officer member side
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("BROADCAST PULSE NOTICE TO MEMBERS", style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    Text("Your published posts automatically display with active notifications on the member Dashboard feed layout.", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                    
                    TextField(
                        value = announcementTitle,
                        onValueChange = { announcementTitle = it },
                        label = { Text("Announcement Headline") },
                        modifier = Modifier.fillMaxWidth().testTag("post_broadcast_title")
                    )
                    TextField(
                        value = announcementContent,
                        onValueChange = { announcementContent = it },
                        label = { Text("Details & Event registrants info") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )

                    var importance by remember { mutableStateOf("General") } // General, Urgent
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Importance Alert tag:", style = MaterialTheme.typography.labelSmall, color = CampfireOnBg)
                        Spacer(modifier = Modifier.weight(1f))
                        listOf("General", "Urgent").forEach { tag ->
                            val isSel = importance == tag
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CampfirePrimary else CampfireCardBorder)
                                    .clickable { importance = tag }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(tag, color = if (isSel) Color.White else CampfireOnBg, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (announcementTitle.isNotBlank() && announcementContent.isNotBlank()) {
                                viewModel.postPublicAnnouncement(
                                    announcementTitle,
                                    announcementContent,
                                    if (importance == "Urgent") "Urgent" else "Newsletter"
                                )
                                announcementTitle = ""
                                announcementContent = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Broadcast Live to Feed", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 5. THE HANDOFF (Communications & Project Tracking)
// ----------------------------------------------------
@Composable
fun HandoffSection(viewModel: CampfireViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var inputTaskTitle by remember { mutableStateOf("") }
    var taskCommittee by remember { mutableStateOf("Logistics") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
            border = BorderStroke(1.dp, CampfireCardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "CROSS-COMMITTEE DEPENDENCY ENGINE",
                    style = MaterialTheme.typography.titleSmall,
                    color = CampfirePrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Break silos. Marking dependent tasks as completed (e.g., Logistics contract approval) triggers an automated notification unblocking other workflows instantly.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CampfireMuted
                )
            }
        }

        // Add a task
        Card(
            colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
            border = BorderStroke(1.dp, CampfireCardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Create Multi-Step Workflow Task", color = CampfireOnBg, fontWeight = FontWeight.Bold)
                TextField(
                    value = inputTaskTitle,
                    onValueChange = { inputTaskTitle = it },
                    label = { Text("Task Action Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Department Assigned: ", style = MaterialTheme.typography.labelSmall, color = CampfireOnBg)
                    listOf("Logistics", "Marketing", "Finance").forEach { com ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, if (taskCommittee == com) CampfirePrimary else CampfireCardBorder, RoundedCornerShape(8.dp))
                                .clickable { taskCommittee = com }
                                .background(if (taskCommittee == com) CampfirePrimary.copy(alpha = 0.08f) else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(com, color = if (taskCommittee == com) CampfirePrimary else CampfireMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Setup artificial dependency on existing tasks
                var selectedDepId by remember { mutableStateOf<Int?>(null) }
                var selectedDepTitle by remember { mutableStateOf<String?>(null) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Depends on task: ", style = MaterialTheme.typography.labelSmall, color = CampfireOnBg)
                    Spacer(modifier = Modifier.weight(1f))
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(selectedDepTitle ?: "No Dependency", color = CampfirePrimary, fontWeight = FontWeight.Bold)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text("No Dependency") },
                                onClick = {
                                    selectedDepId = null
                                    selectedDepTitle = null
                                    expanded = false
                                }
                            )
                            tasks.forEach { task ->
                                DropdownMenuItem(
                                    text = { Text("[${task.committee}] ${task.title}") },
                                    onClick = {
                                        selectedDepId = task.id
                                        selectedDepTitle = task.title
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (inputTaskTitle.isNotBlank()) {
                            viewModel.createOrgTask(inputTaskTitle, taskCommittee, selectedDepId, selectedDepTitle)
                            inputTaskTitle = ""
                            selectedDepId = null
                            selectedDepTitle = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Catalyze Step Workflow Task", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Project timeline list
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(tasks) { task ->
                val hasDep = task.dependencyTaskId != null
                val isUnlocked = if (!hasDep) true else {
                    tasks.find { it.id == task.dependencyTaskId }?.isCompleted == true
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                    border = BorderStroke(
                        1.dp,
                        if (task.isCompleted) Color(0xFF2E7D32).copy(alpha = 0.3f)
                        else if (!isUnlocked) Color(0xFFBA1A1A).copy(alpha = 0.3f)
                        else CampfireCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    task.title,
                                    color = if (isUnlocked) CampfireOnBg else CampfireMuted,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                )
                                Text("Committee: " + task.committee.uppercase(), style = MaterialTheme.typography.labelSmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                            }
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { if (isUnlocked) viewModel.toggleOrgTaskCompletion(task) },
                                enabled = isUnlocked,
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2E7D32), uncheckedColor = CampfirePrimary)
                            )
                        }

                        if (hasDep) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = if (isUnlocked) Color(0xFF2E7D32) else Color(0xFFBA1A1A),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Requires: ${task.dependencyTitle}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUnlocked) Color(0xFF2E7D32) else Color(0xFFBA1A1A),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ====================================================
// VIEW: STUDENT-FACING MEMBER HUB COMPANION APP
// ====================================================

@Composable
fun MemberHubLayout(viewModel: CampfireViewModel) {
    val isMemberOnboarded by viewModel.isMemberOnboarded.collectAsState()

    if (!isMemberOnboarded) {
        // 1. THE WELCOME GATES Interest Mapper Walkthrough
        MemberWelcomeGates(viewModel = viewModel)
    } else {
        // Authenticated Member Portal Home
        MemberMainDashboard(viewModel = viewModel)
    }
}

// ----------------------------------------------------
// 1. THE WELCOME GATES (The Onboarding & Interest Mapping)
// ----------------------------------------------------
@Composable
fun MemberWelcomeGates(viewModel: CampfireViewModel) {
    val interests = listOf("UI/UX Design", "Tech & Kotlin", "Marketing & Writing", "Partnerships", "Finance", "Event Hosting")
    val selectedInterests = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.campfire_logo),
            contentDescription = "Welcome Logo",
            modifier = Modifier.size(110.dp)
        )
        
        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Welcome to Campfire!",
            style = MaterialTheme.typography.headlineMedium,
            color = CampfireOnBg,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome Gates Custom Onboarding: Map your interest tracks to instantly shape your companion experience and personalize growth recommendations.",
            style = MaterialTheme.typography.bodyMedium,
            color = CampfireMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
            border = BorderStroke(1.dp, CampfirePrimary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SELECT 1 OR MORE INTERESTS TO AUTO-ATTACH PROFILE:",
                    style = MaterialTheme.typography.labelSmall,
                    color = CampfirePrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                interests.forEach { item ->
                    val isChecked = selectedInterests.contains(item)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) selectedInterests.remove(item) else selectedInterests.add(item)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                if (isChecked) selectedInterests.remove(item) else selectedInterests.add(item)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = CampfirePrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(item, color = CampfireOnBg)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedInterests.isNotEmpty()) {
                    viewModel.submitWelcomeOnboarding(selectedInterests.toList())
                } else {
                    // Seed at least one
                    viewModel.submitWelcomeOnboarding(listOf("UI/UX Design"))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_interests_button")
        ) {
            Text("Build My Profile & Start Discovering", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White)
        }
    }
}

// ----------------------------------------------------
// THE CORE MEMBER PORTAL MAIN DASHBOARD
// ----------------------------------------------------
@Composable
fun MemberMainDashboard(viewModel: CampfireViewModel) {
    var selectedView by remember { mutableStateOf("Discover") } // Discover, Passport, Buzz, Opps, Kudos
    
    val menuItems = listOf(
        "Discover" to Icons.Default.Festival,
        "Passport" to Icons.Default.QrCode,
        "The Buzz" to Icons.Default.Campaign,
        "Opportunities" to Icons.Default.WorkOutline,
        "Standings" to Icons.Default.WorkspacePremium
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Thumb-friendly mini sub menu
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems) { (label, icon) ->
                val active = selectedView == label
                FilterChip(
                    selected = active,
                    onClick = { selectedView = label },
                    label = { Text(label, fontWeight = FontWeight.Bold) },
                    leadingIcon = { Icon(icon, null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CampfireSecondary,
                        selectedLabelColor = Color.Black,
                        selectedLeadingIconColor = Color.Black,
                        containerColor = CampfireDarkSurface,
                        labelColor = CampfireOnBg
                    )
                )
            }
        }

        Divider(color = CampfireCardBorder)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when (selectedView) {
                "Discover" -> MemberDiscoverFeed(viewModel = viewModel)
                "Passport" -> MemberPassportView(viewModel = viewModel)
                "The Buzz" -> MemberBuzzFeed(viewModel = viewModel)
                "Opportunities" -> MemberOppsHub(viewModel = viewModel)
                "Standings" -> MemberStandingsView(viewModel = viewModel)
            }
        }
    }
}

// ----------------------------------------------------
// CARD: SEMINARS PORTAL (Event Discovery Feed)
// ----------------------------------------------------
@Composable
fun MemberDiscoverFeed(viewModel: CampfireViewModel) {
    val events by viewModel.events.collectAsState()
    val interests by viewModel.memberInterests.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "🔥 RECOMMENDED FOR YOU (INTEREST MATCH)",
                        style = MaterialTheme.typography.titleSmall,
                        color = CampfirePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Based on your Interest Mapping tags: ${interests.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireOnBg
                    )
                }
            }
        }

        items(events) { ev ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, if (ev.isRegistered) Color(0xFF2E7D32) else CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(ev.title, style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                            Text("Type: ${ev.blueprintType} | Location: ${ev.location}", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ev.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireOnBg
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ev.dateTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = CampfireMuted
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { viewModel.registerEventRSVP(ev) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (ev.isRegistered) Color(0xFF2E7D32) else CampfirePrimary
                            )
                        ) {
                            Icon(
                                if (ev.isRegistered) Icons.Default.Check else Icons.Default.DoubleArrow,
                                null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (ev.isRegistered) "RSVP'd" else "Unlock RSVP",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CARD: EVENT PASSPORT (Attendance check-in QR & Certificates)
// ----------------------------------------------------
@Composable
fun MemberPassportView(viewModel: CampfireViewModel) {
    val events by viewModel.events.collectAsState()
    val rsvpEvents = events.filter { it.isRegistered }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "CAMPFIRE EVENT PASSPORT",
                        style = MaterialTheme.typography.titleSmall,
                        color = CampfirePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Present your Passport checking interface at checkpoints. Once scanned by an officer, your dynamic completion certificates will unlock.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (rsvpEvents.isEmpty()) {
            item {
                Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.QrCode, "No RSVP", tint = CampfireMuted, modifier = Modifier.size(64.dp))
                        Text("No active registrations found. Register for a seminar on the Discover tab to populate your passport QR code.", color = CampfireMuted, textAlign = TextAlign.Center)
                    }
                }
            }
        } else {
            items(rsvpEvents) { ev ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                    border = BorderStroke(1.dp, if (ev.isCheckedIn) Color(0xFF2E7D32) else CampfireCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(ev.title, fontWeight = FontWeight.Bold, color = CampfireOnBg, textAlign = TextAlign.Center)
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (ev.isCheckedIn) Color(0xFF2E7D32).copy(alpha = 0.08f) else CampfirePrimary.copy(alpha = 0.08f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                             ) {
                                Text(
                                    if (ev.isCheckedIn) "ATTENDED" else "PENDING DOOR SCAN",
                                    color = if (ev.isCheckedIn) Color(0xFF2E7D32) else CampfirePrimary,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Passport QR Representation
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(2.dp, if (ev.isCheckedIn) Color(0xFF2E7D32) else CampfireCardBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // High fidelity retro simulated QR blocks drawing
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val sizeVal = size.width
                                  val margin = 4f
                                  val count = 6
                                  val step = sizeVal / count
                                  for (i in 0 until count) {
                                      for (j in 0 until count) {
                                          // Draw modular abstract code
                                          val fillVal = ((i * j + ev.id) % 3 == 0) || (i == 0 && j == 0) || (i == 0 && j == count-1) || (i == count-1 && j == 0)
                                          if (fillVal) {
                                              drawRect(
                                                  color = Color.Black,
                                                  topLeft = Offset(i * step + margin, j * step + margin),
                                                  size = androidx.compose.ui.geometry.Size(step - margin * 2, step - margin * 2)
                                              )
                                          }
                                      }
                                  }
                            }
                        }

                        // Unlocked Certificate
                        if (ev.isCheckedIn) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2E7D32).copy(alpha = 0.06f))
                                    .border(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.CardMembership, "Certificate", tint = Color(0xFF2E7D32))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Digital Certificate Active!", style = MaterialTheme.typography.bodySmall, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                                        Text("Official cryptographic participation proof loaded.", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(Icons.Default.DownloadForOffline, "Download cert", tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        } else {
                            Text(
                                "CODE PASS: ${ev.attendanceCode}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CampfirePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CARD: THE BUZZ (Personalized News & Updates)
// ----------------------------------------------------
@Composable
fun MemberBuzzFeed(viewModel: CampfireViewModel) {
    val announcements by viewModel.announcements.collectAsState()
    val alertsEnabled by viewModel.pushNotificationsEnabled.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("THE BUZZ", style = MaterialTheme.typography.titleSmall, color = CampfirePrimary, fontWeight = FontWeight.Bold)
                            Text("Unified feed keeping you appraised of critical milestones. Zero notifications spam.", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                        }
                        
                        // Switch matching "Disable Alert Alerts Push"
                        Switch(
                            checked = alertsEnabled,
                            onCheckedChange = { viewModel.pushNotificationsEnabled.value = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = CampfireDarkSurface, checkedTrackColor = CampfirePrimary)
                        )
                    }
                }
            }
        }

        items(announcements) { ann ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, if (ann.isBookmarked) CampfirePrimary else CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (ann.tag == "#Urgent") Color(0xFFBA1A1A).copy(alpha = 0.08f)
                                    else if (ann.tag == "#GeneralAssembly") CampfirePrimary.copy(alpha = 0.08f)
                                    else CampfireMuted.copy(alpha = 0.08f)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = ann.tag,
                                color = if (ann.tag == "#Urgent") Color(0xFFBA1A1A) else if (ann.tag == "#GeneralAssembly") CampfirePrimary else CampfireMuted,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { viewModel.toggleNotificationBookmark(ann) }) {
                            Icon(
                                if (ann.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (ann.isBookmarked) CampfirePrimary else CampfireMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(ann.title, style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(ann.content, style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                }
            }
        }
    }
}

// ----------------------------------------------------
// CARD: THE OPPORTUNITY HUB (Scholarships, Projects, Internships)
// ----------------------------------------------------
@Composable
fun MemberOppsHub(viewModel: CampfireViewModel) {
    val opportunities by viewModel.opportunities.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "THE CAMPFIRE OPPORTUNITY HUB",
                        style = MaterialTheme.typography.titleSmall,
                        color = CampfirePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Exclusive scholarship portfolios, partner internships, and internal committee officer fast tracks attached with auto-profile registration.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CampfireMuted
                    )
                }
            }
        }

        items(opportunities) { opp ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(
                    1.dp,
                    if (opp.hasApplied) Color(0xFF2E7D32).copy(alpha = 0.5f) else CampfireCardBorder
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(opp.title, style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                            Text("Category: ${opp.category} | Deadline: ${opp.deadline}", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                        }
                        if (opp.hasApplied) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2E7D32).copy(alpha = 0.08f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("APPLIED", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = CampfireCardBorder)

                    Text("Perks: ${opp.perks}", style = MaterialTheme.typography.bodySmall, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    Text("Eligibility: ${opp.eligibility}", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { if (!opp.hasApplied) viewModel.submitOpportunityApplication(opp) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (opp.hasApplied) Color(0xFF2E7D32).copy(alpha = 0.08f) else CampfirePrimary,
                            contentColor = if (opp.hasApplied) Color(0xFF2E7D32) else Color.White
                        ),
                        enabled = !opp.hasApplied,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            if (opp.hasApplied) Icons.Default.Check else Icons.Default.Send,
                            null,
                            modifier = Modifier.size(16.dp),
                            tint = if (opp.hasApplied) Color(0xFF2E7D32) else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (opp.hasApplied) "Job Proposal Submitted" else "Instant Mobile Apply",
                            fontWeight = FontWeight.Bold,
                            color = if (opp.hasApplied) Color(0xFF2E7D32) else Color.White
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CARD: STANDINGS & SHOUTOUTS (scoreboard & Kudos)
// ----------------------------------------------------
@Composable
fun MemberStandingsView(viewModel: CampfireViewModel) {
    val kudos by viewModel.kudos.collectAsState()

    var inputReceiver by remember { mutableStateOf("") }
    var inputKudosText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "ENGAGEMENT SCOREBOARD",
                        style = MaterialTheme.typography.titleSmall,
                        color = CampfirePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SEMINARS", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                            Text("3 ATTENDED", color = CampfireOnBg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CONTRIBS", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                            Text("18 HOURS", color = CampfireOnBg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("STREAK", style = MaterialTheme.typography.labelSmall, color = CampfireMuted)
                            Text("12 DAYS", color = CampfireOnBg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfireCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("PEER-TO-PEER PUBLIC SHOUTOUT", style = MaterialTheme.typography.titleMedium, color = CampfireOnBg, fontWeight = FontWeight.Bold)
                    Text("Publicly recognize fellow officers or committee mates on the community standings board.", style = MaterialTheme.typography.bodySmall, color = CampfireMuted)
                    
                    TextField(
                        value = inputReceiver,
                        onValueChange = { inputReceiver = it },
                        label = { Text("Recipient / Committee Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = inputKudosText,
                        onValueChange = { inputKudosText = it },
                        label = { Text("Write your public appreciation kudos...") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    Button(
                        onClick = {
                            if (inputReceiver.isNotBlank() && inputKudosText.isNotBlank()) {
                                viewModel.sendPublicKudos(
                                    from = "Member Alex",
                                    to = inputReceiver,
                                    message = inputKudosText
                                )
                                inputReceiver = ""
                                inputKudosText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CampfirePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolunteerActivism, null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Post Praise on Wall", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        items(kudos) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CampfireDarkSurface),
                border = BorderStroke(1.dp, CampfirePrimary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, "Kudos icon", tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${item.fromUser} sent Kudos to ${item.toUser}",
                            fontWeight = FontWeight.Bold,
                            color = CampfireOnBg,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        item.message,
                        color = CampfireMuted,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

// Helper elements updated
