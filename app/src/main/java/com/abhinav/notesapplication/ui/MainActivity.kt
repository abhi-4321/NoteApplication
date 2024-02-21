package com.abhinav.notesapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.database.NotesDB
import com.abhinav.notesapplication.databinding.ActivityMainBinding
import com.abhinav.notesapplication.util.PREF_LOGGED_IN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel
import com.abhinav.notesapplication.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash Screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val notesDao = NotesDB.getInstance(this).notesDao()
        val mainViewModelFactory = MainViewModelFactory(notesDao)
        viewModel = ViewModelProvider(this,mainViewModelFactory)[MainViewModel::class.java]
        splashScreen.setKeepOnScreenCondition{ viewModel.isLoading.value }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = PreferenceManager(this)
        val isUserLoggedIn = sharedPreferences.getBoolean(PREF_LOGGED_IN,false)

        Toast.makeText(this,isUserLoggedIn.toString(), Toast.LENGTH_SHORT).show()
        if (!isUserLoggedIn)
            findNavController(R.id.nav_host_fragment_activity_main).graph.setStartDestination(R.id.loginFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}