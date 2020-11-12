package com.nk.tokitelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.nk.tokitelist.data.viewmodel.ToKiteViewModel
import com.nk.tokitelist.fragments.list.ListFragment


class Main2Activity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )

//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////         Inflate the menu; this adds items menuto the action bar if it is present.
//        menuInflater.inflate(R.menu.list_fragment_menu, menu)
//
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        var listFrag = supportFragmentManager.findFragmentById(R.id.listFragment)
//
//        val firstFragment: ListFragment = supportFragmentManager.fragments[0] as ListFragment
//
////        when (item.itemId){
////            R.id.menu_summer -> listFrag.filterOnSummer()
////            R.id.menu_winter -> listFrag.filterOnWinter()
////            R.id.menu_restart -> listFrag.restart()
////        }
//        return super.onOptionsItemSelected(item)
//    }

//    private fun restart() {
//        mToKiteViewModel.restart()
//    }
//
//    private fun filterOnWinter() {
////        setSeason(Season.winter)
////        triggerDataSetting(mToKiteViewModel.getAllData.value)
//
//    }
//
//    private fun filterOnSummer() {
////        setSeason(Season.summer)
////        triggerDataSetting(mToKiteViewModel.getAllData.value)
//    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}