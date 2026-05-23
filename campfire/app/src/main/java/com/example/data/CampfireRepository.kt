package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CampfireRepository(private val dao: CampfireDao) {

    val allApplicants: Flow<List<Applicant>> = dao.getAllApplicants()
    val allDocuments: Flow<List<VaultDocument>> = dao.getAllDocuments()
    val allTransactions: Flow<List<LedgerTransaction>> = dao.getAllTransactions()
    val allCRMDeals: Flow<List<CRMDeal>> = dao.getAllCRMDeals()
    val allEvents: Flow<List<ClubEvent>> = dao.getAllEvents()
    val allAnnouncements: Flow<List<Announcement>> = dao.getAllAnnouncements()
    val allTasks: Flow<List<OrgTask>> = dao.getAllTasks()
    val allOpportunities: Flow<List<Opportunity>> = dao.getAllOpportunities()
    val allKudos: Flow<List<Kudos>> = dao.getAllKudos()

    init {
        // Pre-seed tables asynchronously if they're empty
        CoroutineScope(Dispatchers.IO).launch {
            if (dao.getAllApplicants().first().isEmpty()) {
                seedDatabase()
            }
        }
    }

    private suspend fun seedDatabase() {
        // Seed Applicants
        dao.insertApplicant(Applicant(name = "Alex Rivera", email = "alex@university.edu", interests = "UI/UX Development, Frontend", status = "Pending"))
        dao.insertApplicant(Applicant(name = "Jordan Lee", email = "jordan@university.edu", interests = "Content Writing, Marketing", status = "Accepted", assignedCommittee = "Marketing", onboardingState = "Create Profile [Done], Set Role [Done], Join Slack [Pending], Read Handbook [Pending]"))
        dao.insertApplicant(Applicant(name = "Maya Patel", email = "maya@university.edu", interests = "Sponsorships, Public Relations", status = "Pending"))
        dao.insertApplicant(Applicant(name = "Chris Evans", email = "chris@university.edu", interests = "Database Setup, DevOps", status = "Rejected"))

        // Seed Vault Documents
        dao.insertDocument(VaultDocument(title = "Annual budget ledger 2026 guidelines", role = "VP of Finance", category = "Budget Ledger", content = "Guidelines for officer disbursements: Minimum 15% reserves maintained for contingency. All liquidations must match official invoices. No advance payment without presidential approval."))
        dao.insertDocument(VaultDocument(title = "General Webinar Blueprint Template", role = "VP of Education", category = "Template", content = "1. Prep Phase: Create speaker contract, finalize event banner.\n2. Tech Phase: Launch Zoom link, setup slider polls.\n3. Engagement Phase: Share registration announcement via Pulse.\n4. Post-Event: Distribute certificates of attendance via Passport check-ins."))
        dao.insertDocument(VaultDocument(title = "EOY Terminal Report - FY2025", role = "President", category = "Terminal Report", content = "Summary of FY2025 highlights:\n- Total Active Members: 124\n- Seminars Executed: 8\n- Key Achievements: Partnered with 3 local tech start-ups for student internships; achieved 92% satisfaction on peer feedback surveys."))

        // Seed Ledger Transactions
        dao.insertTransaction(LedgerTransaction(description = "Sponsorship from Tech Corp Ltd", amount = 1500.0, type = "Income", category = "Corporate Sponsor"))
        dao.insertTransaction(LedgerTransaction(description = "Catering for GA (General Assembly)", amount = 450.0, type = "Expense", category = "Member Assembly"))
        dao.insertTransaction(LedgerTransaction(description = "Room Reservation & Audio Setup fee", amount = 120.0, type = "Expense", category = "Venue"))
        dao.insertTransaction(LedgerTransaction(description = "T-shirts for Core Organizers Team", amount = 300.0, type = "Expense", category = "Merchandise"))

        // Seed CRM Deals
        dao.insertCRMDeal(CRMDeal(partnerName = "Google Developer Relations", contactPerson = "Sarah Jenkins", status = "Negotiating", value = 2500.0, remarks = "Sent initial proposal and pitch deck. In review."))
        dao.insertCRMDeal(CRMDeal(partnerName = "Local Brew Coffee Co.", contactPerson = "Marcus Santos", status = "MOA Signed", value = 600.0, remarks = "Signed partnership contract! Providing free beverage coupons for attendants."))
        dao.insertCRMDeal(CRMDeal(partnerName = "DevTech Bootcamps Group", contactPerson = "Kenji Sato", status = "Pitch Deck", value = 1200.0, remarks = "Followed up with brochure attachment."))

        // Seed Club Events
        dao.insertEvent(ClubEvent(title = "AI Workshop: Prompt Crafting & Jetpack Compose", blueprintType = "Seminar", dateTime = "May 28, 2026, 2:00 PM", location = "Grand Auditorium B", description = "Unlock next-generation software development leveraging LLMs and native styling."))
        dao.insertEvent(ClubEvent(title = "Design Studio: Motion & Delight in Mobile UX", blueprintType = "Webinar", dateTime = "June 05, 2026, 6:00 PM", location = "Zoom Platform", description = "Understand the principles of physical weight, spring animation, and tactile feedback in mobile design."))

        // Seed Announcements
        dao.insertAnnouncement(Announcement(title = "URGENT: Upload Liquidation Receipts before Audit Day", content = "All members with active budget advances must submit scanned receipts. Financial standing scores rely on complete liquidations.", tag = "#Urgent"))
        dao.insertAnnouncement(Announcement(title = "General Assembly (GA) Next Friday!", content = "Gather around the campfire virtual space! Get updates from all committees, play team building games, and enjoy free coffee vouchers.", tag = "#GeneralAssembly"))
        dao.insertAnnouncement(Announcement(title = "Campfire Beta Launch Complete", content = "We have officially migrated our operations to the Campfire platform. Officers and members can now sync in real-time.", tag = "#Newsletter"))

        // Seed Org Tasks
        val task1 = OrgTask(title = "Finalize AI Workshop Speaker Contract", committee = "Logistics", isCompleted = true)
        dao.insertTask(task1)
        dao.insertTask(OrgTask(title = "Design AI Workshop Marketing Banners", committee = "Marketing", dependencyTaskId = 1, dependencyTitle = "Finalize AI Workshop Speaker Contract", isCompleted = false))
        dao.insertTask(OrgTask(title = "Distribute AI Workshop Registration Links", committee = "Marketing", dependencyTaskId = 2, dependencyTitle = "Design AI Workshop Marketing Banners", isCompleted = false))

        // Seed Opportunities
        dao.insertOpportunity(Opportunity(title = "Google Summer of Code Mentee", category = "Internship", deadline = "June 15, 2026", perks = "$3,000 Stipend + Direct Expert Mentorship", eligibility = "Active CS/IT student, basic knowledge of Kotlin and Git."))
        dao.insertOpportunity(Opportunity(title = "Campfire Tech Merit Scholarship 2026", category = "Scholarship", deadline = "June 01, 2026", perks = "100% Tuition Waiver + $1,000 Equipment Reimbursement", eligibility = "Minimum 1 Semester Active Membership, GPA >= 3.6"))
        dao.insertOpportunity(Opportunity(title = "Executive Assistant Officer Position", category = "Position", deadline = "May 31, 2026", perks = "Official chapter certificate + direct pipeline to presidency", eligibility = "Enthusiastic student leader, strong writing and scheduling skills."))

        // Seed Kudos
        dao.insertKudos(Kudos(fromUser = "VP Finance", toUser = "Marketing Committee", message = "Special kudos on closing the Coffee Spot deal! The budget ledger is looking very strong for the GA!"))
        dao.insertKudos(Kudos(fromUser = "President", toUser = "Alex Rivera", message = "Superb job with helping test the database persistence layers! Highly valued effort!"))
    }

    // Insert functions
    suspend fun insertApplicant(applicant: Applicant) = dao.insertApplicant(applicant)
    suspend fun updateApplicant(applicant: Applicant) = dao.updateApplicant(applicant)

    suspend fun insertDocument(document: VaultDocument) = dao.insertDocument(document)

    suspend fun insertTransaction(transaction: LedgerTransaction) = dao.insertTransaction(transaction)

    suspend fun insertCRMDeal(deal: CRMDeal) = dao.insertCRMDeal(deal)
    suspend fun updateCRMDeal(deal: CRMDeal) = dao.updateCRMDeal(deal)

    suspend fun insertEvent(event: ClubEvent) = dao.insertEvent(event)
    suspend fun updateEvent(event: ClubEvent) = dao.updateEvent(event)

    suspend fun insertAnnouncement(announcement: Announcement) = dao.insertAnnouncement(announcement)
    suspend fun updateAnnouncement(announcement: Announcement) = dao.updateAnnouncement(announcement)

    suspend fun insertTask(task: OrgTask) = dao.insertTask(task)
    suspend fun updateTask(task: OrgTask) = dao.updateTask(task)

    suspend fun insertOpportunity(opportunity: Opportunity) = dao.insertOpportunity(opportunity)
    suspend fun updateOpportunity(opportunity: Opportunity) = dao.updateOpportunity(opportunity)

    suspend fun insertKudos(kudos: Kudos) = dao.insertKudos(kudos)
}
