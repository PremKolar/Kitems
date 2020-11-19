package com.nk.tokitelist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var navView: NavigationView? = null
    private val MENU_IDX_FOR_NIGHTMODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)
        setUpNavigation()
        setUpNightModeFeature()
    }

    private fun setUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        navView!!.setupWithNavController(navController)
        navView!!.setNavigationItemSelectedListener(this)
    }

    private fun setUpNightModeFeature() {
        if (AppCompatDelegate.getDefaultNightMode() < 0) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }
        val nightModeItem = navView!!.menu.getItem(MENU_IDX_FOR_NIGHTMODE)
        nightModeItem.isCheckable = true
        nightModeItem.isChecked = true
        val navMenu = navView!!.menu
        val actionView = navMenu!!.findItem(R.id.switch_night_mode)!!.actionView!!.findViewById<View>(R.id.switch_item)
        actionView.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.getDefaultNightMode() % 2) + 1)
            recreate()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        drawer_layout.close()
        // this part checks if current fragment is the same as destination
        return if (findNavController(R.id.nav_host_fragment).currentDestination?.id != item.itemId)
        {
            val builder = when (item.title){
                "Sessions" -> NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setEnterAnim(R.anim.slide_in_up)
                        .setExitAnim(R.anim.slide_out_up)
                        .setPopEnterAnim(R.anim.slide_in_down)
                        .setPopExitAnim(R.anim.slide_out_down)
                else -> NavOptions.Builder() // "Kitems"
                        .setLaunchSingleTop(true)
                        .setEnterAnim(R.anim.slide_in_down)
                        .setExitAnim(R.anim.slide_out_down)
                        .setPopEnterAnim(R.anim.slide_in_up)
                        .setPopExitAnim(R.anim.slide_out_up)
            }
            val options = builder.build()
            return try
            {
                findNavController(R.id.nav_host_fragment).navigate(item.itemId, null, options)
                true
            }
            catch (e: IllegalArgumentException) // couldn't find destination, do nothing
            {
                false
            }
        }
        else
        {
            false
        }
    }
}