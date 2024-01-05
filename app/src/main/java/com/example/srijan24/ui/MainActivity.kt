package com.example.srijan24.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.srijan24.R
import com.example.srijan24.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var toolbar: Toolbar

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        toolbar = findViewById(R.id.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)

//        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.eventsFragment,
            R.id.merchandiseFragment,
            R.id.sponsorsFragment,
            R.id.coreTeamFragment,
            R.id.contactFragment
        ), binding.drawerLayout)

//        setupActionBarWithNavController(navController, binding.drawerLayout)
//        binding.navView.setupWithNavController(navController)


        binding.appBar.btnMenu.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment -> binding.navView.setCheckedItem(R.id.homeFragment)
                R.id.eventsFragment -> binding.navView.setCheckedItem(R.id.eventsFragment)
                R.id.merchandiseFragment -> binding.navView.setCheckedItem(R.id.merchandiseFragment)
                R.id.sponsorsFragment -> binding.navView.setCheckedItem(R.id.sponsorsFragment)
                R.id.coreTeamFragment -> binding.navView.setCheckedItem(R.id.coreTeamFragment)
                R.id.contactFragment -> binding.navView.setCheckedItem(R.id.contactFragment)
                else -> binding.navView.setCheckedItem(R.id.homeFragment)
            }
            binding.appBar.tvTitle.text = when(destination.id) {
                R.id.homeFragment -> "Home"
                R.id.eventsFragment -> "Events"
                R.id.merchandiseFragment -> "Merchandise"
                R.id.sponsorsFragment -> "Past Sponsors"
                R.id.coreTeamFragment -> "Core Team"
                R.id.contactFragment -> "Contact Us"
                else -> "Srijan 24"
            }
        }

        binding.navView.setupWithNavController(navController)
        binding.navView.setCheckedItem(R.id.homeFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}