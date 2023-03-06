package com.example.aom_dials_app.orders

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.LoginActivity
import com.google.android.material.navigation.NavigationView

class Home_Activity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpViews()

    }

    fun setUpViews() {
        setUpDrawerLayout()
    }

    fun setUpDrawerLayout() {
        setSupportActionBar(findViewById(R.id.appBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        actionBarDrawerToggle=ActionBarDrawerToggle(this@Home_Activity,drawerLayout,
            R.string.open,
            R.string.close
        )
        actionBarDrawerToggle.syncState()

        navView=findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener{ MenuItem ->
            when (MenuItem.itemId) {
                R.id.Home -> {
                    startActivity(Intent(this@Home_Activity, Home_Activity::class.java))
                    true
                }
                R.id.createNewOrder -> {
                    startActivity(Intent(this@Home_Activity,
                        createWorkOrder::class.java))
                    true
                }
                R.id.viewOrders -> {
                    startActivity(Intent(this@Home_Activity, ViewOrders::class.java))
                    true
                }
                R.id.orderStatistics -> {
                    startActivity(Intent(this@Home_Activity, Statistics::class.java))
                    true
                }
                R.id.Logout -> {
                    startActivity(Intent(this@Home_Activity,LoginActivity::class.java))
                    true
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

fun createWorkOrder(view: View) {
            val intent = Intent(this@Home_Activity,
                createWorkOrder::class.java)
            startActivity(intent)
    }

    fun onOrderStatisticsClicked(view: View) {
        val intent = Intent(this@Home_Activity, Statistics::class.java)
        startActivity(intent)
    }

    fun onViewOrdersClicked(view: View) {
        val intent = Intent(this@Home_Activity, ViewOrders::class.java)
        startActivity(intent)
    }
}


