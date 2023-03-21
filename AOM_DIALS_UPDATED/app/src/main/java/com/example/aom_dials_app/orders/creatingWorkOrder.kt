package com.example.aom_dials_app.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.aom_dials_app.R
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.createOrderResponse
import com.example.aom_dials_app.apis.newOrderCreationRequest
import com.example.aom_dials_app.auth.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response

class creatingWorkOrder : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_work_order)

        mechFeatureDropdown()
        numberTypeDropdown()

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@creatingWorkOrder,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@creatingWorkOrder, Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@creatingWorkOrder, ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@creatingWorkOrder, user_Profile::class.java))
                }
            }
            true
        }
    }

    fun mechFeatureDropdown(){
        val spinner: Spinner = findViewById(R.id.mechFeaturesDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.mechanical_feature_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun numberTypeDropdown(){
        val spinner: Spinner = findViewById(R.id.numberTypeDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.number_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun onGenerateButtonClicked(view: View) {
        val progressBar = findViewById<ProgressBar>(R.id.createOrderProgressBar)

        progressBar.visibility= View.VISIBLE

        try{
            val bundle = intent.extras

            val modelNumber= bundle?.getInt("modelNumber")
            val orderDate= bundle?.getString("orderDate")
            val partyName= bundle?.getString("partyName")
            val baseFeatures= bundle?.getString("baseFeatures")
            val extraQty= bundle?.getInt("extraQty")
            val extraFeatures= bundle?.getString("extraFeatures")
            val size= bundle?.getDouble("size")
            val material= bundle?.getString("material")
            val department= bundle?.getString("department")

            val mechfeaturesSpinner=findViewById<Spinner>(R.id.mechFeaturesDropdown)
            val mechfeatures=mechfeaturesSpinner.getSelectedItem().toString()

            val pkgQty=findViewById<EditText>(R.id.pkgQtyInput).editableText.toString().toInt()

            val numberTypeSpinner=findViewById<Spinner>(R.id.numberTypeDropdown)
            val numberType=numberTypeSpinner.getSelectedItem().toString()

            val otherColorIndex=findViewById<EditText>(R.id.otherColorIndex).editableText.toString().toInt()
            val orderName=findViewById<EditText>(R.id.orderName).editableText.toString()
            val copper=findViewById<EditText>(R.id.copper).editableText.toString().toInt()
            val NP=findViewById<EditText>(R.id.NP).editableText.toString().toInt()
            val GP=findViewById<EditText>(R.id.GP).editableText.toString().toInt()

            sessionManager = SessionManager(this)

            val token = sessionManager.fetchAuthToken()

            Log.d("AOM DIALS", "Auth token fetched: $token")

            dataInstance = DataInterface()

            val order = dataInstance.getApiService().createOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",
                newOrderCreationRequest(GP,NP,copper, department!!,
                    extraQty!!,baseFeatures!!,extraFeatures!!,
                    mechfeatures,material!!,
                    modelNumber!!,numberType,orderDate!!,orderName,otherColorIndex,partyName!!,pkgQty,size!!)
            )

            //Log messages for verifying the retrofit call

            val orderRequest = newOrderCreationRequest(GP,NP,copper,department,extraQty,baseFeatures,extraFeatures,mechfeatures,material,
                modelNumber,numberType,orderDate,orderName,otherColorIndex,partyName,pkgQty, size)
            Log.d("createOrder", "createOrderRequest: $orderRequest")

            val apiService = dataInstance.getApiService()
            Log.d("createOrder", "ApiService: $apiService")

            val orderReq = apiService.createOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",orderRequest)
            Log.d("createOrder", "$orderReq")

            order.enqueue(object : retrofit2.Callback<createOrderResponse> {

                override fun onResponse(
                    call: Call<createOrderResponse>,
                    response: Response<createOrderResponse>,
                ) {
                    val Response = response.body()
                    if (Response != null) {
                        if (Response.status == "Success") {

                            progressBar.visibility = View.GONE

                            Log.d("AOM DIALS", "Order created successfully!")

                            Toast.makeText(this@creatingWorkOrder, Response.message, Toast.LENGTH_SHORT)
                                .show()

                            val intent = Intent(this@creatingWorkOrder, Home_Activity::class.java)

                            startActivity(intent)
                        } else {
                            Log.d("AOM DIALS", "Order creation Failed!")

                            progressBar.visibility = View.GONE

                            Toast.makeText(this@creatingWorkOrder, Response.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<createOrderResponse>, t: Throwable) {

                    progressBar.visibility = View.GONE

                    Toast.makeText(
                        this@creatingWorkOrder,
                        "Error! Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }catch (e : Exception){
            progressBar.visibility= View.GONE
            Toast.makeText(this@creatingWorkOrder, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
        }
    }
}