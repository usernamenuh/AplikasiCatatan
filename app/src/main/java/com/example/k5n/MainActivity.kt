package com.example.k5n

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.k5n.database.NoteDatabase
import com.example.k5n.repository.NoteRepository
import com.example.k5n.viewmodel.NoteViewModel
import com.example.k5n.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // ⏱️ Install splash screen dan delay selama 1.5 detik
        val splashScreen = installSplashScreen()
        val startTime = System.currentTimeMillis()
        splashScreen.setKeepOnScreenCondition {
            System.currentTimeMillis() - startTime < 1500
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inisialisasi ViewModel
        setupViewModel()

        // Menyesuaikan status bar dan nav bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainerView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewModel() {
        val database = NoteDatabase.getDatabase(this)
        val repository = NoteRepository(database)
        val factory = NoteViewModelFactory(application, repository)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }
}
