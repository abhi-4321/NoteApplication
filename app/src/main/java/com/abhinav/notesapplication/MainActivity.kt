package com.abhinav.notesapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.abhinav.notesapplication.api.NoteApi
import com.abhinav.notesapplication.databinding.ActivityMainBinding
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.repository.auth.AuthRepository
import com.abhinav.notesapplication.repository.auth.AuthRepositoryImpl
import com.abhinav.notesapplication.util.Constants.JWT_TOKEN
import com.abhinav.notesapplication.viewmodel.MainViewModel
import com.abhinav.notesapplication.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var authRepository: AuthRepository
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModelFactory(authRepository,sharedPreferences))[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Back Press Handler
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController(R.id.nav_host_fragment_activity_main).navigateUp().not())
                    finish()
            }
        }

        onBackPressedDispatcher.addCallback(this,callback)
    }
}