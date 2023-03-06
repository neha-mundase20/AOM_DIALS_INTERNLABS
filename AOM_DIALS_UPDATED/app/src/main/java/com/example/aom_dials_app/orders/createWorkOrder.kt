package com.example.aom_dials_app.orders

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.aom_dials_app.*
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.createOrderResponse
import com.example.aom_dials_app.apis.newOrderCreationRequest
import com.example.aom_dials_app.auth.SessionManager
import retrofit2.Call
import retrofit2.Response
import java.util.*


class createWorkOrder : AppCompatActivity() {

    private lateinit var id1: Button
    private lateinit var id2:TextView
//    private lateinit var selectImageButton: Button
//    private lateinit var selectedImage: ImageView
    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    private lateinit var department: String

    private lateinit var material: String

//    companion object{
//        const val IMAGE_REQUEST_CODE = 100
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_work_order)

        supportActionBar?.hide()

        Material_Dropdown()
        deptDropdown()
//        selectImageButton = findViewById(R.id.selectButton)
//        selectedImage=findViewById(R.id.selectedImagePreview)
//
//        selectImageButton.setOnClickListener{
//            imagePicker()
//        }
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

    fun onGenerateButtonClicked(view: View) {
        val progressBar = findViewById<ProgressBar>(R.id.createOrderProgressBar)

        progressBar.visibility= View.VISIBLE

        val modelNumber= findViewById<EditText>(R.id.modelInput).editableText.toString().toInt()
        val Date = findViewById<View>(R.id.displayDate) as TextView
        val orderDate = Date.text.toString()
        val partyName=findViewById<EditText>(R.id.partyNameInput).editableText.toString()
        val baseFeatures=findViewById<EditText>(R.id.basefeaturesInput).editableText.toString()
        val extraQty=findViewById<EditText>(R.id.extraQtyText).editableText.toString().toInt()
        val extraFeatures=findViewById<EditText>(R.id.extraFeaturesInput).editableText.toString()
        val mechfeatures=findViewById<EditText>(R.id.mechfeaturesInput).editableText.toString()
        val size=findViewById<EditText>(R.id.sizeInput).editableText.toString().toDouble()
        val pkgQty=findViewById<EditText>(R.id.pkgQtyInput).editableText.toString().toInt()
        val numberType=findViewById<EditText>(R.id.numberType).editableText.toString()
        val otherColorIndex=findViewById<EditText>(R.id.otherColorIndex).editableText.toString().toInt()
        val orderName=findViewById<EditText>(R.id.orderName).editableText.toString()
        val copper=findViewById<EditText>(R.id.copper).editableText.toString().toInt()
        val NP=findViewById<EditText>(R.id.NP).editableText.toString().toInt()
        val GP=findViewById<EditText>(R.id.GP).editableText.toString().toInt()

        val materialSpinner=findViewById<Spinner>(R.id.materialDropdown)
        material = materialSpinner.getSelectedItem().toString()

        //val material=materialSpinner.selectedItem.toString()

        val deptSpinner=findViewById<Spinner>(R.id.deptDropdown)
        department = deptSpinner.getSelectedItem().toString()

        //val department=deptSpinner.selectedItem.toString()

        sessionManager = SessionManager(this)

        val token = sessionManager.fetchAuthToken()

        Log.d("AOM DIALS", "Auth token fetched: $token")

        dataInstance = DataInterface()

        val order = dataInstance.getApiService().createOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",
            newOrderCreationRequest(GP,NP,copper,department,
                extraQty,baseFeatures,extraFeatures,mechfeatures,material,
                modelNumber,numberType,orderDate,orderName,otherColorIndex,partyName,pkgQty, size)
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

                        Toast.makeText(this@createWorkOrder, Response.message, Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(this@createWorkOrder, Home_Activity::class.java)

                        startActivity(intent)
                    } else {
                        Log.d("AOM DIALS", "Order creation Failed!")

                        progressBar.visibility = View.GONE

                        Toast.makeText(this@createWorkOrder, Response.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<createOrderResponse>, t: Throwable) {

                progressBar.visibility = View.GONE

                Toast.makeText(
                    this@createWorkOrder,
                    "Error! Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

//    fun imagePicker() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type="image/*"
//        startActivityForResult(intent, IMAGE_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode== IMAGE_REQUEST_CODE && resultCode== RESULT_OK){
//            selectedImage.setImageURI(data?.data)
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onChooseDateButtonClicked(view: View) {
        val button = findViewById<Button>(R.id.chooseDateButton)
        button.setOnClickListener { datePickerDialog(it) }
    }

}