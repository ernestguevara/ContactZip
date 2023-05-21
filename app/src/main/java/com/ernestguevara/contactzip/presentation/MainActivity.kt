package com.ernestguevara.contactzip.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ernestguevara.contactzip.R
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.databinding.ActivityMainBinding
import com.ernestguevara.contactzip.presentation.components.disableTooltip
import com.ernestguevara.contactzip.presentation.userscreen.UserListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityListener {

    private val userListViewModel: UserListViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    var isDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.disableTooltip()
    }

    override fun setToolbarTitle(title: String) {
        binding.toolbarTitle.text = title
    }
}