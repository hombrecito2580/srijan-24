package com.iitism.srijan24.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.databinding.ActivityMainBinding
import com.razorpay.PaymentResultListener

class MainActivity : AppCompatActivity(), PaymentResultListener {
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
            R.id.announcementsFragment,
            R.id.merchandiseFragment,
            R.id.sponsorsFragment,
            R.id.aboutUsFragment,
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
                R.id.announcementsFragment -> binding.navView.setCheckedItem(R.id.announcementsFragment)
                R.id.merchandiseFragment -> binding.navView.setCheckedItem(R.id.merchandiseFragment)
                R.id.sponsorsFragment -> binding.navView.setCheckedItem(R.id.sponsorsFragment)
                R.id.aboutUsFragment -> binding.navView.setCheckedItem(R.id.aboutUsFragment)
                R.id.coreTeamFragment -> binding.navView.setCheckedItem(R.id.coreTeamFragment)
                R.id.contactFragment -> binding.navView.setCheckedItem(R.id.contactFragment)
                else -> binding.navView.setCheckedItem(R.id.homeFragment)
            }
            binding.appBar.tvTitle.text = when(destination.id) {
                R.id.homeFragment -> "Home"
                R.id.eventsFragment -> "Events"
                R.id.announcementsFragment -> "Announcements"
                R.id.merchandiseFragment -> "Merchandise"
                R.id.sponsorsFragment -> "Past Sponsors"
                R.id.aboutUsFragment -> "About Us"
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

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(applicationContext,"Payment success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(applicationContext,"Payment failure", Toast.LENGTH_SHORT).show()
    }
}