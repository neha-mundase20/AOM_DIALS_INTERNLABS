package com.example.aom_dials_app.orders

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.aom_dials_app.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class createWorkOrder : AppCompatActivity() {

    private lateinit var id1: Button
    private lateinit var id2:TextView

    private lateinit var department: String

    private lateinit var material: String
//    private lateinit var selectImageButton: Button
//    private lateinit var selectedImage: ImageView

//    companion object{
//        const val IMAGE_REQUEST_CODE = 100
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_work_order)

        supportActionBar?.hide()

        Material_Dropdown()
        deptDropdown()
        baseFeatureDropdown()
        extraFeatureDropdown()
//        selectImageButton = findViewById(R.id.selectButton)
//        selectedImage=findViewById(R.id.selectedImagePreview)
//
//        selectImageButton.setOnClickListener{
//            imagePicker()
//        }
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@createWorkOrder,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@createWorkOrder,Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@createWorkOrder,ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@createWorkOrder, user_Profile::class.java))
                }
            }
            true
        }
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


fun onNextButtonClicked(view: View) {
    try {
    val modelNumber= findViewById<EditText>(R.id.modelInput).editableText.toString().toInt()
    val Date = findViewById<View>(R.id.displayDate) as TextView
    val orderDate = Date.text.toString()
    val partyName=findViewById<EditText>(R.id.partyNameInput).editableText.toString()
    val extraQty=findViewById<EditText>(R.id.extraQtyText).editableText.toString().toInt()

    val extraFeaturesSpinner=findViewById<Spinner>(R.id.extraFeaturesDropdown)
    val extraFeatures=extraFeaturesSpinner.getSelectedItem().toString()

    val size=findViewById<EditText>(R.id.sizeInput).editableText.toString().toDouble()

    val baseFeaturesSpinner=findViewById<Spinner>(R.id.baseFeaturesDropdown)
    val baseFeatures=baseFeaturesSpinner.getSelectedItem().toString()

    val materialSpinner=findViewById<Spinner>(R.id.materialDropdown)
    material = materialSpinner.getSelectedItem().toString()

    val deptSpinner=findViewById<Spinner>(R.id.deptDropdown)
    department = deptSpinner.getSelectedItem().toString()

            val bundle = Bundle()
            bundle.putInt("modelNumber",modelNumber)
            bundle.putString("orderDate",orderDate)
            bundle.putString("partyName",partyName)
            bundle.putString("baseFeatures",baseFeatures)
            bundle.putInt("extraQty",extraQty)
            bundle.putString("extraFeatures",extraFeatures)
            bundle.putDouble("size",size)
            bundle.putString("material",material)
            bundle.putString("department",department)

            val intent = Intent(this@createWorkOrder, creatingWorkOrder::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

    } catch (e: Exception) {
        Toast.makeText(this@createWorkOrder, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
    }
}


}