package com.example.aom_dials_app.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataInterface {

    private lateinit var  dataInstance: ApiInterface

    init {
        //create retrofit builder obj
        if (!::dataInstance.isInitialized){
            val retrofitbuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            dataInstance = retrofitbuilder.create(ApiInterface::class.java)
        }
    }

    fun getApiService(): ApiInterface {
        return dataInstance
    }
}