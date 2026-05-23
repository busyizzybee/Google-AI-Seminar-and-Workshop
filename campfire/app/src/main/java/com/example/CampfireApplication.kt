package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.CampfireDatabase
import com.example.data.CampfireRepository

class CampfireApplication : Application() {
    lateinit var database: CampfireDatabase
    lateinit var repository: CampfireRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            CampfireDatabase::class.java,
            "campfire_db"
        )
        .fallbackToDestructiveMigration()
        .build()
        
        repository = CampfireRepository(database.dao())
    }
}
