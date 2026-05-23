package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.CampfireAppUI
import com.example.ui.CampfireViewModel
import com.example.ui.CampfireViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve database repository safely from application subclass
        val application = applicationContext as CampfireApplication
        val repository = application.repository

        // Initialize master ViewModel
        val viewModel: CampfireViewModel by viewModels {
            CampfireViewModelFactory(repository)
        }

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    CampfireAppUI(viewModel = viewModel)
                }
            }
        }
    }
}
