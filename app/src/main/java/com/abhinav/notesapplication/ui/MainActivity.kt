package com.abhinav.notesapplication.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.database.NotesDB
import com.abhinav.notesapplication.databinding.ActivityMainBinding
import com.abhinav.notesapplication.util.PREF_IS_LOGGED_IN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel
import com.abhinav.notesapplication.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var sharedPreferences: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash Screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val notesDao = NotesDB.getInstance(this).notesDao()
        val mainViewModelFactory = MainViewModelFactory(notesDao)
        viewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        sharedPreferences = PreferenceManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Condition to skip login screen
        val isUserLoggedIn = sharedPreferences.getBoolean(PREF_IS_LOGGED_IN, false)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val startDestination = if (isUserLoggedIn) R.id.homeFragment else R.id.loginFragment
        val navGraph = navController.navInflater.inflate(R.navigation.navigation_graph)

        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph

        //Back Press Handler
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.popBackStack().not())
                    finish()
            }
        }

        onBackPressedDispatcher.addCallback(this,callback)
    }
}