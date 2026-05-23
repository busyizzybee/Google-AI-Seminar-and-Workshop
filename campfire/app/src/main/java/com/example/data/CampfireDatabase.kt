package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ----------------------------------------------------
// 1. ENTITIES
// ----------------------------------------------------

@Entity(tableName = "applicants")
data class Applicant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val interests: String,
    val status: String, // "Pending", "Accepted", "Rejected"
    val assignedCommittee: String = "None",
    val onboardingState: String = "Create Profile [Pending], Set Role [Pending], Join Slack [Pending], Read Handbook [Pending]"
)

@Entity(tableName = "vault_documents")
data class VaultDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val role: String, // e.g. "VP Finance", "President", "Logistics Lead"
    val category: String, // "Budget Ledger", "Template", "Terminal Report"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "ledger_transactions")
data class LedgerTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val type: String, // "Income", "Expense"
    val category: String,
    val receiptPath: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "crm_deals")
data class CRMDeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val partnerName: String,
    val contactPerson: String,
    val status: String, // "Pitch Deck", "Negotiating", "MOA Signed"
    val value: Double,
    val remarks: String
)

@Entity(tableName = "club_events")
data class ClubEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val blueprintType: String, // "Webinar", "General Assembly", "Seminar"
    val dateTime: String,
    val location: String,
    val description: String,
    val isRegistered: Boolean = false,
    val isCheckedIn: Boolean = false,
    val attendanceCode: String = "CAMP-${(1000..9999).random()}"
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val tag: String, // "#Urgent", "#GeneralAssembly", "#Newsletter"
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false
)

@Entity(tableName = "org_tasks")
data class OrgTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val committee: String, // e.g., "Logistics", "Marketing", "Finance"
    val dependencyTaskId: Int? = null,
    val dependencyTitle: String? = null,
    val isCompleted: Boolean = false
)

@Entity(tableName = "opportunities")
data class Opportunity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Internship", "Scholarship", "Position"
    val deadline: String,
    val perks: String,
    val eligibility: String,
    val hasApplied: Boolean = false
)

@Entity(tableName = "kudos")
data class Kudos(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fromUser: String,
    val toUser: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

// ----------------------------------------------------
// 2. DAOs
// ----------------------------------------------------

@Dao
interface CampfireDao {
    // Applicants
    @Query("SELECT * FROM applicants ORDER BY id DESC")
    fun getAllApplicants(): Flow<List<Applicant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplicant(applicant: Applicant)

    @Update
    suspend fun updateApplicant(applicant: Applicant)

    // Vault
    @Query("SELECT * FROM vault_documents ORDER BY timestamp DESC")
    fun getAllDocuments(): Flow<List<VaultDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: VaultDocument)

    // Ledger
    @Query("SELECT * FROM ledger_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<LedgerTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: LedgerTransaction)

    // CRM
    @Query("SELECT * FROM crm_deals ORDER BY id DESC")
    fun getAllCRMDeals(): Flow<List<CRMDeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCRMDeal(deal: CRMDeal)

    @Update
    suspend fun updateCRMDeal(deal: CRMDeal)

    // Events
    @Query("SELECT * FROM club_events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<ClubEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: ClubEvent)

    @Update
    suspend fun updateEvent(event: ClubEvent)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Update
    suspend fun updateAnnouncement(announcement: Announcement)

    // Tasks
    @Query("SELECT * FROM org_tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<OrgTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: OrgTask)

    @Update
    suspend fun updateTask(task: OrgTask)

    // Opportunities
    @Query("SELECT * FROM opportunities ORDER BY id DESC")
    fun getAllOpportunities(): Flow<List<Opportunity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: Opportunity)

    @Update
    suspend fun updateOpportunity(opportunity: Opportunity)

    // Kudos
    @Query("SELECT * FROM kudos ORDER BY timestamp DESC")
    fun getAllKudos(): Flow<List<Kudos>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKudos(kudos: Kudos)
}

// ----------------------------------------------------
// 3. DATABASE
// ----------------------------------------------------

@Database(
    entities = [
        Applicant::class,
        VaultDocument::class,
        LedgerTransaction::class,
        CRMDeal::class,
        ClubEvent::class,
        Announcement::class,
        OrgTask::class,
        Opportunity::class,
        Kudos::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CampfireDatabase : RoomDatabase() {
    abstract fun dao(): CampfireDao
}
