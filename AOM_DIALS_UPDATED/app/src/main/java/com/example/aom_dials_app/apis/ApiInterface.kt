package com.example.aom_dials_app.apis

import retrofit2.Call
import retrofit2.http.*

const val BASE_URL= "https://watch-dials.onrender.com"

interface ApiInterface {

    //here we will be declaring our get requests

    @POST("/api/user/register")
    fun registerUser(@Body request: signupRequest):Call<signupResponse>

    @POST("/api/user/login")
    fun loginUser(@Body request: loginRequest):Call<loginResponse>

    @GET("/api/order/getAllOrders")   //GET ANNOTATION TO SPECIFY WE ARE MAKING A GET REQUEST
    //ADD END POINT OF URL TO THE GET ANNOTATION
    fun Getdata(@Header("Authorization")token: String):Call<orderFormat>

    @POST("/api/order/createOrder")
    fun createOrder(@Header("Authorization")token: String,@Body request: newOrderCreationRequest):Call<createOrderResponse>

    @GET("/api/order/dynamicFilters")
    fun fetchData(@Header("Authorization")token: String,@Query("department") department:String):Call<orderFormat>

    @PATCH("/api/order/updateOrder")
    fun updateOrder(@Header("Authorization") token: String, @Body request: orderUpdationRequest):Call<updateOrderResponse>

    @DELETE("/api/order/deleteOrder")
    fun deleteOrder(@Header("Authorization")token: String, @Query("id") id:String):Call<orderDeletionResponse>
}


