package com.example.aom_dials_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aom_dials_app.*
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.signupRequest
import com.example.aom_dials_app.apis.signupResponse
import com.example.aom_dials_app.orders.Home_Activity
import retrofit2.Call
import retrofit2.Response

class SignupActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    private lateinit var user:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val admin = findViewById<RadioButton>(R.id.adminRadioButton)
        val manager = findViewById<RadioButton>(R.id.managerRadioButton)

        admin.setOnClickListener {
            user=admin.text.toString()
        }

        manager.setOnClickListener {
            user=manager.text.toString()
        }

    }

    fun onsignUpButtonClicked(view: View) {

        val progressBar = findViewById<ProgressBar>(R.id.progressBarsignup)

        progressBar.visibility = View.VISIBLE

        val name= findViewById<EditText>(R.id.UserName) .editableText.toString()
        val email=findViewById<EditText>(R.id.EmailAddress).editableText.toString()
        val password=findViewById<EditText>(R.id.Password).editableText.toString()
        val password_confirmation=findViewById<EditText>(R.id.ConfirmPassword).editableText.toString()
        val userType=user
        val tc = "true"

        sessionManager = SessionManager(this)

        dataInstance = DataInterface()

        //Log messages for verifying the retrofit call

//        val signupRequest = signupRequest(name,email,password,password_confirmation,userType,tc)
//        Log.d("SignUp", "signupRequest: $signupRequest")
//
//        val apiService = dataInstance.getApiService()
//        Log.d("SignUp", "ApiService: $apiService")
//
//        val signup1 = apiService.registerUser(signupRequest)
//        Log.d("SignUp", "signup: $signup1")


        val signup = dataInstance.getApiService().registerUser(signupRequest(name,email,password,password_confirmation,userType,tc))

        signup.enqueue(object : retrofit2.Callback<signupResponse> {
            override fun onResponse(
                call: Call<signupResponse>,
                response: Response<signupResponse>,
            ) {
                val Response = response.body()
                if (Response != null) {
                    if (Response.status == "Success") {

                        Log.d("AOM DIALS","SignUp Successfull")

                        progressBar.visibility = View.GONE

                        sessionManager.saveAuthToken(Response.token)

                        Toast.makeText(this@SignupActivity,Response.message,Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@SignupActivity, Home_Activity::class.java)

                        startActivity(intent)
                    }
                    else {
                        Log.d("AOM DIALS","SignUp Failed")

                        progressBar.visibility = View.GONE

                        Toast.makeText(this@SignupActivity,Response.message,Toast.LENGTH_SHORT).show()
                    }
                }
                }

            override fun onFailure(call: Call<signupResponse>, t: Throwable) {

                progressBar.visibility = View.GONE

                Toast.makeText(this@SignupActivity,"Error! Please try again",Toast.LENGTH_SHORT).show()
            }

        })


    }
}




