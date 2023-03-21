package com.example.aom_dials_app.orders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.SessionManager
import com.example.aom_dials_app.apis.orderFormat
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Statistics : AppCompatActivity() {

    lateinit var department: String
    lateinit var adapter: RecyclerViewDataAdapter
    private lateinit var dataInstance: DataInterface
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        StatisticsDropdown()

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val MenuItem = menu.getItem(1)
        MenuItem.setChecked(true)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@Statistics,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@Statistics,Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@Statistics,ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@Statistics, user_Profile::class.java))
                }
            }
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@Statistics, Home_Activity::class.java)
        startActivity(intent)
    }

    fun StatisticsDropdown() {
        val statisticsSpinner=findViewById<Spinner>(R.id.statisticsDropdown)
        ArrayAdapter.createFromResource(
            this,
            R.array.statistics_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statisticsSpinner.adapter = adapter
        }
    }

    fun showDeptwiseStatistics(view: View) {
        val statisticsSpinner=findViewById<Spinner>(R.id.statisticsDropdown)
        department = statisticsSpinner.getSelectedItem().toString()

        Log.d("AOM DIALS","Department : $department")

        dataInstance = DataInterface()

        sessionManager = SessionManager(this)

        val token = sessionManager.fetchAuthToken()

        Log.d("AOM DIALS", "Auth token fetched: $token")

        val recyclerView = findViewById<RecyclerView>(R.id.statisticsRecyclerView)

        adapter = RecyclerViewDataAdapter(recyclerView,this, emptyList())

        recyclerView.adapter = adapter

        Log.d("AOM DIALS", "Recycler View Adapter : ${recyclerView.adapter} ")

        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.d("AOM DIALS", "Adapter set successfully")

        val order= dataInstance.getApiService().fetchData(token = "Bearer ${sessionManager.fetchAuthToken()}",department)



        order.enqueue(object : Callback<orderFormat> {
            override fun onResponse(call: Call<orderFormat>, response: Response<orderFormat>) {
                val order = response.body()
                if (order != null) {

                    Log.d("AOM DIALS",order.status)

                    Log.d("AOM DIALS", order.toString())

                    adapter = RecyclerViewDataAdapter(recyclerView, this@Statistics,order.orders)

                    recyclerView.adapter=adapter

                    Log.d("AOM DIALS", "Recycler View Adapter : ${recyclerView.adapter} ")

                }
            }

            override fun onFailure(call: Call<orderFormat>, t: Throwable) {
                Log.d("AOM DIALS", "Error in fetching")
            }

        })
    }

}