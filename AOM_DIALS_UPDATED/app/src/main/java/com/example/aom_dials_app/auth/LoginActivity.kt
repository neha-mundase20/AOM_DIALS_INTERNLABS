package com.example.aom_dials_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aom_dials_app.*
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.loginRequest
import com.example.aom_dials_app.apis.loginResponse
import com.example.aom_dials_app.orders.Home_Activity
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    @Deprecated("Deprecated in Java", ReplaceWith("finishAffinity()"))
    override fun onBackPressed() {
        finishAffinity()
    }

    fun onSignupTextClicked(view: View) {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
    }

    fun onLoginButtonClicked(view: View) {
        try {
            val progressBar = findViewById<ProgressBar>(R.id.progressBarlogin)

            val email=findViewById<EditText>(R.id.EmailAddress).editableText.toString()
            val password=findViewById<EditText>(R.id.Password).editableText.toString()

            if(email.length==0 || password.length==0){
                Toast.makeText(this@LoginActivity,"Please fill all the fields!",Toast.LENGTH_SHORT).show()
            }

            else{
                progressBar.visibility = View.VISIBLE

                sessionManager = SessionManager(this)

                dataInstance = DataInterface()

                //Log messages for verifying the retrofit call

                val loginRequest = loginRequest(email,password)
                Log.d("Login", "loginRequest: $loginRequest")

                val apiService = dataInstance.getApiService()
                Log.d("Login", "ApiService: $apiService")

                val login1 = apiService.loginUser(loginRequest)
                Log.d("Login", "login: $login1")

                val login = dataInstance.getApiService().loginUser(loginRequest(email,password))

                login.enqueue(object : retrofit2.Callback<loginResponse> {
                    override fun onResponse(
                        call: Call<loginResponse>,
                        response: Response<loginResponse>,
                    ) {
                        val Response = response.body()
                        if (Response != null) {
                            if (Response.status == "success") {

                                Log.d("AOM DIALS","Login Successfull")

                                sessionManager.saveAuthToken(Response.token)

                                progressBar.visibility = View.GONE

                                Toast.makeText(this@LoginActivity,Response.message, Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, Home_Activity::class.java)

                                startActivity(intent)
                            }
                            else {
                                Log.d("AOM DIALS","Login Failed")

                                progressBar.visibility = View.GONE

                                Toast.makeText(this@LoginActivity,Response.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<loginResponse>, t: Throwable) {

                        progressBar.visibility = View.GONE

                        Toast.makeText(this@LoginActivity,"Error! Please try again", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }catch (e : Exception){
            Toast.makeText(this@LoginActivity,"Please fill all the fields!",Toast.LENGTH_SHORT).show()
        }


    }
}