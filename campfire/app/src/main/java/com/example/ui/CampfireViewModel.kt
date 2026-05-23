package com.example.ui

import androidx.lifecycle.*
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CampfireViewModel(private val repository: CampfireRepository) : ViewModel() {

    // 1. OBSERVABLE DB DATA FLOWS
    val applicants: StateFlow<List<Applicant>> = repository.allApplicants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<VaultDocument>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<LedgerTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val crmDeals: StateFlow<List<CRMDeal>> = repository.allCRMDeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<ClubEvent>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<Announcement>> = repository.allAnnouncements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<OrgTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val opportunities: StateFlow<List<Opportunity>> = repository.allOpportunities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val kudos: StateFlow<List<Kudos>> = repository.allKudos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. SESSION & RUNTIME UI STATES
    val selectedVaultRole = MutableStateFlow("VP of Finance")
    val activeSection = MutableStateFlow("Admin Board") // "Admin Board" or "Member Hub"
    val isMemberOnboarded = MutableStateFlow(false)
    val memberInterests = MutableStateFlow<List<String>>(emptyList())
    val notifications = MutableStateFlow<List<String>>(
        listOf("Welcome to Campfire Operating System! Admin & Member views are active.")
    )
    val pushNotificationsEnabled = MutableStateFlow(true)
    val selectedAnnouncementForDetail = MutableStateFlow<Announcement?>(null)
    
    // Member metrics deduced
    val memberName = "Alex Rivera" // Uses the preloaded student profile Alex

    // Unread count (Announcements that are not bookmarked)
    val unreadCount = announcements.map { list ->
        list.size // Simplified counter
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

    // 3. SERVICE INTERACTORS

    // Induction Page Actions
    fun addNewApplicant(name: String, email: String, interests: String) {
        viewModelScope.launch {
            repository.insertApplicant(
                Applicant(
                    name = name,
                    email = email,
                    interests = interests,
                    status = "Pending"
                )
            )
            addNotification("Recruit Applied: $name registered under SCREENING pipeline.")
        }
    }

    fun toggleApplicantStatus(applicant: Applicant, newStatus: String, assignedCommittee: String = "None") {
        viewModelScope.launch {
            val updated = applicant.copy(
                status = newStatus,
                assignedCommittee = assignedCommittee,
                onboardingState = if (newStatus == "Accepted") {
                    "Setup Profile [Done], Read SOPs [Pending], Complete Induction [Pending]"
                } else applicant.onboardingState
            )
            repository.updateApplicant(updated)
            
            if (newStatus == "Accepted") {
                addNotification("🎉 Profile Auto-Created: ${applicant.name} is now in the $assignedCommittee committee dashboard.")
            }
        }
    }

    fun toggleOnboardingTask(applicant: Applicant, taskIndex: Int) {
        viewModelScope.launch {
            val taskList = applicant.onboardingState.split(", ").mapIndexed { index, taskText ->
                if (index == taskIndex) {
                    val label = taskText.substringBefore(" [")
                    val isDone = taskText.contains("[Pending]")
                    "$label [${if (isDone) "Done" else "Pending"}]"
                } else {
                    taskText
                }
            }
            val updatedState = taskList.joinToString(", ")
            repository.updateApplicant(applicant.copy(onboardingState = updatedState))
        }
    }

    // Vault Documentation
    fun createVaultDocument(title: String, role: String, category: String, content: String) {
        viewModelScope.launch {
            repository.insertDocument(
                VaultDocument(
                    title = title,
                    role = role,
                    category = category,
                    content = content
                )
            )
            addNotification("📄 New Vault Entry: '$title' cataloged securely under role code: $role.")
        }
    }

    // Ledger Transactions
    fun addLedgerTransaction(description: String, amount: Double, type: String, category: String, receiptPath: String? = null) {
        viewModelScope.launch {
            repository.insertTransaction(
                LedgerTransaction(
                    description = description,
                    amount = amount,
                    type = type,
                    category = category,
                    receiptPath = receiptPath
                )
            )
            addNotification("💳 Ledger Updated: '${description}' ($category) posted to compliance stream.")
        }
    }

    // CRM Deal Toggles
    fun advanceCRMDealStatus(deal: CRMDeal) {
        viewModelScope.launch {
            val nextStatus = when (deal.status) {
                "Pitch Deck" -> "Negotiating"
                "Negotiating" -> "MOA Signed"
                else -> "Pitch Deck"
            }
            repository.updateCRMDeal(deal.copy(status = nextStatus))
            addNotification("🤝 CRM Pipeline: Direct update of ${deal.partnerName} status to [$nextStatus].")
        }
    }

    fun createCRMDeal(partner: String, contact: String, value: Double, remarks: String) {
        viewModelScope.launch {
            repository.insertCRMDeal(
                CRMDeal(
                    partnerName = partner,
                    contactPerson = contact,
                    status = "Pitch Deck",
                    value = value,
                    remarks = remarks
                )
            )
        }
    }

    // Event Blueprints Creation
    fun selectEventBlueprint(blueprint: String) {
        viewModelScope.launch {
            val (title, loc, desc) = when (blueprint) {
                "Webinar" -> Triple("Interactive Panel: Future of Tech & Leadership", "Zoom Conference", "An online roundtable designed to outline student leadership templates.")
                "Seminar" -> Triple("On-Campus Crash Course: Jetpack Compose Masterclass", "Seminar Studio 3A", "Hands-on coding sprint creating polished, accessible interfaces live.")
                "General Assembly" -> Triple("Campfire GA: S'mores, Cocoa & Term Turnovers", "Lakeside Amphitheater", "Gather around to report EOY accomplishments and handoff roles.")
                else -> Triple("Custom Blueprint Event", "Student Center", "New custom planned blueprint event.")
            }
            repository.insertEvent(
                ClubEvent(
                    title = title,
                    blueprintType = blueprint,
                    dateTime = "June 14, 2026, 4:00 PM",
                    location = loc,
                    description = desc
                )
            )
            addNotification("📅 Blueprint Activated: '${title}' tasks auto-populated ($blueprint workflow).")
        }
    }

    // Handoff & Project Tracking dependency engine
    fun toggleOrgTaskCompletion(task: OrgTask) {
        viewModelScope.launch {
            val nextState = !task.isCompleted
            repository.updateTask(task.copy(isCompleted = nextState))
            
            if (nextState) {
                // Cross-Committee Event Triggering
                addNotification("✅ Task Completed: '${task.title}' under ${task.committee} committee.")
                
                // If it triggers dependency on another card
                tasks.value.forEach { dependentTask ->
                    if (dependentTask.dependencyTaskId == task.id && !dependentTask.isCompleted) {
                        addNotification("🔔 Dependency Triggered: '${task.title}' completed instantly unlocked and notified ${dependentTask.committee} for '${dependentTask.title}'!")
                    }
                }
            }
        }
    }

    fun createOrgTask(title: String, committee: String, dependencyId: Int? = null, depTitle: String? = null) {
        viewModelScope.launch {
            repository.insertTask(
                OrgTask(
                    title = title,
                    committee = committee,
                    dependencyTaskId = dependencyId,
                    dependencyTitle = depTitle,
                    isCompleted = false
                )
            )
        }
    }

    // Member Hub Actions
    fun submitWelcomeOnboarding(selected: List<String>) {
        isMemberOnboarded.value = true
        memberInterests.value = selected
        addNotification("✨ Welcome Gate Complete! Interests mapped: ${selected.joinToString()}. Personal carousel generated.")
    }

    fun registerEventRSVP(event: ClubEvent) {
        viewModelScope.launch {
            repository.updateEvent(event.copy(isRegistered = !event.isRegistered))
            val actMsg = if (!event.isRegistered) "Registered!" else "Cancelled Registration."
            addNotification("🎫 Passport RSVP: Successfully updated '${event.title}' code.")
        }
    }

    fun checkInWithQRCode(eventCode: String) {
        viewModelScope.launch {
            val matchedEvent = events.value.find { it.attendanceCode.equals(eventCode, ignoreCase = true) }
            if (matchedEvent != null) {
                repository.updateEvent(matchedEvent.copy(isCheckedIn = true, isRegistered = true))
                addNotification("🎓 Instant Check-in Successful! Scanned code: '$eventCode'. Digital Participation Certificate unlocked.")
            } else {
                addNotification("⚠️ Invalid Ticket Code Scan: Code '$eventCode' was not recognized on the venue deck.")
            }
        }
    }

    fun toggleNotificationBookmark(announcement: Announcement) {
        viewModelScope.launch {
            repository.updateAnnouncement(announcement.copy(isBookmarked = !announcement.isBookmarked))
        }
    }

    fun submitOpportunityApplication(opportunity: Opportunity) {
        viewModelScope.launch {
            repository.updateOpportunity(opportunity.copy(hasApplied = true))
            addNotification("🚀 Instant Apply: Submitted profile data to '${opportunity.title}'. Interview sync initiated!")
        }
    }

    fun sendPublicKudos(from: String, to: String, message: String) {
        viewModelScope.launch {
            repository.insertKudos(Kudos(fromUser = from, toUser = to, message = message))
            addNotification("❤️ Kudos Posted: Public appreciation shared on the peer scoreboard.")
        }
    }

    fun postPublicAnnouncement(title: String, content: String, level: String) {
        viewModelScope.launch {
            repository.insertAnnouncement(
                Announcement(
                    title = title,
                    content = content,
                    tag = if (level == "Urgent") "#Urgent" else if (level == "Assembly") "#GeneralAssembly" else "#Newsletter"
                )
            )
            addNotification("📢 Feed broadcast: Posted '$title' to student-member feed screens.")
        }
    }

    // Helper notifications list
    private fun addNotification(msg: String) {
        val updated = notifications.value.toMutableList()
        updated.add(0, msg)
        notifications.value = updated.take(6) // Keep latest 6 alert rows
    }
}

class CampfireViewModelFactory(private val repository: CampfireRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampfireViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CampfireViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
