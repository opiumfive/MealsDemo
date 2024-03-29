package com.opiumfive.livetypingdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.iterika.marvel.api.enqueue
import com.opiumfive.livetypingdemo.api.IApi
import com.opiumfive.livetypingdemo.feature.list.MealsRepo
import kotlinx.android.synthetic.main.activity_list.*
import org.koin.android.ext.android.inject

class ListActivity : AppCompatActivity() {

    val repo : MealsRepo by inject()

    var menuItem: MenuItem? = null
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(toolbar)
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment? ?: return
        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        findNavController(R.id.hostFragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.filterFragment -> menuItem?.isVisible = false
                R.id.catalogFragment -> {
                    menuItem?.setIcon(if (repo.isFilterApplied()) R.drawable.filter_chosen else R.drawable.filter)
                    menuItem?.isVisible = true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        menuItem = menu?.findItem(R.id.filter)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter) {
            findNavController(R.id.hostFragment).navigate(R.id.actionFilter, null)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
       return findNavController(R.id.hostFragment).navigateUp()
    }
}