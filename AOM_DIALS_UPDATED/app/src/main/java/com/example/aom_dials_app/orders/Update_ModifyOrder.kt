package com.example.aom_dials_app.orders

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.SessionManager
import com.example.aom_dials_app.apis.orderUpdationRequest
import com.example.aom_dials_app.apis.updateOrderResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response
import java.util.*

class Update_ModifyOrder : AppCompatActivity() {

    private lateinit var id1: Button
    private lateinit var id2: TextView

    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    private lateinit var orderId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_modify_order)

        Material_Dropdown()
        deptDropdown()
        baseFeatureDropdown()
        extraFeatureDropdown()
        mechFeatureDropdown()
        numberTypeDropdown()

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@Update_ModifyOrder,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@Update_ModifyOrder,Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@Update_ModifyOrder,ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@Update_ModifyOrder, user_Profile::class.java))
                }
            }
            true
        }

        val bundle = intent.extras
        val orderName = bundle?.getString("orderName")
        orderId = bundle?.getString("orderId").toString()
        val orderDate = bundle?.getString("orderDate")
        val otherclrIndex = bundle?.getInt("otherclrIndex")
        val modelNumber = bundle?.getInt("modelNumber")
        val material = bundle?.getString("material")
        val baseFeature = bundle?.getString("baseFeature")
        val extraFeatures = bundle?.getString("extraFeatures")
        val mechanicalFeatures = bundle?.getString("mechanicalFeatures")
        val numberType = bundle?.getString("numberType")
        val extraQty = bundle?.getInt("extraQty")
        val deptName = bundle?.getString("deptName")
        val partyName = bundle?.getString("partyName")
        val pkgQty = bundle?.getInt("pkgQty")
        val size = bundle?.getDouble("size")
        val GP = bundle?.getInt("GP")
        val NP = bundle?.getInt("NP")
        val copper = bundle?.getInt("copper")

        val modelnumber = findViewById<EditText>(R.id.modelInput)
        modelnumber.setText(modelNumber.toString())

        val orderdate = findViewById<TextView>(R.id.displayDate)
        if (orderDate != null) {
            orderdate.setText(orderDate.substringBefore("T"))
        }

        val partyname = findViewById<EditText>(R.id.partyNameInput)
        partyname.setText(partyName)

        val materialSpinner=findViewById<Spinner>(R.id.materialDropdown)
        val materialAdapter = ArrayAdapter.createFromResource(this, R.array.material_array,android.R.layout.simple_spinner_item)
        materialSpinner.setSelection(materialAdapter.getPosition(material))

        val deptSpinner=findViewById<Spinner>(R.id.deptDropdown)
        val deptAdapter = ArrayAdapter.createFromResource(this, R.array.dept_array,android.R.layout.simple_spinner_item)
        deptSpinner.setSelection(deptAdapter.getPosition(deptName))

        val baseFeaturesSpinner=findViewById<Spinner>(R.id.baseFeaturesDropdown)
        val baseFeatureAdapter = ArrayAdapter.createFromResource(this, R.array.base_feature_array,android.R.layout.simple_spinner_item)
        baseFeaturesSpinner.setSelection(baseFeatureAdapter.getPosition(baseFeature))

        val extraqty = findViewById<EditText>(R.id.extraQtyText)
        extraqty.setText(extraQty.toString())

        val extraFeaturesSpinner=findViewById<Spinner>(R.id.extraFeaturesDropdown)
        val extraFeaturesAdapter = ArrayAdapter.createFromResource(this, R.array.extra_feature_array,android.R.layout.simple_spinner_item)
        extraFeaturesSpinner.setSelection(extraFeaturesAdapter.getPosition(extraFeatures))

        val mechfeaturesSpinner = findViewById<Spinner>(R.id.mechFeaturesDropdown)
        val mechFeaturesAdapter=ArrayAdapter.createFromResource(this,R.array.mechanical_feature_array,android.R.layout.simple_spinner_item)
        mechfeaturesSpinner.setSelection(mechFeaturesAdapter.getPosition(mechanicalFeatures))

        val Size = findViewById<EditText>(R.id.sizeInput)
        Size.setText(size.toString())

        val PkgQty = findViewById<EditText>(R.id.pkgQtyInput)
        PkgQty.setText(pkgQty.toString())

        val numberTypeSpinner=findViewById<Spinner>(R.id.numberTypeDropdown)
        val numberTypeAdapter = ArrayAdapter.createFromResource(this, R.array.number_type_array,android.R.layout.simple_spinner_item)
        numberTypeSpinner.setSelection(numberTypeAdapter.getPosition(numberType))

        val OtherColorIndex = findViewById<EditText>(R.id.otherColorIndex)
        OtherColorIndex.setText(otherclrIndex.toString())

        val OrderName = findViewById<EditText>(R.id.orderName)
        OrderName.setText(orderName)

        val Copper = findViewById<EditText>(R.id.copper)
        Copper.setText(copper.toString())

        val np = findViewById<EditText>(R.id.NP)
        np.setText(NP.toString())

        val gp = findViewById<EditText>(R.id.GP)
        gp.setText(GP.toString())
    }

    fun Material_Dropdown() {
        val spinner: Spinner = findViewById(R.id.materialDropdown)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.material_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

    }

    fun deptDropdown(){
        val spinner: Spinner = findViewById(R.id.deptDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.dept_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun baseFeatureDropdown(){
        val spinner: Spinner = findViewById(R.id.baseFeaturesDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.base_feature_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun extraFeatureDropdown(){
        val spinner: Spinner = findViewById(R.id.extraFeaturesDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.extra_feature_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun datePickerDialog(view: View) {

        id1 = findViewById(R.id.chooseDateButton)
        id2 = findViewById(R.id.displayDate)

        //Date picker using calender
        val calender = Calendar.getInstance()

        //displaying date
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calender.set(Calendar.YEAR,year)
            calender.set(Calendar.MONTH,month)
            calender.set(Calendar.DATE,dayOfMonth)
            updateLabel(calender)
        }

        id1.setOnClickListener{
            DatePickerDialog(this,datePicker,calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),calender.get(Calendar.DATE)).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(calender: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        id2.setText(sdf.format(calender.time))
    }

    fun onUpdateButtonClicked(view: View) {

        val ProgressBar = findViewById<ProgressBar>(R.id.updateProgressBar)

        ProgressBar.visibility = View.VISIBLE

        try{
            val modelNumber= findViewById<EditText>(R.id.modelInput).editableText.toString().toInt()
            val Date = findViewById<View>(R.id.displayDate) as TextView
            val orderDate = Date.text.toString()
            val partyName=findViewById<EditText>(R.id.partyNameInput).editableText.toString()
            val extraQty=findViewById<EditText>(R.id.extraQtyText).editableText.toString().toInt()

            val extraFeaturesSpinner=findViewById<Spinner>(R.id.extraFeaturesDropdown)
            val extraFeatures=extraFeaturesSpinner.getSelectedItem().toString()

            val baseFeaturesSpinner=findViewById<Spinner>(R.id.baseFeaturesDropdown)
            val baseFeatures=baseFeaturesSpinner.getSelectedItem().toString()

            val mechfeaturesSpinner=findViewById<Spinner>(R.id.mechFeaturesDropdown)
            val mechfeatures=mechfeaturesSpinner.getSelectedItem().toString()

            val numberTypeSpinner=findViewById<Spinner>(R.id.numberTypeDropdown)
            val numberType=numberTypeSpinner.getSelectedItem().toString()

            val size=findViewById<EditText>(R.id.sizeInput).editableText.toString().toDouble()
            val pkgQty=findViewById<EditText>(R.id.pkgQtyInput).editableText.toString().toInt()
            val otherColorIndex=findViewById<EditText>(R.id.otherColorIndex).editableText.toString().toInt()
            val orderName=findViewById<EditText>(R.id.orderName).editableText.toString()
            val copper=findViewById<EditText>(R.id.copper).editableText.toString().toInt()
            val NP=findViewById<EditText>(R.id.NP).editableText.toString().toInt()
            val GP=findViewById<EditText>(R.id.GP).editableText.toString().toInt()

            val materialSpinner=findViewById<Spinner>(R.id.materialDropdown)
            val material = materialSpinner.getSelectedItem().toString()

            val deptSpinner=findViewById<Spinner>(R.id.deptDropdown)
            val department = deptSpinner.getSelectedItem().toString()


            sessionManager = SessionManager(this)

            val token = sessionManager.fetchAuthToken()

            Log.d("AOM DIALS", "Auth token fetched: $token")

            dataInstance = DataInterface()

            val update = dataInstance.getApiService().updateOrder(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                orderUpdationRequest(orderId,GP,NP,copper,department,
                    extraQty,baseFeatures,extraFeatures,mechfeatures,material,
                    modelNumber,numberType,orderDate,orderName,otherColorIndex,partyName,pkgQty,size)
            )

            //Log messages for verifying the retrofit call

            val updateRequest = orderUpdationRequest(orderId,GP,NP,copper,department,extraQty,baseFeatures,extraFeatures,mechfeatures,material,
                modelNumber,numberType,orderDate,orderName,otherColorIndex,partyName,pkgQty,size)
            Log.d("updateOrder", "updateOrderRequest: $updateRequest")

            val apiService = dataInstance.getApiService()
            Log.d("updateOrder", "ApiService: $apiService")

            val updateReq = apiService.updateOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",updateRequest)
            Log.d("updateOrder", "$updateReq")

            update.enqueue(object : retrofit2.Callback<updateOrderResponse> {

                override fun onResponse(
                    call: Call<updateOrderResponse>,
                    response: Response<updateOrderResponse>,
                ) {
                    val Response = response.body()
                    if (Response != null) {
                        if (Response.status == "Success") {

                            Log.d("AOM DIALS", "Order updated successfully!")

                            ProgressBar.visibility = View.GONE

                            Toast.makeText(this@Update_ModifyOrder, Response.message, Toast.LENGTH_SHORT)
                                .show()

                            val intent = Intent(this@Update_ModifyOrder, Home_Activity::class.java)

                            startActivity(intent)
                        } else {
                            Log.d("AOM DIALS", "Order updation Failed!")

                            ProgressBar.visibility = View.GONE

                            Toast.makeText(this@Update_ModifyOrder, Response.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<updateOrderResponse>, t: Throwable) {

                    ProgressBar.visibility = View.GONE

                    Toast.makeText(
                        this@Update_ModifyOrder,
                        "Error! Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }catch (e : Exception){
            ProgressBar.visibility= View.GONE
            Toast.makeText(this@Update_ModifyOrder, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
        }
    }


}