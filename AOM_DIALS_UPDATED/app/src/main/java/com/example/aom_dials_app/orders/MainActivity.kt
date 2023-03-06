package com.example.aom_dials_app.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()  //to hide the action bar shown above splash screen

        Handler().postDelayed({val intent = Intent(
            this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()},
            3000)
    }
}