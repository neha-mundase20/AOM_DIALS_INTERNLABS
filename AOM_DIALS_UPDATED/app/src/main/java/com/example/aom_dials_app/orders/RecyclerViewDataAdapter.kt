package com.example.aom_dials_app.orders

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.aom_dials_app.apis.DataInterface
import com.example.aom_dials_app.apis.Orders
import com.example.aom_dials_app.R
import com.example.aom_dials_app.auth.SessionManager
import com.example.aom_dials_app.apis.orderDeletionResponse
import com.example.aom_dials_app.auth.SignupActivity
import retrofit2.Call
import retrofit2.Response


class RecyclerViewDataAdapter(private val recyclerView: RecyclerView, val context: Context,
                              var articles:List<Orders>): RecyclerView.Adapter<RecyclerViewDataAdapter.listViewHolder>() {

    private lateinit var sessionManager: SessionManager

    private lateinit var dataInstance : DataInterface

    private lateinit var orderid : String

        fun setOrderList(list: List<Orders>) {
        articles = list
    }


    class listViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderName: TextView = itemView.findViewById(R.id.orderName)
        var orderId:TextView = itemView.findViewById(R.id.orderId)
        var orderDate: TextView = itemView.findViewById(R.id.orderDate)
        var otherclrIndex: TextView = itemView.findViewById(R.id.otherColorIndex)
        var modelNumber: TextView = itemView.findViewById(R.id.modelNumber)
        var material: TextView = itemView.findViewById(R.id.material)
        var baseFeature: TextView = itemView.findViewById(R.id.baseFeature)
        var extraFeatures: TextView = itemView.findViewById(R.id.extraFeatures)
        var mechanicalFeatures: TextView = itemView.findViewById(R.id.mechanicalFeatures)
        var numberType: TextView = itemView.findViewById(R.id.numberType)
        var extraQty: TextView = itemView.findViewById(R.id.extraQty)
        var deptName: TextView = itemView.findViewById(R.id.deptName)
        var partyName: TextView = itemView.findViewById(R.id.partyName)
        var pkgQty: TextView = itemView.findViewById(R.id.pkgQty)
        var size: TextView = itemView.findViewById(R.id.size)
        var GP: TextView = itemView.findViewById(R.id.GP)
        var NP: TextView = itemView.findViewById(R.id.NP)
        var coppper:TextView=itemView.findViewById(R.id.copper)
        //var orderImageView: ImageView = itemView.findViewById(R.id.orderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listViewHolder {
        //LayoutInflater converts R.layouts.statistics_xml to view which we can pass in statistics view holder

        val view = LayoutInflater.from(context).inflate(R.layout.data_items,parent,false)

        Log.d("AOM DIALS","View : $view")

        return listViewHolder(view)
    }

    override fun onBindViewHolder(holder: listViewHolder, position: Int) {
        val currentItem = articles[position]

        //Log.d("AOM DIALS","Current item-orderName:${currentItem.orderName}")
        holder.orderName.text= currentItem.orderName

        //Log.d("AOM DIALS","Current item-orderDate:${currentItem.orderDate}")

        holder.orderId.text= currentItem._id

        holder.orderDate.text= currentItem.orderDate

        holder.otherclrIndex.text= currentItem.otherColorIndex.toString()

        holder.modelNumber.text= currentItem.modelNo.toString()

        holder.material.text= currentItem.material

        holder.baseFeature.text= currentItem.feature1BaseFeature

        holder.extraFeatures.text= currentItem.feature2Extra

        holder.mechanicalFeatures.text= currentItem.feature3Mechanical

        holder.numberType.text= currentItem.numberType

        holder.extraQty.text= currentItem.extraQty.toString()

        holder.deptName.text= currentItem.department

        holder.partyName.text= currentItem.partyName

        holder.pkgQty.text= currentItem.pkgQty.toString()

        holder.size.text= currentItem.size.toString()

        holder.GP.text= currentItem.GP.toString()

        holder.NP.text= currentItem.NP.toString()

        holder.coppper.text=currentItem.copper.toString()

        //Glide.with(context).load(currentItem.orderPicture).into(holder.orderImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Update_ModifyOrder::class.java)
            val bundle = Bundle()
            bundle.putString("orderName", currentItem.orderName)
            bundle.putString("orderId", currentItem._id)
            bundle.putString("orderDate", currentItem.orderDate)
            bundle.putInt("otherclrIndex", currentItem.otherColorIndex)
            bundle.putInt("modelNumber", currentItem.modelNo)
            bundle.putString("material", currentItem.material)
            bundle.putString("baseFeature", currentItem.feature1BaseFeature)
            bundle.putString("extraFeatures", currentItem.feature2Extra)
            bundle.putString("mechanicalFeatures", currentItem.feature3Mechanical)
            bundle.putString("numberType", currentItem.numberType)
            bundle.putInt("extraQty", currentItem.extraQty)
            bundle.putString("deptName", currentItem.department)
            bundle.putString("partyName", currentItem.partyName)
            bundle.putInt("pkgQty", currentItem.pkgQty)
            bundle.putDouble("size", currentItem.size)
            bundle.putInt("GP", currentItem.GP)
            bundle.putInt("NP", currentItem.NP)
            bundle.putInt("copper", currentItem.copper)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {

            orderid=currentItem._id

            Log.d("AOM DIALS","$orderid")

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to delete this order?")
            builder.setPositiveButton("Yes") { dialog, which ->
                sessionManager = SessionManager(context)

                val token = sessionManager.fetchAuthToken()

                Log.d("AOM DIALS", "Auth token fetched: $token")

                dataInstance = DataInterface()

                val delete = dataInstance.getApiService().deleteOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",
                    orderid
                )

                //Log messages for verifying the retrofit call

                val apiService = dataInstance.getApiService()
                Log.d("DeleteOrder", "ApiService: $apiService")

                val delReq = apiService.deleteOrder(token = "Bearer ${sessionManager.fetchAuthToken()}",orderid)
                Log.d("DeleteOrder", "$delReq")

                Log.d("AOM DIALS","$orderid")

                delete.enqueue(object : retrofit2.Callback<orderDeletionResponse> {

                    override fun onResponse(
                        call: Call<orderDeletionResponse>,
                        response: Response<orderDeletionResponse>,
                    ) {
                        val Response = response.body()
                        if (Response != null) {
                            if (Response.status == "success") {

                                Log.d("AOM DIALS","${Response.message}")

                                Log.d("AOM DIALS", "Order deleted successfully!")

//                                val adapter = recyclerView.adapter as? RecyclerViewDataAdapter
//                                adapter?.let {
//                                    val updatedOrderList = articles.filter { it._id != orderid }
//                                    setOrderList(updatedOrderList)
//                                    recyclerView.adapter?.notifyDataSetChanged()
//                                    recyclerView.adapter = RecyclerViewDataAdapter(recyclerView,context,articles)
//                                }
                                Toast.makeText(context, Response.message, Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, Home_Activity::class.java)
                                startActivity(context,intent,null)

                            } else {
                                Log.d("AOM DIALS", "Order deletion Failed!")

                                Toast.makeText(context, Response.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<orderDeletionResponse>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Error! Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            builder.show()

            true // indicate that the long click has been handled
            }
        }

    override fun getItemCount(): Int {

        Log.d("AOM DIALS","${articles.size}")

        return  articles.size

    }
}
