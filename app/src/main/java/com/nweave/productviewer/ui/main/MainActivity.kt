package com.nweave.productviewer.ui.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.nweave.productviewer.ui.products.AllProductsFragment
import com.nweave.productviewer.ui.products.CheapestProductsFragment
import com.nweave.productviewer.ui.products.ExpensiveProductsFragment
import com.nweave.productviewer.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.nweave.productviewer.data.local.FetchMethodStorage
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fetchMethodStorage: FetchMethodStorage

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        getString(R.string.using_method, fetchMethodStorage.getFetchMethod())

        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_all -> AllProductsFragment()
                R.id.nav_cheapest -> CheapestProductsFragment()
                R.id.nav_expensive -> ExpensiveProductsFragment()
                else -> AllProductsFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            drawerLayout.closeDrawers()
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AllProductsFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_all)
        }
    }
}