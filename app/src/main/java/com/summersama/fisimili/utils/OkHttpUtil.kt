package com.summersama.fisimili.utils

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.summersama.fisimili.utils.OkHttpUtil.Companion.client
import okhttp3.*

import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 会存在多个函数调用client 导致响应延时。
 * @author loveloliii
 */
class OkHttpUtil(client: OkHttpClient?) {

    init {
        var client = client
        if (client == null) {
            client = OkHttpClient.Builder()
                .connectTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((5 * 60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout((5 * 60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .build()
            OkHttpUtil.client = client
        } else {
            OkHttpUtil.client = client
        }
    }


    @Throws(IOException::class)
    internal fun post(url: String, json: String): String {
        val body = RequestBody.create(Json, json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body()!!.string() }
    }

    companion object {

        private val TAG = OkHttpUtil::class.java.canonicalName
        private var client = OkHttpClient()

        operator fun get(url: String, activity: Activity): String {
            val request = Request.Builder()
                .url(url)
                .build()
            val call = client.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("okhttp", e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.e("okhttp", "success")
                    }
                }
            })
            return url
        }

        operator fun get(url: String): Call {
            val request = Request.Builder()
                .url(url)
                .build()

            return client.newCall(request)
        }

        val Json = MediaType.get("application/json; charset=utf-8")
        /*    public static Call postFile(File file,String url,Activity activity) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img","1.jpg",RequestBody.create(MediaType.parse("image/png"),file))
                .addFormDataPart("token",PUtils.getToken(activity));
        RequestBody requestBody = builder.build();
        Request request  =new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        return call;
    }*/

        fun post(queryMap: Map<*, *>, url: String, i: Int): Call {
            val g = Gson()
            val queryStr = g.toJson(queryMap)
            val requestBody = RequestBody.create(Json, queryStr)
            Log.e("onClick: ", queryStr)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client = OkHttpClient.Builder()
                .connectTimeout((i * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((5 * i * 1000).toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout((5 * i * 1000).toLong(), TimeUnit.MILLISECONDS)
                .build()
            return client.newCall(request)
        }

        fun post(map: Map<*, *>, url: String): Call {
            val g = Gson()
            val queryStr = g.toJson(map)
            val requestBody = RequestBody.create(Json, queryStr)
           //Log.e("onClick: ", queryStr)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            return client.newCall(request)
        }
    }

}
