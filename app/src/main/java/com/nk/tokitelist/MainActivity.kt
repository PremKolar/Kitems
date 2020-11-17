package com.nk.tokitelist
// TODO: 12.11.20 animations missing

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private var navView: NavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView!!.setupWithNavController(navController)
//        navView!!.setNavigationItemSelectedListener(this)
        if (AppCompatDelegate.getDefaultNightMode() == -100){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }
        val nightModeItem = navView!!.menu.getItem(2)
        nightModeItem.isCheckable = true
        nightModeItem.isChecked = true
        val navMenu = navView!!.menu
        val actionView = navMenu!!.findItem(R.id.switch_night_mode)!!.actionView!!.findViewById<View>(R.id.switch_item)
        actionView.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.getDefaultNightMode()%2) +1)
            recreate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        println(item)
//        return true
//    }

}