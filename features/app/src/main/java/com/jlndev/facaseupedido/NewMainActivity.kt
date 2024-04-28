package com.jlndev.facaseupedido

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.databinding.ActivityMainBinding
import com.jlndev.facaseupedido.databinding.ActivityNewMainBinding

class NewMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)

        configToolbar()
        configBottomNavigation()
    }

    private fun configToolbar() {
        setSupportActionBar(binding.appBarMain.toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_cart,
                R.id.nav_profile,
                R.id.nav_order_history,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun configBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
        handleBottomNavigationVisibility()
    }


    private fun handleBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home,
                R.id.nav_cart,
                R.id.nav_profile,
                R.id.nav_order_history, -> {
                    showBottomMenu()
                }
                else -> {
                    hideBottomMenu()
                }
            }
        }
    }
    private fun showBottomMenu() {
        binding.bottomNavigation.visible()
    }

    private fun hideBottomMenu() {
        binding.bottomNavigation.gone()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}