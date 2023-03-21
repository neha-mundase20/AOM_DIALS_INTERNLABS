package com.example.aom_dials_app.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.aom_dials_app.R
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.loggedUser
import com.example.aom_dials_app.apis.loginResponse
import com.example.aom_dials_app.apis.orderFormat
import com.example.aom_dials_app.auth.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class user_Profile : AppCompatActivity() {

    private lateinit var dataInstance: DataInterface
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val MenuItem = menu.getItem(3)
        MenuItem.setChecked(true)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@user_Profile, Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@user_Profile, Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@user_Profile, ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@user_Profile, user_Profile::class.java))
                }
            }
            true
        }

        val user = findViewById<TextView>(R.id.userName)
        val email = findViewById<TextView>(R.id.userEmail)
        val type = findViewById<TextView>(R.id.userType)

        dataInstance = DataInterface()

        sessionManager = SessionManager(this)

        val token = sessionManager.fetchAuthToken()

        Log.d("AOM DIALS", "Auth token fetched: $token")

        val LoggedUser = dataInstance.getApiService().GetLoggedUser(token = "Bearer ${sessionManager.fetchAuthToken()}")

        LoggedUser.enqueue(object : Callback<loggedUser> {
            override fun onResponse(call: Call<loggedUser>, response: Response<loggedUser>) {
                val currentUser = response.body()
                Log.d("AOM DIALS", "API call successful. Response body: $currentUser")

                if (currentUser != null) {
                    user.text = currentUser.name
                    email.text = currentUser.email
                    type.text = currentUser.userType
                }

            }

            override fun onFailure(call: Call<loggedUser>, t: Throwable) {
                Log.d("AOM DIALS", "API call failed: $t")
            }

        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@user_Profile, Home_Activity::class.java)
        startActivity(intent)
    }
}