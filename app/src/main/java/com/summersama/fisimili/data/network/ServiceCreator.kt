package com.summersama.fisimili.data.network

import com.summersama.fisimili.utils.ConstantUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceCreator {

    private const val BASE_URL = ConstantUtils.QUERY_URL

    private val httpClient = OkHttpClient.Builder()

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    fun <T> create(serviceClass:Class<T>,url:String): T  {
         return  Retrofit.Builder()
             .baseUrl(url)
             .client(httpClient.build())
             .addConverterFactory(ScalarsConverterFactory.create())
             .addConverterFactory(GsonConverterFactory.create()).build().create(serviceClass)
    }
}
