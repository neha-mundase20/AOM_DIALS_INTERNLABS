package com.example.aom_dials_app.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class ViewOrders : AppCompatActivity() {

    lateinit var adapter: RecyclerViewDataAdapter
    private lateinit var dataInstance: DataInterface
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menu = bottomNavigationView.menu
        val MenuItem = menu.getItem(2)
        MenuItem.setChecked(true)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    startActivity(Intent(this@ViewOrders,Home_Activity::class.java))
                }
                R.id.ic_orderStats -> {
                    startActivity(Intent(this@ViewOrders,Statistics::class.java))
                }
                R.id.ic_viewOrders -> {
                    startActivity(Intent(this@ViewOrders,ViewOrders::class.java))
                }
                R.id.ic_Profile -> {
                    startActivity(Intent(this@ViewOrders, user_Profile::class.java))
                }
            }
            true
        }

        dataInstance = DataInterface()

        sessionManager = SessionManager(this)

        val token = sessionManager.fetchAuthToken()

        Log.d("AOM DIALS", "Auth token fetched: $token")

        val recyclerView = findViewById<RecyclerView>(R.id.searchOrderRecyclerview)

        adapter = RecyclerViewDataAdapter(recyclerView,this@ViewOrders, emptyList())

        recyclerView.adapter = adapter

        Log.d("AOM DIALS", "Recycler View Adapter : ${recyclerView.adapter} ")

        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.d("AOM DIALS", "Adapter set successfully")

        val order= dataInstance.getApiService().Getdata(token = "Bearer ${sessionManager.fetchAuthToken()}")


        order.enqueue(object : Callback<orderFormat> {
            override fun onResponse(call: Call<orderFormat>, response: Response<orderFormat>) {
                val order = response.body()
                Log.d("AOM DIALS", "API call successful. Response body: $order")

                adapter = RecyclerViewDataAdapter(recyclerView,this@ViewOrders,order!!.orders)

                recyclerView.adapter = adapter

                Log.d("AOM DIALS", "Recycler View Adapter : ${recyclerView.adapter} ")
            }

            override fun onFailure(call: Call<orderFormat>, t: Throwable) {
                Log.d("AOM DIALS", "API call failed: $t")
            }

        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@ViewOrders, Home_Activity::class.java)
        startActivity(intent)
    }
}