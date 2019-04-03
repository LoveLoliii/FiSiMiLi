package com.summersama.fisimili.data.network

import android.text.TextUtils
import com.summersama.fisimili.utils.ConstantUtils
import com.summersama.fisimili.utils.FApplication.Companion.context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

object ServiceCreator {

    private const val BASE_URL = ConstantUtils.QUERY_URL

    private val httpClient = OkHttpClient.Builder()
    //设置缓存路径 内置存储
 //  val  httpCacheDirectory = File(context.cacheDir,"responses")
//外部存储
 //   val httpCacheDirectory =  File(context.externalCacheDir,"responses")
   val cache:Cache = Cache(File(context.cacheDir,"responses"),10*1024)
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.cache(cache).addNetworkInterceptor(Interceptor {
            val rq = it.request()
            // fixme time out error
            val rp  = it.proceed(rq)
            val serverCache = rp.header("Cache-Control")
            if(TextUtils.isEmpty(serverCache)){
                val  cacheContrl = rq.cacheControl().toString()
                if(TextUtils.isEmpty(cacheContrl)){
                    val maxAge = 1*60*60
                    return@Interceptor rp.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control","public,max-age="+maxAge)
                        .build()
                }else{
                    return@Interceptor rp.newBuilder()
                        .addHeader("Cache-Control",cacheContrl)
                        .removeHeader("Pragma")
                        .build()
                }
            }
            rp
        }).build())
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
