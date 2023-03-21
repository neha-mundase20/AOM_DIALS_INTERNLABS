package com.example.aom_dials_app.orders

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val MenuItem = menu.getItem(0)
        MenuItem.setChecked(true)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@Home_Activity,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@Home_Activity,Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@Home_Activity,ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@Home_Activity, user_Profile::class.java))
                }
            }
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@Home_Activity, LoginActivity::class.java)
        startActivity(intent)
    }

    fun createNewOrder(view: View) {
        startActivity(Intent(this@Home_Activity,createWorkOrder::class.java))
    }

}


